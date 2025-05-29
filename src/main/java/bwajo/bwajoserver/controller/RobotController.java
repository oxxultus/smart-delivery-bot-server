package bwajo.bwajoserver.controller;

import bwajo.bwajoserver.dto.PaymentListNumberResultMessage;
import bwajo.bwajoserver.dto.SensorDto;
import bwajo.bwajoserver.entity.*;
import bwajo.bwajoserver.service.CartListService;
import bwajo.bwajoserver.service.PaymentService;
import bwajo.bwajoserver.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
public class RobotController {

        @Autowired
        PaymentService paymentService;

        // TODO: 추후 서비스에서 싱글톤으로 관리하게 변경해야 한다.
        // 결제 내역 정리
        private static PaymentList paymentList;

        // 아두이노가 로딩할 현재 작업 중 결제 내역
        private static WorkingPaymentList workingPaymentList;

        // 카트 =============================================================================================================

        // 아두이노가 결제 내역을 GET으로 요청할 때 응답
        @GetMapping("/bot/payment")
        public void getPaymentData(HttpServletResponse response) throws IOException {
            // System.out.println("[서버] 최신 결제 내역 요청받음");

            List<PaymentList> paymentLists = paymentService.findAllPaymentsList().stream()
                    .filter(p -> p.getPaymentStatus() == PaymentStatus.PAYMENT_SUCCESS)
                    .sorted(Comparator.comparing(PaymentList::getId))  // ID 기준 오름차순
                    .toList();

            if (paymentLists.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                return;
            }

            paymentList = paymentLists.getFirst();
            System.out.println("[서버][결제내역 갱신] 결제내역 갱신 완료");

            String paymentId = paymentList.getUniqueNumber();
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode root = mapper.createObjectNode();
            root.put("paymentId", paymentId);

            for (PaymentItem item : paymentList.getPaymentItems()) {
                String name = item.getItem().getName();
                String uid = item.getItem().getUniqueValue();
                int quantity = item.getQuantity();

                ArrayNode array = mapper.createArrayNode();
                array.add(uid);
                array.add(String.valueOf(quantity));
                root.set(name, array);
            }

            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(root);
            byte[] jsonBytes = json.getBytes(StandardCharsets.UTF_8);

            response.setContentType("application/json");
            response.setContentLength(jsonBytes.length);
            response.getOutputStream().write(jsonBytes);
        }

        // 결제내역 불러온것이 있다면 해당 결제내역을 바탕으로 작업 목록에 결제내역 추가
        @GetMapping("/bot/first-set-working-list")
        public ResponseEntity<String> setWorkingList() {
            if (paymentList == null) {
                System.out.println("[서버][작업 리스트 생성] paymentList가 존재하지 않습니다.");
                return ResponseEntity.status(400).body("현재 paymentList가 설정되어 있지 않습니다.");
            }

            // WorkingPaymentList를 빈 리스트로 생성
            workingPaymentList = new WorkingPaymentList(
                    paymentList.getId(),
                    paymentList.getUniqueNumber(),
                    paymentList.getPaymentStatus(),
                    paymentList.getUser(),
                    new ArrayList<>()  // 빈 작업 리스트
            );

            System.out.println("[서버][작업 리스트 생성] 초기 작업 리스트 생성 완료: " + workingPaymentList.getUniqueNumber());
            return ResponseEntity.ok("초기 작업 리스트 생성 완료");
        }

        @GetMapping("/bot/add-working-list")
        public ResponseEntity<String> addWorkingItem(@RequestParam String uid) {
            // System.out.println("[서버] 작업목록 추가 요청 받음: " + uid);

            if (workingPaymentList == null) {
                System.out.println("[서버][작업 목록 추가] 작업 리스트가 먼저 설정되지 않았습니다.");
                return ResponseEntity.status(400).body("작업 리스트가 먼저 설정되지 않았습니다.");
            }

            if (paymentList == null) {
                System.out.println("[서버][작업 목록 추가] paymentList가 존재하지 않습니다.");
                return ResponseEntity.status(400).body("paymentList가 존재하지 않습니다.");
            }

            // 중복 UID 확인
            boolean alreadyExists = workingPaymentList.getWorkingPaymnetListItem().stream()
                    .anyMatch(item -> item.getUid().equals(uid));
            if (alreadyExists) {
                System.out.println("[서버][작업 목록 추가] 이미 해당 UID가 작업 목록에 등록되어 있습니다.");
                return ResponseEntity.status(409).body("이미 해당 UID가 작업 목록에 등록되어 있습니다.");
            }

            // UID로 paymentList에서 항목 탐색
            Optional<PaymentItem> match = paymentList.getPaymentItems().stream()
                    .filter(item -> item.getItem().getUniqueValue().equals(uid))
                    .findFirst();

            if (match.isEmpty()) {
                System.out.println("[서버][작업 목록 추가] 해당 UID에 해당하는 상품을 찾을 수 없습니다.");
                return ResponseEntity.status(404).body("해당 UID에 해당하는 상품을 찾을 수 없습니다.");
            }

            // 항목 이름과 수량 추출
            String itemName = match.get().getItem().getName();
            Long itemCount = (long) match.get().getQuantity();

            // 생성자 사용해 객체 생성 및 추가
            WorkingPaymnetListItem newItem = new WorkingPaymnetListItem(uid, itemName, itemCount);
            workingPaymentList.getWorkingPaymnetListItem().add(newItem);

            System.out.println("[서버][작업 목록 추가] 작업 항목 추가됨: " + uid);

            return ResponseEntity.ok("작업 항목이 성공적으로 추가되었습니다.");
        }

        @GetMapping("/bot/reset-working-list")
        public ResponseEntity<String> resetWorkingList() {
            if (workingPaymentList == null) {
                return ResponseEntity.status(400).body("현재 작업 리스트가 없습니다.");
            }

            if (workingPaymentList.getPaymentStatus() != PaymentStatus.DELIVERY_PROCESSING) {
                return ResponseEntity.status(400).body("작업 상태가 DELIVERY_PROCESSING이 아닙니다.");
            }

            // 상태 변경 수행
            String uniqueNumber = workingPaymentList.getUniqueNumber();
            PaymentListNumberResultMessage result = paymentService.updateStatus(uniqueNumber, PaymentStatus.DELIVERY_COMPLETE);

            if (result.getCode() == 500) {
                return ResponseEntity.status(500).body("상태 업데이트 실패: " + result.getMessage());
            }

            // 상태 변경 후 작업 리스트 초기화
            workingPaymentList = null;

            System.out.println("[서버][작업목록 재설정] 결제내역 " + uniqueNumber + "의 상태를 DELIVERY_COMPLETE로 변경하고 작업 리스트를 초기화했습니다.");
            return ResponseEntity.ok("결제 상태를 완료로 변경하고 작업 리스트를 초기화했습니다.");
        }


        // 진열대 ============================================================================================================
        // 요청 방식: GET /end/working-list?uid=e3221014 같은 형식으로 요청

            //532eb318 : 2번
            //c3a4e195 : 1번

        // 진열대의 작업량 할당
        @GetMapping("/check/working-list")
        public void checkWorkingList(@RequestParam String uid, HttpServletResponse response) throws IOException {

            if (workingPaymentList == null) {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                return;
            }

            Optional<WorkingPaymnetListItem> match = workingPaymentList.getWorkingPaymnetListItem().stream()
                    .filter(item -> item.getUid().equals(uid) && "미완".equals(item.getStatus()))
                    .findFirst();

            ObjectMapper mapper = new ObjectMapper();
            ObjectNode json = mapper.createObjectNode();

            if (match.isPresent()) {
                json.put("count", match.get().getCount());
                System.out.println("[서버][진열대 작업량 할당] 요청 진열대 UID: " + uid + " 의 값 " + match.get().getCount() + " 전달 완료.");
            } else {
                json.put("count", 0);
                System.out.println("[서버][진열대 작업량 할당] 요청 진열대 UID: " + uid + " 에 해당하는 작업이 없어 수량 0 을 전달 합니다");
            }

            byte[] jsonBytes = mapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(json);
            response.setContentType("application/json");
            response.setContentLength(jsonBytes.length);
            response.getOutputStream().write(jsonBytes);
        }

        // 진열대의 1회 작업 종료
        @GetMapping("/end/working-list")
        public ResponseEntity<String> endWorkingItem(@RequestParam String uid) {
            if (workingPaymentList == null) {
                return ResponseEntity.status(400).body("작업 리스트가 존재하지 않습니다.");
            }

            Optional<WorkingPaymnetListItem> match = workingPaymentList.getWorkingPaymnetListItem().stream()
                    .filter(item -> item.getUid().equals(uid))
                    .findFirst();

            if (match.isEmpty()) {
                return ResponseEntity.status(404).body("해당 UID를 가진 항목이 없습니다.");
            }

            // 상태를 '완료'로 변경
            match.get().setStatus("완료");
            System.out.println("[서버][진열대 작업완료] UID " + uid + " 작업 상태를 '완료'로 변경했습니다.");

            // 모든 항목 완료 시 처리 로직 (선택사항)
            /*
            boolean allCompleted = workingPaymentList.getWorkingPaymnetListItem().stream()
                    .allMatch(item -> "완료".equals(item.getStatus()));

            if (allCompleted) {
                workingPaymentList.setPaymentStatus(PaymentStatus.DELIVERY_COMPLETE);
                System.out.println("[서버] 모든 항목 완료됨 → 상태를 DELIVERY_COMPLETE로 변경");
            }
            */

            printWorkingPaymentList();

            // 로봇에게 다시 작동 명령 전달
            try {
                String robotUrl = UriComponentsBuilder.fromHttpUrl("http://oxxultus-bot.kro.kr:8081/go")
                        .toUriString();
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getForEntity(robotUrl, String.class);
                System.out.println("[서버 → 로봇] /go 명령 전송 완료");
            } catch (Exception e) {
                System.err.println("[서버 → 로봇] /go 명령 전송 실패: " + e.getMessage());
            }

            return ResponseEntity.ok(uid + "에 해당하는 상품이 완료 상태로 변경되었습니다.");
        }

        // 작업 내역 출력
        public static void printWorkingPaymentList() {
            if (workingPaymentList == null) {
                System.out.println("[디버그] 현재 workingPaymentList가 null입니다.");
                return;
            }

            System.out.println("=== [작업 리스트 상태 출력] ===");
            System.out.println("결제 ID: " + workingPaymentList.getId());
            System.out.println("고유 번호: " + workingPaymentList.getUniqueNumber());
            System.out.println("결제 상태: " + workingPaymentList.getPaymentStatus());
            System.out.println("사용자: " + workingPaymentList.getUser().getId());

            System.out.println("→ 작업 항목 목록:");
            for (WorkingPaymnetListItem item : workingPaymentList.getWorkingPaymnetListItem()) {
                System.out.println(" - UID: " + item.getUid() +
                        ", 이름: " + item.getName() +
                        ", 수량: " + item.getCount() +
                        ", 상태: " + item.getStatus());
            }
            System.out.println("=== [작업 리스트 끝] ===");
        }


        // TEST ========================================================================================================

        @GetMapping("/clear-memory-data")
        public ResponseEntity<String> clear() {
            workingPaymentList = null;
            paymentList = null;
            System.out.println("[서버][초기화] 결제내역 및 작업공간 초기화 완료");
            return ResponseEntity.status(200).body("결제내역 및 작업공간 초기화 완료");
        }
}

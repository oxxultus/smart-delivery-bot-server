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
import org.springframework.web.bind.annotation.*;

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
            System.out.println("[서버] 최신 결제 내역 요청받음");

            List<PaymentList> paymentLists = paymentService.findAllPaymentsList().stream()
                    .filter(p -> p.getPaymentStatus() == PaymentStatus.PAYMENT_SUCCESS)
                    .sorted(Comparator.comparing(PaymentList::getId))  // ID 기준 오름차순
                    .toList();

            if (paymentLists.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                return;
            }

            paymentList = paymentLists.getFirst();
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
        @PostMapping("/bot/set-working-list")
        public ResponseEntity<String> setWorkingList() {
            if (paymentList == null) {
                return ResponseEntity.status(400).body("현재 paymentList가 설정되어 있지 않습니다.");
            }

            // 변환: PaymentList → List<WorkingPaymnetListItem>
            List<WorkingPaymnetListItem> workingItems = new ArrayList<>();
            for (PaymentItem item : paymentList.getPaymentItems()) {
                workingItems.add(new WorkingPaymnetListItem(
                        item.getItem().getUniqueValue(),
                        item.getItem().getName(),
                        (long) item.getQuantity()
                ));
            }

            // WorkingPaymentList 생성 및 저장
            workingPaymentList = new WorkingPaymentList(
                    paymentList.getId(),
                    paymentList.getUniqueNumber(),
                    paymentList.getPaymentStatus(),
                    paymentList.getUser(),
                    workingItems
            );

            System.out.println("[서버] workingPaymentList 저장 완료: " + workingPaymentList.getUniqueNumber());
            return ResponseEntity.ok("작업 리스트 저장 완료");
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

            System.out.println("[서버] " + uniqueNumber + " 상태를 DELIVERY_COMPLETE로 변경하고 작업 리스트를 초기화했습니다.");
            return ResponseEntity.ok("결제 상태를 완료로 변경하고 작업 리스트를 초기화했습니다.");
        }


        // 진열대 ============================================================================================================
        // 요청 방식: GET /end/working-list?uid=e3221014 같은 형식으로 요청

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
            } else {
                json.put("count", 0);
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

            match.get().setStatus("완료");

            System.out.println("[서버] UID " + uid + " 상태를 '완료'로 변경했습니다.");

            // TODO: 로봇에게 다시 작동명령 혹은 선반에서 작동명령

            return ResponseEntity.ok(uid + "에 해당하는 상품이 완료 상태로 변경되었습니다.");
        }


}

package bwajo.bwajoserver.controller;

import bwajo.bwajoserver.dto.SensorDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class RobotController {

    // 1. 아두이노 → Spring: 센서 데이터 전송
    @PostMapping("/sensor")
    public ResponseEntity<Map<String, String>> receiveSensorData(@RequestBody SensorDto sensor) {
        Map<String, String> response = new HashMap<>();
        response.put("payment", "paid");
        return ResponseEntity.ok(response);
    }

    // ✅ 아두이노가 결제 내역을 GET으로 요청할 때 응답
    @GetMapping("/bot/payment")
    public void getPaymentData(HttpServletResponse response) throws IOException {
        System.out.println("요청받음");

        String json = """
    {
        "paymentId": "123e4567-e89b-12d3-a456-426655440000",
        "과자": ["a1b2c3d4", "3"],
        "음료수": ["f5e6d7c8", "2"],
        "사탕": ["e3221014", "1"]
    }
    """;

        byte[] jsonBytes = json.getBytes(StandardCharsets.UTF_8);
        response.setContentType("application/json");
        response.setContentLength(jsonBytes.length);
        response.getOutputStream().write(jsonBytes);
    }

    // 2. Spring → 아두이노: 명령 전달
    @GetMapping("/command")
    public ResponseEntity<String> sendCommand() {
        return ResponseEntity.ok("TURN_ON");  // 예: LED 켜기 명령
    }

    // 장바구니에서 준비 중인 상품 리스트를 가져와서 가장 최신의 내역을 전송한다 Get으로


    // 상품 적재 완료 후 해당 결재내역의 상태를 변경한다.

}

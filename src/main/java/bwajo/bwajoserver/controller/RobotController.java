package bwajo.bwajoserver.controller;

import bwajo.bwajoserver.dto.SensorDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RobotController {

    // 1. 아두이노 → Spring: 센서 데이터 전송
    @PostMapping("/sensor")
    public ResponseEntity<String> receiveSensorData(@RequestBody SensorDto sensor) {
        System.out.println("📥 요청 수신됨 (임시 로그)");
        System.out.println("전달 받은 값: " + sensor.getTemperature());
        return ResponseEntity.ok("데이터 수신 완료");
    }

    // 2. Spring → 아두이노: 명령 전달
    @GetMapping("/command")
    public ResponseEntity<String> sendCommand() {
        return ResponseEntity.ok("TURN_ON");  // 예: LED 켜기 명령
    }

    // 장바구니에서 준비 중인 상품 리스트를 가져와서 가장 최신의 내역을 전송한다 Get으로


    // 상품 적재 완료 후 해당 결재내역의 상태를 변경한다.

}

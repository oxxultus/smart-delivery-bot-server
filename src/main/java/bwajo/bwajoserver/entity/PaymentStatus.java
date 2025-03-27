package bwajo.bwajoserver.entity;

public enum PaymentStatus {
    PAYMENT_SUCCESS,        // 결제 완료
    DELIVERY_PREPARING,     // 배달 준비중
    DELIVERY_PROCESSING,    // 배송 처리
    DELIVERY_COMPLETE,      // 배송 완료
    DELIVERY_FAILED         // 배송 실패
}

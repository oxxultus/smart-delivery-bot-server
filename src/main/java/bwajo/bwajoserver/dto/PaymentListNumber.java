package bwajo.bwajoserver.dto;

public class PaymentListNumber extends ResultMessage {
    private String uniqueNumber;

    public PaymentListNumber(int code, String message, String uniqueNumber) {
        super(code, message);
        this.uniqueNumber = uniqueNumber;
    }

    public String getUniqueNumber() {
        return uniqueNumber;
    }

    public void setUniqueNumber(String uniqueNumber) {
        this.uniqueNumber = uniqueNumber;
    }
}

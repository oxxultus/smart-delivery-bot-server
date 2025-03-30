package bwajo.bwajoserver.dto;

public class PaymentListNumberResultMessage extends ResultMessage {
    private String uniqueNumber;

    public PaymentListNumberResultMessage(int code, String message, String uniqueNumber) {
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

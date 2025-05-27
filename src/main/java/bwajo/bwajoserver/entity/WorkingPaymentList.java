package bwajo.bwajoserver.entity;

import java.util.ArrayList;
import java.util.List;

public class WorkingPaymentList {

    private Long id;

    private String uniqueNumber;
    private PaymentStatus paymentStatus;

    private User user;

    private List<WorkingPaymnetListItem> workingPaymnetListItem;

    public WorkingPaymentList(Long id, String uniqueNumber, PaymentStatus paymentStatus, User user, List<WorkingPaymnetListItem> workingPaymnetListItem) {
        this.id = id;
        this.uniqueNumber = uniqueNumber;
        this.paymentStatus = paymentStatus;
        this.user = user;
        this.workingPaymnetListItem = workingPaymnetListItem;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUniqueNumber() {
        return uniqueNumber;
    }

    public void setUniqueNumber(String uniqueNumber) {
        this.uniqueNumber = uniqueNumber;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<WorkingPaymnetListItem> getWorkingPaymnetListItem() {
        return workingPaymnetListItem;
    }

    public void setWorkingPaymnetListItem(List<WorkingPaymnetListItem> workingPaymnetListItem) {
        this.workingPaymnetListItem = workingPaymnetListItem;
    }
}

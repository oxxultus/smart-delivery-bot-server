package bwajo.bwajoserver.entity;

public class WorkingPaymnetListItem {
    private String uid;
    private String name;
    private Long count;
    private String status = "미완";

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public WorkingPaymnetListItem(String uid, String name, Long count) {
        this.uid = uid;
        this.name = name;
        this.count = count;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}

package lk.choizy.company;

public class Feedback {

    String compId,studentName,branchId,msg;

    public Feedback() {
    }

    public Feedback(String compId, String studentName, String branchId, String msg) {
        this.compId = compId;
        this.studentName = studentName;
        this.branchId = branchId;
        this.msg = msg;
    }

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

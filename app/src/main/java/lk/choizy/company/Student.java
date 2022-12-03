package lk.choizy.company;

import com.google.firebase.database.Exclude;

public class Student {

    public String imageName;
    public String imageURL;
    public String student_id;
    public String student_name;
    public String student_email;
    public String student_gender;
    public String student_bday;
    public String student_nic;
    public String student_mobile;
    public String student_password;
    public double student_loan;
    private String key;

    @Exclude
    public String getKeey() {return key;}
    @Exclude
    public void setKeey(String key) {
        this.key = key;
    }

    public Student(){}

    public Student(String student_id, String student_name, String student_email, String student_gender, String student_bday, String student_nic, String student_mobile, String student_password, String url) {
        this.student_id = student_id;
        this.student_name = student_name;
        this.student_email = student_email;
        this.student_gender = student_gender;
        this.student_bday = student_bday;
        this.student_nic = student_nic;
        this.student_mobile = student_mobile;
        this.student_password = student_password;
        this.imageURL = url;

    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getStudent_email() {
        return student_email;
    }

    public void setStudent_email(String student_email) {
        this.student_email = student_email;
    }

    public String getStudent_gender() {
        return student_gender;
    }

    public void setStudent_gender(String student_gender) {
        this.student_gender = student_gender;
    }

    public String getStudent_bday() {
        return student_bday;
    }

    public void setStudent_bday(String student_bday) {
        this.student_bday = student_bday;
    }

    public String getStudent_nic() {
        return student_nic;
    }

    public void setStudent_nic(String student_nic) {
        this.student_nic = student_nic;
    }

    public String getStudent_mobile() {
        return student_mobile;
    }

    public void setStudent_mobile(String student_mobile) {
        this.student_mobile = student_mobile;
    }

    public String getStudent_password() {
        return student_password;
    }

    public void setStudent_password(String student_password) {
        this.student_password = student_password;
    }

    public double getStudent_loan() {
        return student_loan;
    }

    public void setStudent_loan(double student_loan) {
        this.student_loan = student_loan;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

}

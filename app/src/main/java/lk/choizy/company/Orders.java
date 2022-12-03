package lk.choizy.company;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;

import lk.choizy.company.CompanyBraunch.Cart;

public class Orders {
    String orderID;
    String branchID;
    String studentKey,studentName,studentEmail,studentID;
    String PaymentType;
    String orderType;
    String delivery;
    String studentMobile;
    String notificationKey;
    boolean paymentDone;
    String date;
    String time;
    String orderStatus;

    @Exclude
    Student student;

    ArrayList<Cart> cartList;


    public Orders() {
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getPaymentType() {
        return PaymentType;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public void setPaymentType(String paymentType) {
        this.PaymentType = paymentType;
    }

    public String getNotificationKey() {
        return notificationKey;
    }

    public void setNotificationKey(String notificationKey) {
        this.notificationKey = notificationKey;
    }

    @Exclude
    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getBranchID() {
        return branchID;
    }

    public String getStudentMobile() {
        return studentMobile;
    }

    public void setStudentMobile(String studentMobile) {
        this.studentMobile = studentMobile;
    }

    public void setBranchID(String branchID) {
        this.branchID = branchID;
    }

    public String getStudentKey() {
        return studentKey;
    }

    public boolean isPaymentDone() {
        return paymentDone;
    }

    public void setPaymentDone(boolean paymentDone) {
        this.paymentDone = paymentDone;
    }

    public void setStudentKey(String studentKey) {
        this.studentKey = studentKey;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public ArrayList<Cart> getCartList() {
        return cartList;
    }

    public void setCartList(ArrayList<Cart> cartList) {
        this.cartList = cartList;
    }

    @Exclude
    public double getTotal(){
        double total = 0;
        for (Cart cart:cartList) {
            total+= cart.getOffer().getPrice()*cart.getQut();
        }

        return total;
    }

    @Exclude
    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

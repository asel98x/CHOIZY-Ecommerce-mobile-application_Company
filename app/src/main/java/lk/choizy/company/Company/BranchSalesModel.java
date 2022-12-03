package lk.choizy.company.Company;

public class BranchSalesModel {

    String offerName;
    String date,time;
    double price;

    public BranchSalesModel(String offerName, String date, String time, double price) {
        this.offerName = offerName;
        this.date = date;
        this.time = time;
        this.price = price;
    }

    public String getOfferName() {
        return offerName;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public double getPrice() {
        return price;
    }
}

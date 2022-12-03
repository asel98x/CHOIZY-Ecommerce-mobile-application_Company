package lk.choizy.company.CompanyBraunch;

public class Sales {

    String yearMonth;
    double totalPrice;

    public Sales(String yearMonth, double totalPrice) {
        this.yearMonth = yearMonth;
        this.totalPrice = totalPrice;
    }

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}

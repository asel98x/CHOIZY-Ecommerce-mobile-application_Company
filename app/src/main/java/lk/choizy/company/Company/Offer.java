package lk.choizy.company.Company;

import com.google.firebase.database.Exclude;

public class Offer {

    String offerId, BranchID,Title,Description,OfferUrl;
    double price;

    public Offer() {

    }

    public Offer(String branchID, String title, String description, String Url, double price) {
        BranchID = branchID;
        Title = title;
        Description = description;
        this.price = price;
        this.OfferUrl = Url;
    }

    public String getOfferUrl() {
        return OfferUrl;
    }

    public void setOfferUrl(String offerUrl) {
        OfferUrl = offerUrl;
    }

    public String getBranchID() {
        return BranchID;
    }

    public void setBranchID(String branchID) {
        BranchID = branchID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Exclude
    public String getOfferId() {
        return offerId;
    }
    @Exclude
    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }
}

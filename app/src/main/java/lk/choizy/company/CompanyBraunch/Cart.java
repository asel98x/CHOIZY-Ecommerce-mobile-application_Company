package lk.choizy.company.CompanyBraunch;

import lk.choizy.company.Company.Offer;

public class Cart {

    Offer offer;
    int qut;

    public Cart(Offer offer, int qut) {
        this.offer = offer;
        this.qut = qut;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public int getQut() {
        return qut;
    }

    public void setQut(int qut) {
        this.qut = qut;
    }

    String OfferID;
    String BranchID;
    String BranchName;
    String delivery;


    public Cart() {

    }



}

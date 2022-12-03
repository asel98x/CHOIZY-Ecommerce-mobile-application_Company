package lk.choizy.company.Company;

import com.google.firebase.database.Exclude;

public class RecommendedAD {

    String ad_ID,url,compID,type,title;
    public RecommendedAD(){

    }
    public RecommendedAD(String url,String compID) {
        this.url = url;
        this.compID = compID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompID() {
        return compID;
    }

    public void setCompID(String compID) {
        this.compID = compID;
    }

    @Exclude
    public String getAd_ID() {
        return ad_ID;
    }

    public void setAd_ID(String ad_ID) {
        this.ad_ID = ad_ID;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

package lk.choizy.company.Company;

import android.widget.ImageView;

import com.google.firebase.database.Exclude;

public class UpcomingAD {

   String Id,Url,compID, type;

    public UpcomingAD() {

    }

    public UpcomingAD(String compID, String url) {
        this.compID = compID;
        Url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Exclude
    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getCompID() {
        return compID;
    }

    public void setCompID(String compID) {
        this.compID = compID;
    }
}

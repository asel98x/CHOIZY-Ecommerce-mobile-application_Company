package lk.choizy.company.Company;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.Query;

import lk.choizy.company.BranchHelper;
import lk.choizy.company.Feedback;
import lk.choizy.company.Users;

public class CompanyHelper extends BranchHelper {


    public CompanyHelper(Context context) {
        super(context);
    }

    public String getCompanyId(){
        SharedPreferences preferences = getContext().getSharedPreferences(CASharePreference, MODE_PRIVATE);

        return preferences.getString("CompanyID",null);
    }

    public void logoutCompPreference() {
        SharedPreferences userData = getContext().getSharedPreferences(CASharePreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userData.edit();

        // LoginDetails details = db.customerDetials(emailAddresss.getText().toString(),password.getText().toString());

        editor.remove("CompanyID");


        editor.apply();
    }

    public Task<Void> insertRecommendedAd(RecommendedAD recommendedAD){
        return myRef.child(RecommendedAD.class.getSimpleName()).push().setValue(recommendedAD);
    }

    public Task<Void> updateRecommended(RecommendedAD recommendedAD){
        return myRef.child(RecommendedAD.class.getSimpleName()).child(recommendedAD.getAd_ID()).setValue(recommendedAD);
    }


    public Query getFeedBack(String branchID){
        return myRef.child(Feedback.class.getSimpleName()).orderByChild("branchId").equalTo(branchID);
    }



}

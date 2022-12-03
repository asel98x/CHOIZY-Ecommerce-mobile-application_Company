package lk.choizy.company;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;

import lk.choizy.company.DB_Class;
import lk.choizy.company.Orders;

public class BranchHelper extends DB_Class {

    static NumberFormat format;
    Calendar date = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");

    public BranchHelper(Context context) {
        super(context);
        this.format = NumberFormat.getCurrencyInstance();
        format.setMinimumFractionDigits(2);
        format.setCurrency(Currency.getInstance("LKR"));
    }

    public String getBranchId(){
        SharedPreferences preferences = getContext().getSharedPreferences(CASharePreference, MODE_PRIVATE);

        return preferences.getString("BranchID",null);
    }

    public void logoutPreference() {
        SharedPreferences userData = getContext().getSharedPreferences(CASharePreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userData.edit();

        // LoginDetails details = db.customerDetials(emailAddresss.getText().toString(),password.getText().toString());

        editor.remove("BranchID");


        editor.apply();
    }

    public void RemoveEventListener(ValueEventListener valueEventListener){
        myRef.removeEventListener(valueEventListener);
    }

    public static String getFormattedPrice(double price){
        return format.format(price);
    }

    public Query studentGetDetailsByKey(String key){
        return myRef.child("Student").child(key);
    }

    public Query getOngoingOrder(){
        return myRef.child("OrderOngoing").orderByChild("branchID").equalTo(getBranchId());
    }

    public Task<Void> updateOrder(Orders order){
        return myRef.child("OrderOngoing").child(order.getOrderID()).setValue(order);
    }

    public Task<Void> FinishOrder(Orders order){
        myRef.child("OrderOngoing").child(order.getOrderID()).removeValue();
        myRef.child("BranchSales").child(order.getBranchID()).child(dateFormat.format(date.getTime())).child(order.getOrderID()).setValue(order);
        return myRef.child("OrderHistory").child(order.getOrderID()).setValue(order);
    }

    public Query getHistoryOrder(String branchID){
        return myRef.child("OrderHistory").orderByChild("branchID").equalTo(branchID);
    }

    public Query getCurrentSale(){
        return myRef.child("BranchSales").child(getBranchId()).child(dateFormat.format(date.getTime()));
    }

    public Query getPreviousSale(){
        Calendar pre = date;
        pre.add(Calendar.MONTH,-1);
        System.out.println(pre.getTime());
        System.out.println(date.getTime());
        return myRef.child("BranchSales").child(getBranchId()).orderByKey().endAt(dateFormat.format(pre.getTime()));
    }

    public Query getFeedBack(){
        return myRef.child(Feedback.class.getSimpleName()).orderByChild("branchId").equalTo(getBranchId());
    }

    public Query getUser(String id){
        return myRef.child(Users.class.getSimpleName()).child(id);
    }

}

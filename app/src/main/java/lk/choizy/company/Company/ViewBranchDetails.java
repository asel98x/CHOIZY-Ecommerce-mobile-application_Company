package lk.choizy.company.Company;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import lk.choizy.company.CompanyBraunch.Cart;
import lk.choizy.company.CompanyBraunch.Sales;
import lk.choizy.company.Orders;
import lk.choizy.company.R;
import lk.choizy.company.network_change_listner;

public class ViewBranchDetails extends AppCompatActivity {

    RecyclerView branchSaleLV;
    String branchID,branchName;
    CompanyHelper db_helper;

    ArrayList<BranchSalesModel> saleList;
    ValueEventListener salesListener;
    BranchSalesCardAdapter adapter;
    TextView branchNameTxt,priceTxt;
    network_change_listner networkChangeListener = new network_change_listner();
    double total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_branch_details);

        branchNameTxt = findViewById(R.id.viewBranch_Name);
        priceTxt = findViewById(R.id.viewBranch_Price);

        Intent intent = getIntent();
        branchID = intent.getStringExtra("BranchID");
        branchName = intent.getStringExtra("BranchName");
        db_helper = new CompanyHelper(getApplicationContext());

        branchNameTxt.setText(branchName);
        priceTxt.setText(db_helper.getFormattedPrice(total));


        saleList = new ArrayList<>();

        branchSaleLV = findViewById(R.id.BranchSaleListView);

        adapter =new BranchSalesCardAdapter();


        branchSaleLV.setAdapter(adapter);
        branchSaleLV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        branchSaleLV.addItemDecoration(new BranchSalesCardAdapter.SalesDecoration());
        loadSales();

    }

    public void ViewBranchFeedback(View view){
        if(branchID != null){
            Intent intent = new Intent(getApplicationContext(),CompBranchFeedback.class);
            intent.putExtra("BranchID",branchID);
            startActivity(intent);
        }
    }

    private void loadSales(){
        salesListener = db_helper.getHistoryOrder(branchID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                saleList.clear();
                total = 0;
                for (DataSnapshot one: dataSnapshot.getChildren()) {
                    Orders orders = one.getValue(Orders.class);
                    total += orders.getTotal();
                    for (Cart cart :orders.getCartList()) {
                        saleList.add(new BranchSalesModel(cart.getOffer().getTitle(),orders.getDate(), orders.getTime(), cart.getOffer().getPrice()* cart.getQut()));
                    }

                }
                Collections.reverse(saleList);
                adapter.setList(saleList);
                adapter.notifyDataSetChanged();

                priceTxt.setText(CompanyHelper.getFormattedPrice(total));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}
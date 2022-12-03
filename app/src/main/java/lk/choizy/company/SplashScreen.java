package lk.choizy.company;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import lk.choizy.company.Company.CompanyHelper;
import lk.choizy.company.Company.MainActivity;
import lk.choizy.company.CompanyBraunch.CompanyBranchDash;

public class SplashScreen extends AppCompatActivity {


    CompanyHelper helper ;
    network_change_listner networkChangeListener = new network_change_listner();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        helper = new CompanyHelper(getApplicationContext());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(helper.getBranchId() != null || helper.getCompanyId() != null){
                    if(helper.getBranchId() != null){
                        helper.getUser(helper.getBranchId()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                helper.getUser(helper.getBranchId()).removeEventListener(this);
                                if(dataSnapshot.exists()){

                                    Intent intent = new Intent(getApplicationContext(), CompanyBranchDash.class);
                                    startActivity(intent);
                                    finish();
                                }else {
                                    loginPage();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }else{
                        helper.getUser(helper.getCompanyId()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                helper.getUser(helper.getCompanyId()).removeEventListener(this);
                                if(dataSnapshot.exists()){
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    loginPage();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }

                }else{
                    loginPage();
                }


            }
        },4100);

    }

    private void loginPage(){
        Intent intent = new Intent(getApplicationContext(),loginPanel.class);
        startActivity(intent);
        finish();
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
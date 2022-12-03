package lk.choizy.company;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import lk.choizy.company.Company.Branch;
import lk.choizy.company.Company.Company;
import lk.choizy.company.Company.MainActivity;
import lk.choizy.company.CompanyBraunch.CompanyBranchDash;

public class loginPanel extends AppCompatActivity {

    TextInputLayout email,password;
    Branch branch = null;
    Company company = null;
    private final String CASharePreference = "Choizy_SharedPreference";
    network_change_listner networkChangeListener = new network_change_listner();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_panel);
        email = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);
    }

    public void login(View view){
        if (email.getEditText().getText().toString().trim().isEmpty()||password.getEditText().getText().toString().isEmpty()) {
            if(email.getEditText().getText().toString().trim().isEmpty()){
                email.setError(getString(R.string.email_blank_valid));
                email.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        email.setError(null);
                        email.requestFocus();
                    }
                },2000);

            }

            if(password.getEditText().getText().toString().isEmpty()){
                password.setError(getString(R.string.pass_blank_valid));

                password.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        password.setError(null);
                        password.requestFocus();
                    }
                },2000);


            }
            return;
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        myRef.child(Users.class.getSimpleName()).orderByChild("email").equalTo(email.getEditText().getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                myRef.removeEventListener(this);
                if(dataSnapshot.exists()){
                    System.out.println(dataSnapshot);

                    Users login = null;
                    for (DataSnapshot one: dataSnapshot.getChildren()) {
                        login = one.getValue(Users.class);
                        login.setUid(one.getKey());

                        break;
                    }
                    if(!login.getPassword().equals(password.getEditText().getText().toString())){
                        password.setError("invalid email or password");
                        password.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                password.setError(null);
                                password.requestFocus();
                            }
                        },4000);
                        return;
                    }


                    if(login.getRole().equals(Branch.class.getSimpleName())){
                        myRef.child(Branch.class.getSimpleName()).child(login.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {

                                myRef.removeEventListener(this);
                                branch = dataSnapshot.getValue(Branch.class);
                                branch.setID(dataSnapshot.getKey());
                                setBranchIdPreference();
                                Intent intent = new Intent(getApplicationContext(), CompanyBranchDash.class);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

                            }
                        });

                    }else{
                        myRef.child(Company.class.getSimpleName()).child(login.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                                myRef.removeEventListener(this);
                                company = dataSnapshot.getValue(Company.class);
                                if (company == null) {
                                    return;

                                }
                                company.setKey(dataSnapshot.getKey());
                                setCompanyIdPreference();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

                            }
                        });

                    }

                    // DataSnapshot pass = dataSnapshot.child("password");
                //   String rPass = dataSnapshot.child("password").getValue(String.class);
//                    if(branch.getPassword().equals(password.getText().toString())){
//                        setBranchIdPreference();
//                        Intent intent = new Intent(getApplicationContext(), CompanyBranchDash.class);
//                        startActivity(intent);
//                    }
                }else {
                    email.setError("couldn't find the user");
                    email.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            email.setError(null);
                            email.requestFocus();
                        }
                    },4000);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

            }
        });

    }

    private void setBranchIdPreference() {
        SharedPreferences userData = getSharedPreferences(CASharePreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userData.edit();

       // LoginDetails details = db.customerDetials(emailAddresss.getText().toString(),password.getText().toString());

        editor.putString("BranchID",branch.getID());


        editor.apply();
    }

    private void setCompanyIdPreference() {
        SharedPreferences userData = getSharedPreferences(CASharePreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userData.edit();

        // LoginDetails details = db.customerDetials(emailAddresss.getText().toString(),password.getText().toString());

        editor.putString("CompanyID",company.getKey());


        editor.apply();
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
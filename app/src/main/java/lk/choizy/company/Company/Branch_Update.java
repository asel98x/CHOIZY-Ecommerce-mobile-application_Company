package lk.choizy.company.Company;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.PopupMenu;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import lk.choizy.company.PasswordChangeDialog;
import lk.choizy.company.R;
import lk.choizy.company.Users;
import lk.choizy.company.network_change_listner;

public class Branch_Update extends AppCompatActivity implements PasswordChangeDialog.onPasswordChange, PopupMenu.OnMenuItemClickListener {

    TextInputLayout branchNameTIL,branchMobileTIL,branchEmailTIL,branchSt_NoTIL,branchStreetTIL,branchCityTIL,branchLatitudeTIL,branchLongitudeTIL;
    CheckBox haveDeliveryCB,haveAdvanceOrder;
    String branchID;
    Branch branchDetails;
    FirebaseDatabase database ;
    DatabaseReference myRef,myRef2;
    network_change_listner networkChangeListener = new network_change_listner();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_update);
        branchNameTIL = findViewById(R.id.updateBNameTIL);
        branchMobileTIL = findViewById(R.id.updateBMobileTIL);
        branchEmailTIL = findViewById(R.id.updateBEmailTIL);
        branchSt_NoTIL = findViewById(R.id.updateBSt_NoTIL);
        branchStreetTIL = findViewById(R.id.updateBStreetTIL);
        branchCityTIL = findViewById(R.id.updateBCityTIL);
        branchLatitudeTIL = findViewById(R.id.updateBLatitudeTIL);
        branchLongitudeTIL = findViewById(R.id.updateBLongitudeTIL);
        haveDeliveryCB = findViewById(R.id.updateBHaveDeliverCB);
        haveAdvanceOrder = findViewById(R.id.updateBHaveAdvanceOrderCB);
//        optionBtn = findViewById(R.id.updateB_optionBtn);
//        backBtn = findViewById(R.id.updateB_backBtn);


        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(Branch.class.getSimpleName());
        myRef2 = database.getReference();


        Intent intent = getIntent();
        branchID = intent.getStringExtra("BranchID");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Branch.class.getSimpleName());

        myRef.child(branchID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {

                Branch branch = dataSnapshot.getValue(Branch.class);
                if (branch != null) {
                    branchDetails = branch;
                    setBranchDetails();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

            }
        });

    }

    private void setBranchDetails() {
        branchNameTIL.getEditText().setText(branchDetails.getName());
        branchMobileTIL.getEditText().setText(branchDetails.getMobile());
        branchEmailTIL.getEditText().setText(branchDetails.getEmail());
        branchSt_NoTIL.getEditText().setText(branchDetails.getNo_adres());
        branchStreetTIL.getEditText().setText(branchDetails.getStreetAddress());
        branchCityTIL.getEditText().setText(branchDetails.getCity());
        branchLatitudeTIL.getEditText().setText(""+branchDetails.getLatitude());
        branchLongitudeTIL.getEditText().setText(""+branchDetails.getLongitude());
        haveDeliveryCB.setChecked(branchDetails.isHaveDelivering());
        haveAdvanceOrder.setChecked(branchDetails.isHaveAdvance());


    }

    public void optionMenuBtn(View view){
        PopupMenu popupMenu = new PopupMenu(getApplicationContext(),view);
        popupMenu.inflate(R.menu.branch_context_menu);
        popupMenu.setOnMenuItemClickListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popupMenu.setForceShowIcon(true);
        }
        popupMenu.show();
    }


    public void backBtn(View view){
        finish();
    }

    public void UpdateBranch(View view){

        if(!validating()){
            return;
        }

        if(changeData() || !branchEmailTIL.getEditText().getText().toString().equals(branchDetails.getEmail())){
            if(!branchEmailTIL.getEditText().getText().toString().equals(branchDetails.getEmail())){
                checkEmailIsUsed(branchEmailTIL.getEditText().getText().toString().toLowerCase());
            }else {
                UpdateFirebase();
            }

        }else {
            Snackbar.make(view,"Nothing change",Snackbar.LENGTH_SHORT).show();
        }

    }

    private void UpdateFirebase(){
        myRef.child(branchID).setValue(branchDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                Snackbar.make(findViewById(android.R.id.content),"Updated",Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validating(){
        boolean valid= true;


        if (branchSt_NoTIL.getEditText().getText().toString().trim().isEmpty()){
            branchSt_NoTIL.setError("No. can't be empty");
            branchSt_NoTIL.postDelayed(new Runnable() {
                @Override
                public void run() {
                    branchSt_NoTIL.setError(null);
                    branchSt_NoTIL.getEditText().setText(branchDetails.getNo_adres());
                    branchSt_NoTIL.requestFocus();
                }
            },3000);

            valid = false ;
        }

        if (branchStreetTIL.getEditText().getText().toString().trim().isEmpty()){
            branchStreetTIL.setError("Street address can't be empty");
            branchStreetTIL.postDelayed(new Runnable() {
                @Override
                public void run() {
                    branchStreetTIL.setError(null);
                    branchStreetTIL.getEditText().setText(branchDetails.getStreetAddress());
                    branchStreetTIL.requestFocus();
                }
            },3000);

            valid = false ;
        }

        if (branchCityTIL.getEditText().getText().toString().trim().isEmpty()){
            branchCityTIL.setError("City can't be empty");
            branchCityTIL.postDelayed(new Runnable() {
                @Override
                public void run() {
                    branchCityTIL.setError(null);
                    branchCityTIL.getEditText().setText(branchDetails.getCity());
                    branchCityTIL.requestFocus();
                }
            },3000);

            valid = false ;
        }

        if (branchLatitudeTIL.getEditText().getText().toString().trim().isEmpty()){
            branchLatitudeTIL.setError("latitude can't be empty");
            branchLatitudeTIL.postDelayed(new Runnable() {
                @Override
                public void run() {
                    branchLatitudeTIL.setError(null);
                    branchLatitudeTIL.getEditText().setText(branchDetails.getLatitude()+"");
                    branchLatitudeTIL.requestFocus();
                }
            },3000);

            valid = false ;
        }

        if (branchLongitudeTIL.getEditText().getText().toString().trim().isEmpty()){
            branchLongitudeTIL.setError("City can't be empty");
            branchLongitudeTIL.postDelayed(new Runnable() {
                @Override
                public void run() {
                    branchLongitudeTIL.setError(null);
                    branchLongitudeTIL.getEditText().setText(branchDetails.getLongitude()+"");
                    branchLongitudeTIL.requestFocus();
                }
            },3000);

            valid = false ;
        }

        if(branchMobileTIL.getEditText().getText().toString().trim().isEmpty()){
            branchMobileTIL.setError("Mobile number can't be empty");
            branchMobileTIL.postDelayed(new Runnable() {
                @Override
                public void run() {
                    branchMobileTIL.setError(null);
                    branchMobileTIL.getEditText().setText(branchDetails.getMobile());
                    branchMobileTIL.requestFocus();
                }
            },3000);



            valid = false ;
        }else if(branchMobileTIL.getEditText().getText().toString().trim().length()<10){
            branchMobileTIL.setError("Mobile number should have 10 digits");
            branchMobileTIL.postDelayed(new Runnable() {
                @Override
                public void run() {
                    branchMobileTIL.setError(null);
                    branchMobileTIL.getEditText().setText(branchDetails.getMobile());
                    branchMobileTIL.requestFocus();
                }
            },3000);



            valid = false ;
        }

        if (branchEmailTIL.getEditText().getText().toString().trim().isEmpty()){
            branchEmailTIL.setError("Email can't be empty");
            branchEmailTIL.postDelayed(new Runnable() {
                @Override
                public void run() {
                    branchEmailTIL.setError(null);
                    branchEmailTIL.getEditText().setText(branchDetails.getEmail());
                    branchEmailTIL.requestFocus();
                }
            },3000);

            valid = false ;
        }

        if (branchNameTIL.getEditText().getText().toString().trim().isEmpty()){
            branchNameTIL.setError("Name can't be empty");
            branchNameTIL.postDelayed(new Runnable() {
                @Override
                public void run() {
                    branchNameTIL.setError(null);
                    branchNameTIL.getEditText().setText(branchDetails.getName());
                    branchNameTIL.requestFocus();
                }
            },3000);

            valid = false ;
        }


        return valid;
    }

    private boolean changeData(){

        boolean update = false;

        if(!branchNameTIL.getEditText().getText().toString().equals(branchDetails.getName())){
            branchDetails.setName(branchNameTIL.getEditText().getText().toString());
            update =true;

        }

        if(!branchMobileTIL.getEditText().getText().toString().equals(branchDetails.getMobile())){
            if(branchMobileTIL.getEditText().getText().toString().trim().length()<10){
                branchMobileTIL.setError("Mobile should at least contain 10 digits");
                branchMobileTIL.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        branchMobileTIL.setError(null);
                        branchMobileTIL.requestFocus();
                    }
                },5000);

                return false;
            }

            branchDetails.setMobile(branchMobileTIL.getEditText().getText().toString());
            update =true;

        }

//        if (!branchEmailTIL.getEditText().getText().toString().equals(branchDetails.getEmail())){
//            checkEmailIsUsed(branchEmailTIL.getEditText().getText().toString());
//        }

        if (!branchSt_NoTIL.getEditText().getText().toString().equals(branchDetails.getNo_adres())){
            branchDetails.setNo_adres(branchSt_NoTIL.getEditText().getText().toString());
            update =true;

        }

        if(!branchStreetTIL.getEditText().getText().toString().equals(branchDetails.getStreetAddress())){
            branchDetails.setStreetAddress(branchStreetTIL.getEditText().getText().toString());
            update =true;

        }

        if(!branchCityTIL.getEditText().getText().toString().equals(branchDetails.getCity())){
            branchDetails.setCity(branchCityTIL.getEditText().getText().toString());
            update =true;

        }

        if(!branchLatitudeTIL.getEditText().getText().toString().equals(""+branchDetails.getLatitude())){
            branchDetails.setLatitude(Double.parseDouble(branchLatitudeTIL.getEditText().getText().toString()));
            update =true;

        }

        if(!branchLongitudeTIL.getEditText().getText().toString().equals(""+branchDetails.getLongitude())){
            branchDetails.setLongitude(Double.parseDouble(branchLongitudeTIL.getEditText().getText().toString()));
            update =true;

        }

        if(!haveDeliveryCB.isChecked() == branchDetails.isHaveDelivering()){
            branchDetails.setHaveDelivering(haveDeliveryCB.isChecked());
            update =true;
        }

        if(!haveAdvanceOrder.isChecked() == branchDetails.isHaveAdvance()){
            branchDetails.setHaveDelivering(haveAdvanceOrder.isChecked());
            update =true;
        }


        return update;
    }

    private void checkEmailIsUsed(String email){
        DatabaseReference myRef2 = database.getReference(Users.class.getSimpleName());
        myRef2.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Snackbar.make(findViewById(android.R.id.content),"Email is Already on use",Snackbar.LENGTH_SHORT).show();
                }else{
                    Users users = new Users (email,branchDetails.getPassword(),"Branch");
                    branchDetails.setEmail(email);
                    myRef2.child(branchID).setValue(users);
                    UpdateFirebase();
                }

                myRef2.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void NewPassword(String pass) {
        branchDetails.setPassword(pass);
        UpdateFirebase();
    }


    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.MenuUpdateB_ChangePass:
                PasswordChangeDialog dialog = new PasswordChangeDialog();
                dialog.setListener(this);
                //dialog.onActivityResult();
                dialog.show(getSupportFragmentManager(),"Branch_ChangePassword");
                return true;
            case R.id.MenuUpdateB_Delete:
                myRef.child(branchID).removeValue();
                myRef2.child(Users.class.getSimpleName()).child(branchID).removeValue();
                finish();
                return true;
            default:
                return false;

        }
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
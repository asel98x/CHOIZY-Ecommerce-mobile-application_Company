package lk.choizy.company.Company;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import lk.choizy.company.R;
import lk.choizy.company.Users;
import lk.choizy.company.network_change_listner;

public class Branch_Create extends AppCompatActivity implements location_picker.locationSelected {

    EditText branchName,branchMobile,branchEmail,branchStreetNo,branchStreet,branchCity,branchLatitude,branchLongitude,branchPassword,branchRePass;
    String bName,bEmail,bStreetNo,bStreet,bCity,bPassword,bRePass,bMobile;
    boolean isDeliver,isBooking;
    DatabaseReference myRef2,myRef;
    private ActivityResultLauncher<Intent> ARL;
    ImageView locationBtn;
    CheckBox haveDeliver,haveAdvanceBooking;
    private final String CASharePreference = "Choizy_SharedPreference";
    network_change_listner networkChangeListener = new network_change_listner();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_create);

        branchName = findViewById(R.id.newBranchNameTxt);
        branchMobile = findViewById(R.id.newBranchMobileTxt);
        branchEmail = findViewById(R.id.newBranchEmailTxt);
        branchStreetNo = findViewById(R.id.newBranchStNoTxt);
        branchStreet = findViewById(R.id.newBranchStTxt);
        branchCity = findViewById(R.id.newBranchCityTxt);
        branchLatitude = findViewById(R.id.newBranchLatitudeTxt);
        branchLongitude = findViewById(R.id.newBranchLongitudeTxt);
        branchPassword = findViewById(R.id.newBranchPassTxt);
        branchRePass =findViewById(R.id.newBranchRePassTxt);
        locationBtn = findViewById(R.id.newBranchLoc);
        haveDeliver = findViewById(R.id.newBranchHaveDelivery);
        haveAdvanceBooking = findViewById(R.id.newBranchHaveBooking);


        System.out.println(getCompanyId());

        ARL = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK){
                            Intent intent = result.getData();
                            if(intent != null){


                            }
                        }
                    }
                });

//        branchName = findViewById(R.id.newBranchNameTxt);
//        mAuth = FirebaseAuth.getInstance();

    }
    public void getLocation(View view){
        location_picker dialog = new location_picker();
        dialog.show(getSupportFragmentManager(),"locPicker");
        dialog.setListener(this);


    }


    public void createBranch(View view){


        bName = branchName.getText().toString();
        bEmail = branchEmail.getText().toString();
        bStreetNo = branchStreetNo.getText().toString();
        bStreet = branchStreet.getText().toString();
        bCity = branchCity.getText().toString();
        bPassword = branchPassword.getText().toString();
        bRePass = branchRePass.getText().toString();
        bMobile = branchMobile.getText().toString();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference(Branch.class.getSimpleName());
        myRef2 = database.getReference();
        isBooking = haveAdvanceBooking.isChecked();
        isDeliver = haveDeliver.isChecked();



        myRef2.child(Users.class.getSimpleName()).orderByChild("email").equalTo(bEmail.toLowerCase()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                myRef2.removeEventListener(this);

                if(dataSnapshot.exists()){
                    Snackbar.make(view,"This Email is already taken",Snackbar.LENGTH_SHORT).show();
                    return;
                }else {
                    insertBranch(view);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

            }
        });

    }

    private void insertBranch(View view){
        if(!bName.isEmpty()&&bMobile.trim().length()>9&&!bMobile.isEmpty()&&!bEmail.isEmpty()&&!bStreetNo.isEmpty()&&!bStreet.isEmpty()&&!bCity.isEmpty()&&!bPassword.isEmpty()&&bPassword.equals(bRePass)){

            Users users = new Users(bEmail.toLowerCase(),bRePass,Branch.class.getSimpleName());
            Branch branch = new Branch(bName,bEmail.toLowerCase(),bStreetNo,bStreet,bCity);
            branch.setMobile(branchMobile.getText().toString());
            branch.setMobile(bMobile);
            branch.setLatitude(Double.parseDouble(branchLatitude.getText().toString()));
            branch.setLongitude(Double.parseDouble(branchLongitude.getText().toString()));
            branch.setPassword(bRePass);
            branch.setCompID(getCompanyId());
            branch.setHaveAdvance(isDeliver);
            branch.setHaveAdvance(isBooking);

            String uID = myRef2.child(Users.class.getSimpleName()).push().getKey();


            myRef2.child(Users.class.getSimpleName()).child(uID).setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {

                    myRef.child(uID).setValue(branch).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            Snackbar.make(view,"Inserted",Snackbar.LENGTH_SHORT).show();
                        }
                    });
                }
            });


//                    }else{
//                        Snackbar.make(view,"",Snackbar.LENGTH_SHORT).show();
//                    }

        }else{
            if(bPassword.trim().isEmpty()){
                branchPassword.setError("Password can't be Empty");
                branchPassword.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        branchPassword.setError(null);
                        branchPassword.requestFocus();
                    }
                },5000);
            }
            if(bPassword.trim().equals(bRePass)){
                branchPassword.setError("Password dose not match");
                branchRePass.setError("Password dose not match");
                branchRePass.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        branchPassword.setError(null);
                        branchRePass.setError(null);
                        branchPassword.setText("");
                        branchPassword.requestFocus();
                        branchRePass.setText("");
                        branchRePass.requestFocus();
                    }
                },5000);
            }

            if(bCity.trim().isEmpty()){
                branchCity.setError("Please Enter Branch City");
                branchCity.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        branchCity.setError(null);
                        branchCity.requestFocus();
                    }
                },5000);
            }

            if(bStreet.trim().isEmpty()){
                branchStreet.setError("Please Enter Branch Street Address");
                branchStreet.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        branchStreet.setError(null);
                        branchStreet.requestFocus();
                    }
                },5000);
            }

            if(bStreetNo.trim().isEmpty()){
                branchStreetNo.setError("Please Enter Street No");
                branchStreetNo.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        branchStreetNo.setError(null);
                        branchStreetNo.requestFocus();
                    }
                },5000);
            }

            if(bEmail.trim().isEmpty()){
                branchEmail.setError("Please Enter Email");
                branchEmail.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        branchEmail.setError(null);
                        branchEmail.requestFocus();
                    }
                },5000);
            }

            if(bMobile.trim().isEmpty()){
                branchMobile.setError("Please Enter Mobile");
                branchMobile.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        branchMobile.setError(null);
                        branchMobile.requestFocus();
                    }
                },5000);
            }

            if(bMobile.trim().trim().length()<10){
                branchMobile.setError("Mobile should at least contain 10 digits");
                branchMobile.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        branchMobile.setError(null);
                        branchMobile.requestFocus();
                    }
                },5000);
            }

            if(bName.trim().isEmpty()){
                branchName.setError("Please Enter Branch Name");
                branchName.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        branchName.setError(null);
                        branchName.requestFocus();
                    }
                },5000);
            }

        }
    }

    private String getCompanyId(){
        SharedPreferences preferences = getSharedPreferences(CASharePreference, MODE_PRIVATE);

        return preferences.getString("CompanyID",null);
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

    @Override
    public void locationSelected(LatLng latLng) {
        branchLatitude.setText(latLng.latitude+"");
        branchLongitude.setText(latLng.longitude+"");
        Geocoder geocoder = new Geocoder(Branch_Create.this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert addresses != null;
        branchStreet.setText(addresses.get(0).getThoroughfare());
        branchCity.setText(addresses.get(0).getLocality());
        branchStreetNo.setText(addresses.get(0).getSubThoroughfare());

    }
}
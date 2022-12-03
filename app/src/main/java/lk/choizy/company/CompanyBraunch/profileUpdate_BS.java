package lk.choizy.company.CompanyBraunch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import lk.choizy.company.Company.Branch;
import lk.choizy.company.R;
import lk.choizy.company.Users;

public class profileUpdate_BS extends BottomSheetDialogFragment {
    Branch branch;
    TextInputLayout name,mobile,emailTIL,no_adrs,streetAddress,city,longitude,latitude;
    MaterialButton updateBtn;
    FirebaseDatabase database ;
    DatabaseReference myRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_update,container,false);
        name = view.findViewById(R.id.profileUpdate_name);
        mobile = view.findViewById(R.id.profileUpdate_Mobile);
        emailTIL = view.findViewById(R.id.profileUpdate_Email);
        no_adrs = view.findViewById(R.id.profileUpdate_No_adres);
        streetAddress = view.findViewById(R.id.profileUpdate_StreetAddress);
        city = view.findViewById(R.id.profileUpdate_city);
        longitude = view.findViewById(R.id.profileUpdate_longitude);
        latitude = view.findViewById(R.id.profileUpdate_latitude);
        updateBtn = view.findViewById(R.id.profileUpdate_updateBtn);

        name.getEditText().setText(branch.getName());
        mobile.getEditText().setText(branch.getMobile());
        emailTIL.getEditText().setText(branch.getEmail());
        no_adrs.getEditText().setText(branch.getNo_adres());
        streetAddress.getEditText().setText(branch.getStreetAddress());
        city.getEditText().setText(branch.getCity());
        longitude.getEditText().setText(""+branch.getLongitude());
        latitude.getEditText().setText(""+branch.getLatitude());

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(Branch.class.getSimpleName());


        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!validating()){
                    return;
                }

                if(changeData() || !emailTIL.getEditText().getText().toString().equals(branch.getEmail())){
                    if(!emailTIL.getEditText().getText().toString().equals(branch.getEmail())){
                        checkEmailIsUsed(emailTIL.getEditText().getText().toString());
                    }else {
                        UpdateFirebase();
                    }

                }else {
                    Snackbar.make(getActivity().findViewById(android.R.id.content),"Nothing change",Snackbar.LENGTH_SHORT).show();
                    dismiss();
                }
            }
        });

        return view;
    }

    private void UpdateFirebase(){
        myRef.child(branch.getID()).setValue(branch).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                Snackbar.make(getActivity().findViewById(android.R.id.content),"Updated",Snackbar.LENGTH_SHORT).show();
                dismiss();
            }
        });
    }

    public profileUpdate_BS(Branch branch) {
        this.branch = branch;
    }

    private boolean validating(){
        boolean valid= true;


        if (no_adrs.getEditText().getText().toString().trim().isEmpty()){
            no_adrs.setError("No. can't be empty");
            no_adrs.postDelayed(new Runnable() {
                @Override
                public void run() {
                    no_adrs.setError(null);
                    no_adrs.getEditText().setText(branch.getNo_adres());
                    no_adrs.requestFocus();
                }
            },3000);

            valid = false ;
        }

        if (streetAddress.getEditText().getText().toString().trim().isEmpty()){
            streetAddress.setError("Street address can't be empty");
            streetAddress.postDelayed(new Runnable() {
                @Override
                public void run() {
                    streetAddress.setError(null);
                    streetAddress.getEditText().setText(branch.getStreetAddress());
                    streetAddress.requestFocus();
                }
            },3000);

            valid = false ;
        }

        if (city.getEditText().getText().toString().trim().isEmpty()){
            city.setError("City can't be empty");
            city.postDelayed(new Runnable() {
                @Override
                public void run() {
                    city.setError(null);
                    city.getEditText().setText(branch.getCity());
                    city.requestFocus();
                }
            },3000);

            valid = false ;
        }

        if (latitude.getEditText().getText().toString().trim().isEmpty()){
            latitude.setError("latitude can't be empty");
            latitude.postDelayed(new Runnable() {
                @Override
                public void run() {
                    latitude.setError(null);
                    latitude.getEditText().setText(branch.getLatitude()+"");
                    latitude.requestFocus();
                }
            },3000);

            valid = false ;
        }

        if (longitude.getEditText().getText().toString().trim().isEmpty()){
            longitude.setError("City can't be empty");
            longitude.postDelayed(new Runnable() {
                @Override
                public void run() {
                    longitude.setError(null);
                    longitude.getEditText().setText(branch.getLongitude()+"");
                    longitude.requestFocus();
                }
            },3000);

            valid = false ;
        }

        if(mobile.getEditText().getText().toString().trim().isEmpty()){
            mobile.setError("Mobile number can't be empty");
            mobile.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mobile.setError(null);
                    mobile.getEditText().setText(branch.getMobile());
                    mobile.requestFocus();
                }
            },3000);



            valid = false ;
        }else if(mobile.getEditText().getText().toString().trim().length()<10){
            mobile.setError("Mobile number should have 10 digits");
            mobile.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mobile.setError(null);
                    //mobile.getEditText().setText(branch.getMobile());
                    mobile.requestFocus();
                }
            },3000);



            valid = false ;
        }

        if (emailTIL.getEditText().getText().toString().trim().isEmpty()){
            emailTIL.setError("Email can't be empty");
            emailTIL.postDelayed(new Runnable() {
                @Override
                public void run() {
                    emailTIL.setError(null);
                    emailTIL.getEditText().setText(branch.getEmail());
                    emailTIL.requestFocus();
                }
            },3000);

            valid = false ;
        }

        if (name.getEditText().getText().toString().trim().isEmpty()){
            name.setError("Name can't be empty");
            name.postDelayed(new Runnable() {
                @Override
                public void run() {
                    name.setError(null);
                    name.getEditText().setText(branch.getName());
                    name.requestFocus();
                }
            },3000);

            valid = false ;
        }


        return valid;
    }

    private boolean changeData(){

        boolean update = false;

        if(!name.getEditText().getText().toString().equals(branch.getName())){
            branch.setName(name.getEditText().getText().toString());
            update =true;

        }

        if(!mobile.getEditText().getText().toString().equals(branch.getMobile())){
            branch.setMobile(mobile.getEditText().getText().toString());
            update =true;

        }

//        if (!branchEmailTIL.getEditText().getText().toString().equals(branchDetails.getEmail())){
//            checkEmailIsUsed(branchEmailTIL.getEditText().getText().toString());
//        }

        if (!no_adrs.getEditText().getText().toString().equals(branch.getNo_adres())){
            branch.setNo_adres(no_adrs.getEditText().getText().toString());
            update =true;

        }

        if(!streetAddress.getEditText().getText().toString().equals(branch.getStreetAddress())){
            branch.setStreetAddress(streetAddress.getEditText().getText().toString());
            update =true;

        }

        if(!city.getEditText().getText().toString().equals(branch.getCity())){
            branch.setCity(city.getEditText().getText().toString());
            update =true;

        }

        if(!latitude.getEditText().getText().toString().equals(""+branch.getLatitude())){
            branch.setLatitude(Double.parseDouble(latitude.getEditText().getText().toString()));
            update =true;

        }

        if(!longitude.getEditText().getText().toString().equals(""+branch.getLongitude())){
            branch.setLongitude(Double.parseDouble(longitude.getEditText().getText().toString()));
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
                    //Snackbar.make(getActivity().findViewById(android.R.id.content),"Email is Already on use",Snackbar.LENGTH_SHORT).show();
                    emailTIL.setError("Email is Already on use");
                    emailTIL.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            emailTIL.setError(null);
                            emailTIL.getEditText().setText(branch.getEmail());
                        }
                    },5000);
                }else{
                    Users users = new Users (email,branch.getPassword(),"Branch");
                    branch.setEmail(email);
                    myRef2.child(branch.getID()).setValue(users);
                    UpdateFirebase();
                }

                myRef2.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

            }
        });
    }
}

package lk.choizy.company.Company;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import lk.choizy.company.R;
import lk.choizy.company.Users;

public class CompanyProfileUpdate_BS extends BottomSheetDialogFragment {

    View view;
    ShapeableImageView companyIcon;
    TextInputLayout companyEmail,companyMobile,companyName;
    TextView compCategory;
    MaterialButton updateBtn;
    ImageButton changeImgBtn;
    Company company;
    FirebaseDatabase database;
    DatabaseReference myRef;
    private ActivityResultLauncher<Intent> ARL;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.company_profile_update_bs,container,false);
        companyName = view.findViewById(R.id.compProfileUpdate_Name);
        companyEmail = view.findViewById(R.id.compProfileUpdate_Email);
        companyMobile = view.findViewById(R.id.compProfileUpdate_Mobile);
        compCategory = view.findViewById(R.id.compProfileUpdate_CategoryTxt);
        companyIcon = view.findViewById(R.id.compProfileUpdate_Icon);
        updateBtn = view.findViewById(R.id.compProfileUpdate_UpdateBtn);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        changeImgBtn = view.findViewById(R.id.compProfileUpdate_UpdateImg);

        if(company!= null){
            Picasso.get().load(company.getImageURL()).into(companyIcon);
            companyName.getEditText().setText(company.getCompany_name());
            companyMobile.getEditText().setText(company.getCompany_mobile());
            companyEmail.getEditText().setText(company.getCompany_email());
            compCategory.setText(company.getCompany_category());
        }

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!validting()){
                    return;
                }

                if(IsChange()||!company.getCompany_email().equals(companyEmail.getEditText().getText().toString())){
                    if(!company.getCompany_email().equals(companyEmail.getEditText().getText().toString())){
                        checkEmailIsUsed(companyEmail.getEditText().getText().toString());
                    }else{
                        UpdateFirebase();
                    }
                }
            }
        });

        changeImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                ARL.launch(intent);
            }
        });

        ARL = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK){
                            Intent intent = result.getData();
                            if(intent != null){
                                try {
                                    Bitmap ImgBit = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), intent.getData());
                                    companyIcon.setImageBitmap(ImgBit);
                                    uploadCompanyImg(intent.getData());
                                } catch (IOException e) {
                                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                                }

                            }
                        }
                    }
                });

        return view;
    }

    private boolean validting(){
        boolean valid = true;

        if(companyMobile.getEditText().getText().toString().trim().isEmpty()){
            companyMobile.setError("Mobile number can't be empty");
            companyMobile.postDelayed(new Runnable() {
                @Override
                public void run() {
                    companyMobile.setError(null);
                    companyMobile.requestFocus();
                    companyMobile.getEditText().setText(company.getCompany_mobile());
                }
            },5000);

            valid= false;
        }else if(companyMobile.getEditText().getText().toString().trim().length()<10){
            companyMobile.setError("Mobile number should contain 10 digits");
            companyMobile.postDelayed(new Runnable() {
                @Override
                public void run() {
                    companyMobile.setError(null);
                    companyMobile.requestFocus();

                }
            },5000);

            valid= false;
        }

        if(companyName.getEditText().getText().toString().trim().isEmpty()){
            companyName.setError("Company name can't be empty");
            companyName.postDelayed(new Runnable() {
                @Override
                public void run() {
                    companyName.setError(null);
                    companyName.requestFocus();
                    companyName.getEditText().setText(company.getCompany_name());
                }
            },5000);

            valid= false;
        }

        if(companyEmail.getEditText().getText().toString().trim().isEmpty()){
            companyEmail.setError("Email address can't be empty");
            companyEmail.postDelayed(new Runnable() {
                @Override
                public void run() {
                    companyEmail.setError(null);
                    companyEmail.requestFocus();
                    companyEmail.getEditText().setText(company.getCompany_email());
                }
            },5000);

            valid= false;
        }
        return valid;
    }


    private void uploadCompanyImg(Uri uri){
        String url = company.getImageURL();
        StorageReference storage = FirebaseStorage.getInstance().getReference(Offer.class.getSimpleName()).child(System.currentTimeMillis()
                + "." + getFileExtension(uri));

        storage.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        company.setImageURL(task.getResult().toString());
                        FirebaseStorage.getInstance().getReferenceFromUrl(url).delete();
                        UpdateFirebase();
                    }
                });
            }
        });
    }


    private void checkEmailIsUsed(String email){
        myRef.child(Users.class.getSimpleName()).orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                myRef.child(Users.class.getSimpleName()).orderByChild("email").equalTo(email).removeEventListener(this);
                if(dataSnapshot.exists()){
//                    Snackbar.make(view.findViewById(android.R.id.content),"Email is Already on use",Snackbar.LENGTH_SHORT).show();
                    Toast.makeText(getContext(),"Email is Already on use",Toast.LENGTH_SHORT).show();
                    dismiss();
                }else{
                    Users users = new Users (email,company.getCompany_password(),Company.class.getSimpleName());
                    company.setCompany_email(email);
                    myRef.child(Users.class.getSimpleName()).child(company.getKey()).setValue(users);
                    UpdateFirebase();
                }

                myRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

            }
        });
    }

    private void UpdateFirebase() {

        myRef.child(Company.class.getSimpleName()).child(company.getKey()).setValue(company).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
//                Snackbar.make(getDialog().getCurrentFocus(),"Updated",Snackbar.LENGTH_SHORT).show();
                Toast.makeText(getContext(),"Updated",Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    }

    public boolean IsChange(){
        boolean change = false;
        if(!company.getCompany_name().equals(companyName.getEditText().getText().toString())){
            company.setCompany_name(companyName.getEditText().getText().toString());
            change = true;
        }
        if(!company.getCompany_mobile().equals(companyMobile.getEditText().getText().toString())){
            company.setCompany_mobile(companyMobile.getEditText().getText().toString());
            change = true;
        }

        return change;
    }

    private String getFileExtension(Uri uri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(requireActivity().getContentResolver().getType(uri));
    }

    public CompanyProfileUpdate_BS(Company company) {
        this.company = company;
    }
}

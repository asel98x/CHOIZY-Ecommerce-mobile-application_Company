package lk.choizy.company.Company;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

import lk.choizy.company.R;

public class OfferAdd extends AppCompatActivity {

    private ImageView offerImg;
    private TextInputLayout offerTitle,offerDescription,offerPrice;
    private ActivityResultLauncher<Intent> ARL;
    private Uri ImgUri;
    private final String CASharePreference = "Choizy_SharedPreference";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_add);

        offerImg = findViewById(R.id.OfferImg);
        offerTitle = findViewById(R.id.OfferTitleTIL);
        offerDescription = findViewById(R.id.OfferDescriptionTIL);
        offerPrice = findViewById(R.id.OfferPriceTIL);

        ARL = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK){
                            Intent intent = result.getData();
                            if(intent != null){
                                try {
                                    Bitmap ImgBit = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), intent.getData());
                                    offerImg.setImageBitmap(ImgBit);
                                    ImgUri = intent.getData();
                                } catch (IOException e) {
                                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                                }

                            }
                        }
                    }
                });


    }

    public void ImgChooser(View view){
        Intent img = new Intent();
        img.setType("image/*");
        img.setAction(Intent.ACTION_GET_CONTENT);
        ARL.launch(img);
    }

    private String getFileExtension(Uri uri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(getContentResolver().getType(uri));
    }

    public void InsertOffer(View view){

        if(ImgUri!=null||!offerTitle.getEditText().getText().toString().trim().isEmpty()||!offerDescription.getEditText().getText().toString().trim().isEmpty()||!offerPrice.getEditText().getText().toString().trim().isEmpty()){
            ArrayList<Branch> list = new ArrayList<>();

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(Branch.class.getSimpleName());
            DatabaseReference myRef2 = database.getReference(Offer.class.getSimpleName());

            StorageReference storage = FirebaseStorage.getInstance().getReference(Offer.class.getSimpleName()).child(System.currentTimeMillis()
                    + "." + getFileExtension(ImgUri));


            myRef.orderByChild("compID").equalTo(getCompanyId()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {

                    for(DataSnapshot one:dataSnapshot.getChildren()){
                        Branch branch = one.getValue(Branch.class);
                        branch.setID(one.getKey());
                        list.add(branch);
                    }

                    if(list.size()>0){
                        storage.putFile(ImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<Uri> task) {
                                        for (int i = 0;i<list.size();i++){
                                            Offer offer = new Offer(list.get(i).getID(),offerTitle.getEditText().getText().toString(),offerDescription.getEditText().getText().toString(),task.getResult().toString(),Double.parseDouble(offerPrice.getEditText().getText().toString()));
                                            myRef2.push().setValue(offer);
                                        }
                                        Snackbar.make(view,"Offer Inserted to current Branchs",Snackbar.LENGTH_SHORT).show();
                                        finish();

                                    }

                                });


                            }
                        });

                    }

                    myRef.removeEventListener(this);
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

                }
            });
        }else {
            Toast.makeText(getApplicationContext(),"Fill the Fields",Toast.LENGTH_SHORT).show();
        }

    }
    private String getCompanyId(){
        SharedPreferences preferences = getSharedPreferences(CASharePreference, MODE_PRIVATE);

        return preferences.getString("CompanyID",null);
    }

}
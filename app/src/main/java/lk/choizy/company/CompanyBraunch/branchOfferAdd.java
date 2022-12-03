package lk.choizy.company.CompanyBraunch;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import lk.choizy.company.Company.Offer;
import lk.choizy.company.R;

public class branchOfferAdd extends AppCompatActivity {
    private ImageView offerImg;
    private TextInputLayout offerTitle,offerDescription,offerPrice;
    private ActivityResultLauncher<Intent> ARL;
    private Button imageChooserBTN;
    private Uri ImgUri;
    TextView allBranch;
    private final String CASharePreference = "Choizy_SharedPreference";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_add);

        offerImg = findViewById(R.id.OfferImg);
        allBranch = findViewById(R.id.offer_AllBranch);
        offerTitle = findViewById(R.id.OfferTitleTIL);
        offerDescription = findViewById(R.id.OfferDescriptionTIL);
        offerPrice = findViewById(R.id.OfferPriceTIL);
        imageChooserBTN = findViewById(R.id.button2);
        allBranch.setVisibility(View.INVISIBLE);
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

        if(ImgUri!=null&&!offerTitle.getEditText().getText().toString().trim().isEmpty()&&!offerDescription.getEditText().getText().toString().trim().isEmpty()&&!offerPrice.getEditText().getText().toString().trim().isEmpty()){


            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef2 = database.getReference(Offer.class.getSimpleName());

            StorageReference storage = FirebaseStorage.getInstance().getReference(Offer.class.getSimpleName()).child(System.currentTimeMillis()
                    + "." + getFileExtension(ImgUri));




            storage.putFile(ImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Uri> task) {
                            Offer offer = new Offer(getBranchId(),offerTitle.getEditText().getText().toString(),offerDescription.getEditText().getText().toString(),task.getResult().toString(),Double.parseDouble(offerPrice.getEditText().getText().toString()));
                            myRef2.push().setValue(offer);
                            Snackbar.make(view,"Offer Added",Snackbar.LENGTH_SHORT).show();
                            offerDescription.getEditText().setText("");
                            offerTitle.getEditText().setText("");
                            offerPrice.getEditText().setText("0.00");
                            offerImg.setImageResource(android.R.color.transparent);
                            ImgUri =null;
                            offerTitle.requestFocus();
                        }

                    });


                }
            });

        }else {
            if(ImgUri == null){
                imageChooserBTN.setError("Please select image");
                imageChooserBTN.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imageChooserBTN.setError(null);
                        imageChooserBTN.requestFocus();
                    }
                },3000);
            }
            Toast.makeText(getApplicationContext(),"Fill the Fields",Toast.LENGTH_SHORT).show();
        }

    }
    private String getBranchId(){
        SharedPreferences preferences =getSharedPreferences(CASharePreference, MODE_PRIVATE);

        return preferences.getString("BranchID",null);
    }
}
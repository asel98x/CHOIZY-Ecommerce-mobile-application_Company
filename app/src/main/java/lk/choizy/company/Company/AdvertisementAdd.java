package lk.choizy.company.Company;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import lk.choizy.company.R;

public class AdvertisementAdd extends AppCompatActivity {

    private ActivityResultLauncher<Intent> result;
    ImageView adBannerImg;
    DatabaseReference mDatabaseRef;
    private Uri ImgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisement_add);
        adBannerImg = findViewById(R.id.upcomingAdImg);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference(UpcomingAD.class.getSimpleName());

        result = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent intent = result.getData();
                        if(intent != null){
                            try {
                                Bitmap ImgBit = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), intent.getData());
                                adBannerImg.setImageBitmap(ImgBit);
                                ImgUri = intent.getData();
                            } catch (IOException e) {
                                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                            }

                        }
                    }
                }
        });
    }

    private String getFileExtension(Uri uri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(getContentResolver().getType(uri));
    }

    public void openImgChooser(View view){
        Intent img = new Intent();
        img.setType("image/*");
        img.setAction(Intent.ACTION_GET_CONTENT);
        result.launch(img);
    }

    public void InsertAd(View view){
        if (ImgUri != null) {
            StorageReference storage = FirebaseStorage.getInstance().getReference(UpcomingAD.class.getSimpleName()).child(System.currentTimeMillis()
                    + "." + getFileExtension(ImgUri));

            storage.putFile(ImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Uri> task) {
                            Toast.makeText(getApplicationContext(),"Inserted",Toast.LENGTH_SHORT).show();
                            UpcomingAD Ad = new UpcomingAD("02",task.getResult().toString());
                            mDatabaseRef.push().setValue(Ad);
                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
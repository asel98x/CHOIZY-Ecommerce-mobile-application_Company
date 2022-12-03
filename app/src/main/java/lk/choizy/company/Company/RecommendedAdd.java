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
import android.widget.EditText;
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

import lk.choizy.company.Company.RecommendedAD;
import lk.choizy.company.R;

public class RecommendedAdd extends AppCompatActivity {

    private ImageView recAdIMG;
    private EditText recommendNameTxt;
    private ActivityResultLauncher<Intent> result;
    private Uri ImgUri;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommended_add);
        recAdIMG = findViewById(R.id.RecAD_Img);
        recommendNameTxt = findViewById(R.id.recomdTxt);

        reference = FirebaseDatabase.getInstance().getReference(RecommendedAD.class.getSimpleName());

        result = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK){
                            Intent intent = result.getData();
                            if(intent != null){
                                try {
                                    Bitmap ImgBit = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), intent.getData());
                                    recAdIMG.setImageBitmap(ImgBit);
                                    ImgUri = intent.getData();
                                } catch (IOException e) {
                                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                                }

                            }
                        }
                    }
                });

    }

    public void openIMGChooser(View view){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        result.launch(intent);
    }

    private String getFileExtension(Uri uri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(getContentResolver().getType(uri));
    }

    public void InsertRecommendedAd(View view){
        if(ImgUri != null && !recommendNameTxt.getText().toString().trim().isEmpty()){
            StorageReference storage = FirebaseStorage.getInstance("gs://choizy-academic.appspot.com").getReference(RecommendedAD.class.getSimpleName()).child(System.currentTimeMillis()
                    + "." + getFileExtension(ImgUri));

            storage.putFile(ImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Uri> task) {
                            Toast.makeText(getApplicationContext(),"Inserted",Toast.LENGTH_SHORT).show();
                           // RecommendedAD ad = new RecommendedAD(recommendNameTxt.getText().toString(),task.getResult().toString());
                           // reference.push().setValue(ad);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {

                }
            });
        }


    }
}
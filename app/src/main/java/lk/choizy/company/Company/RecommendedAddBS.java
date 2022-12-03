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
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import lk.choizy.company.R;

public class RecommendedAddBS extends BottomSheetDialogFragment {

    TextInputLayout titleTIL;
    ImageView ADImage;
    Button addBtn;
    Uri uri;
    CompanyHelper db_helper;
    private ActivityResultLauncher<Intent> result;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recooment_add_bs,container,false);
        ADImage = view.findViewById(R.id.recommenedAdd_Img);
        titleTIL = view.findViewById(R.id.recommenedAdd_Title);
        addBtn = view.findViewById(R.id.recommenedAdd_Btn);
        db_helper = new CompanyHelper(getContext());

        result = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK){
                            if(result.getData() != null){
                                try {
                                    Bitmap ImgBit = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), result.getData().getData());
                                    ADImage.setImageBitmap(ImgBit);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                               uri = result.getData().getData();
                            }
                        }
                    }
                });

        ADImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                result.launch(intent);
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(uri != null && !titleTIL.getEditText().getText().toString().trim().isEmpty()){
                    insertRecoAd();
                }else{
                    if(titleTIL.getEditText().getText().toString().trim().isEmpty()){
                        titleTIL.setError("please give title for recommended ads");
                        titleTIL.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                titleTIL.setError(null);
                                titleTIL.requestFocus();
                            }
                        },5000);
                    }else{
                        addBtn.setError("Please select an image");
                        addBtn.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                addBtn.setError(null);
                            }
                        },5000);
                    }
                }
            }
        });


        return view;
    }
    private String getFileExtension(Uri uri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(getActivity().getContentResolver().getType(uri));
    }

    private void insertRecoAd(){
        String title = titleTIL.getEditText().getText().toString().trim();

        StorageReference storage = FirebaseStorage.getInstance().getReference(RecommendedAD.class.getSimpleName()).child(System.currentTimeMillis()
                + "." + getFileExtension(uri));

        storage.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        RecommendedAD ad = new RecommendedAD(task.getResult().toString(),db_helper.getCompanyId());
                        ad.setTitle(title);
                        db_helper.insertRecommendedAd(ad).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isComplete()){
                                    Snackbar.make(getActivity().findViewById(android.R.id.content),"Inserted",Snackbar.LENGTH_SHORT).show();
                                }

                                dismiss();
                            }
                        });
                    }
                });
            }
        });

    }

}

package lk.choizy.company.CompanyBraunch;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import lk.choizy.company.BranchHelper;
import lk.choizy.company.R;

public class QRGen_BS extends BottomSheetDialogFragment {

    TextInputLayout MerchantSecret,MerchantId;
    BranchHelper db_helper;
    Button genBtn;
    ImageView qrGenImg;
    ImageButton qrSaveBtn;
    Bitmap qrCode;
    OutputStream outputStream;
    private ActivityResultLauncher<String[]> requestPermissionLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.qr_gen_bs,container,false);
        MerchantId = view.findViewById(R.id.qrGen_MID);
        MerchantSecret = view.findViewById(R.id.qrGen_MSecret);
        genBtn = view.findViewById(R.id.qrGen_GenBtn);
        qrGenImg = view.findViewById(R.id.qrGen_GenIMG);
        db_helper = new BranchHelper(getContext());
        qrSaveBtn = view.findViewById(R.id.qrGen_SaveBtn);

        qrSaveBtn.setEnabled(false);
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                    for (boolean granted : result.values()) {
                        if(!granted){
                            Toast.makeText(getContext(), "Permission failed", Toast.LENGTH_SHORT).show();
                        }else{
                            saveQR();
                        }
                    }
                });

        genBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MerchantId.getEditText().getText().toString().trim().isEmpty()&&MerchantSecret.getEditText().getText().toString().trim().isEmpty()){
                    if(MerchantId.getEditText().getText().toString().trim().isEmpty()){
                        MerchantId.setError("Merchant Id can't be empty");
                        MerchantId.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                MerchantId.setError(null);
                                MerchantId.requestFocus();
                            }
                        },2000);
                    }else{
                        MerchantSecret.setError("Merchant Secret can't be empty");
                        MerchantSecret.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                MerchantSecret.setError(null);
                                MerchantSecret.requestFocus();
                            }
                        },2000);
                    }

                    return;
                }

                String content = "ChoizyPayment,"+MerchantId.getEditText().getText().toString().trim()+","+MerchantSecret.getEditText().getText().toString().trim()+","+db_helper.getBranchId();
                genQR(content);
            }
        });

        qrSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != (PackageManager.PERMISSION_GRANTED)){
                    requestPermissionLauncher.launch(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE});
                }else{
                    System.out.println("saving try");
                    saveQR();                }

            }
        });


        return view;
    }

    private void saveQR2(){
        OutputStream outputStream;
        String fileName = System.currentTimeMillis() + ".jpg";

        ContentValues qrCV = new ContentValues();
        qrCV.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        qrCV.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            qrCV.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/");
            qrCV.put(MediaStore.MediaColumns.IS_PENDING, 1);
        }else{
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            File file = new File(dir, fileName);
            qrCV.put(MediaStore.MediaColumns.DATA, file.getAbsolutePath());
        }
        Uri uri = requireActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, qrCV);

        try {

            outputStream = requireActivity().getContentResolver().openOutputStream(uri);
            qrCode.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void saveQR(){
//        String fileLoc = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        File root = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM+"/Choizy/");
//        String fileLoc = Environment.getExternalStorageDirectory().toString();
        if(!root.exists()){
            root.mkdirs();
        }

        File file= new File(root,System.currentTimeMillis()+".jpg");
        file.getParentFile().mkdir();


        try {

//            file.createNewFile();
//            file.mkdir();
//            outputStream = getContext().openFileOutput(file.toString(), Context.MODE_PRIVATE);
            outputStream = new FileOutputStream(file);
//            BufferedOutputStream bos = new BufferedOutputStream(outputStream);
            qrCode.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
            Toast.makeText(getContext(),"QR Saved in DCIM/Choizy",Toast.LENGTH_SHORT).show();
            dismiss();
//            bos.close();

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try {
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void genQR(String content){
        QRCodeWriter GenQR = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = GenQR.encode(content, BarcodeFormat.QR_CODE,700,700);
            Bitmap imgQR = Bitmap.createBitmap(bitMatrix.getWidth(), bitMatrix.getHeight(), Bitmap.Config.RGB_565);
            for (int x = 0; x < bitMatrix.getWidth(); x++) {
                for (int y = 0; y < bitMatrix.getHeight(); y++) {
                    imgQR.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            qrCode = imgQR.copy(imgQR.getConfig(),true);
            qrGenImg.setImageBitmap(imgQR);
            qrSaveBtn.setVisibility(View.VISIBLE);
            qrSaveBtn.setEnabled(true);


        } catch (WriterException e) {
            System.out.println(e);
        }
    }
}

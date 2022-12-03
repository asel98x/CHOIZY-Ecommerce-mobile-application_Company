package lk.choizy.company.Company;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.palette.graphics.Palette;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.Executors;

import lk.choizy.company.R;

public class AdViewReco_BSheet extends BottomSheetDialogFragment {
    ImageView banner;
    Button deleteBtn,updateBtn;
    TextInputLayout titleTIL;
    RecommendedAD ad;
    onDataChange listener;
    private ActivityResultLauncher<Intent> ARL;
    Palette.Swatch bgS1,bgS2;


    ConstraintLayout background;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recommended_view_bottomsheet,container,false);


        banner = view.findViewById(R.id.ADV_BS_Banner);
        deleteBtn = view.findViewById(R.id.ADV_BS_DeleteBtn);
        background = view.findViewById(R.id.AD_View_Background);
        updateBtn = view.findViewById(R.id.ADV_BS_UpdateBtn);
        titleTIL = view.findViewById(R.id.ADV_BS_TitleTIL);

        titleTIL.getEditText().setText(ad.getTitle());

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL imgUrl = new URL(ad.getUrl());
                    Bitmap bitmapAD = BitmapFactory.decodeStream(imgUrl.openConnection().getInputStream());

                    Palette.Builder builder = new Palette.Builder(bitmapAD);
                    builder.generate(new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(@Nullable @org.jetbrains.annotations.Nullable Palette palette) {
                            if(palette.getLightVibrantSwatch() !=null){
                                bgS1 = palette.getLightVibrantSwatch();
                            }else{
                                bgS1 =palette.getVibrantSwatch();
                            }
                            bgS2 = palette.getDarkVibrantSwatch();

                            //deleteBtn.setTextColor(bgS1.getTitleTextColor());

                            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,new int[]{bgS2.getRgb(),bgS1.getRgb()});
                            background.setBackground(gd);
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Picasso.get().load(ad.getUrl()).fit().centerCrop().into(banner);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!titleTIL.getEditText().getText().toString().trim().isEmpty()&&!titleTIL.getEditText().getText().toString().trim().equals(ad.getTitle())){
                    ad.setTitle(titleTIL.getEditText().getText().toString().trim());
                    if(listener!= null){
                        listener.onTitleUpdate(ad);
                        dismiss();
                    }
                }else{
                    if(titleTIL.getEditText().getText().toString().trim().isEmpty()){
                        titleTIL.setError("Title can't be empty");
                        titleTIL.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                titleTIL.setError(null);
                                titleTIL.requestFocus();
                                titleTIL.getEditText().setText(ad.getTitle());
                            }
                        },4000);
                    }else{
                        dismiss();
                    }
                }
            }
        });





        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null){
                    listener.onDeleteAd(ad);
                }
            }
        });

        banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent img = new Intent();
                img.setType("image/*");
                img.setAction(Intent.ACTION_GET_CONTENT);
                ARL.launch(img);
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
                                    banner.setImageBitmap(ImgBit);
                                    if(listener!=null){
                                        listener.onImageChange(ad,intent.getData());
                                    }
                                } catch (IOException e) {
                                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                                }

                            }
                        }
                    }
                });

        return view;
    }

    private void BackgroundChanger(){



    }

    public AdViewReco_BSheet(RecommendedAD ad) {
        this.ad = ad;
    }

    public interface onDataChange{
        void onDeleteAd(RecommendedAD ad);
        void onImageChange(RecommendedAD ad, Uri imgUri);
        void onTitleUpdate(RecommendedAD ad);
    }

    public void setListener(onDataChange listener) {
        this.listener = listener;
    }
}

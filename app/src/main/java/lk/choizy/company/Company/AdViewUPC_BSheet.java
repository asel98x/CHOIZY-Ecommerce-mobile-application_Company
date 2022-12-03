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
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.Executors;

import lk.choizy.company.R;

public class AdViewUPC_BSheet extends BottomSheetDialogFragment {

    ImageView banner;
    Button deleteBtn;
    UpcomingAD ad;
    onADChange listener;
    private ActivityResultLauncher<Intent> ARL;
    private Uri ImgUri;
    Palette.Swatch Vibrant,VibrantDark,VibrantLight,Muted,MutedDark,MutedLight;
    ConstraintLayout background;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.advertainment_view_bottomsheet,container,false);


        banner = view.findViewById(R.id.ADV_BS_Banner);
        deleteBtn = view.findViewById(R.id.ADV_BS_DeleteBtn);
        background = view.findViewById(R.id.AD_View_Background);

        final Bitmap[] bitmap = {null};

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    bitmap[0] = BitmapFactory.decodeStream(new URL(ad.getUrl()).openStream());
                    if (bitmap[0] != null) {
                        Palette.from(bitmap[0]).maximumColorCount(32).generate(palette -> {

                            if(palette.getLightVibrantSwatch() !=null){
                                VibrantLight = palette.getLightVibrantSwatch();
                            }else{
                                VibrantLight =palette.getVibrantSwatch();
                            }

                            MutedDark = palette.getDarkVibrantSwatch();
//                                background.setBackgroundColor(MutedDark.getRgb());
                           // deleteBtn.setBackgroundColor(VibrantLight.getRgb());
                            deleteBtn.setTextColor(VibrantLight.getTitleTextColor());

                            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,new int[]{MutedDark.getRgb(),VibrantLight.getRgb()});
                            background.setBackground(gd);

                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Picasso.get().load(ad.getUrl()).fit().centerCrop().into(banner);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null){
                    listener.onDelete(ad);
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
                                    ImgUri = intent.getData();
                                    if(listener!=null){
                                        listener.onChangeImage(ad,ImgUri);
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

    public AdViewUPC_BSheet(UpcomingAD ad) {
        this.ad = ad;
    }

    public interface onADChange{
        void onDelete(UpcomingAD ad);
        void onChangeImage(UpcomingAD ad,Uri ImgUri);
    }

    public void setListener(onADChange listener) {
        this.listener = listener;
    }
}

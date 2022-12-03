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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Currency;

import lk.choizy.company.Company.Offer;
import lk.choizy.company.R;

public class OfferViewBottomSheet extends BottomSheetDialogFragment {

    ImageView offerImg;
    TextInputLayout offerTitle,offerDescription,offerPrice;
    Button delOffer,updateBtn;
    Offer offer;
    OnDeleteOffer listener;
    private ActivityResultLauncher<Intent> ARL;
    private Uri ImgUri;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.offer_bottomsheet,container,false);
        offerImg = view.findViewById(R.id.OfferBS_OfferImg);
        offerTitle = view.findViewById(R.id.OfferBS_Title);
        offerDescription = view.findViewById(R.id.OfferBS_Description);
        offerPrice = view.findViewById(R.id.OfferBS_Price);
        delOffer = view.findViewById(R.id.OfferBS_DeleteBtn);
        updateBtn = view.findViewById(R.id.OfferBS_UpdateBtn);

        Picasso.get().load(offer.getOfferUrl()).centerCrop().fit().into(offerImg);
        offerTitle.getEditText().setText(offer.getTitle());
        offerDescription.getEditText().setText(offer.getDescription());
        offerPrice.getEditText().setText(""+offer.getPrice());

        delOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null){
                    listener.onDeleteBtnClick(offer);
                    dismiss();
                }
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(NeedUpdate()){
                    if(listener !=null){
                        if(!validated()){
                            return;
                        }
                        listener.onUpdateBtnClick(offer);
                        dismiss();
                    }
                }
            }
        });

        offerImg.setOnClickListener(new View.OnClickListener() {
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
                                    offerImg.setImageBitmap(ImgBit);
                                    ImgUri = intent.getData();
                                    if(listener != null){
                                        listener.onUploadImg(ImgUri,offer);
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

    private boolean validated(){
        boolean validate = true;

        if (offerPrice.getEditText().getText().toString().trim().isEmpty()){
            offerPrice.setError("Offer price can't be empty");
            offerPrice.postDelayed(new Runnable() {
                @Override
                public void run() {
                    offerPrice.setError(null);
                    offerPrice.requestFocus();
                    offerPrice.getEditText().setText(offer.getPrice()+"");
                }
            },5000);
            validate =false;
        }

        if (offerDescription.getEditText().getText().toString().trim().isEmpty()){
            offerDescription.setError("Description can't be empty");
            offerDescription.postDelayed(new Runnable() {
                @Override
                public void run() {
                    offerDescription.setError(null);
                    offerDescription.requestFocus();
                    offerDescription.getEditText().setText(offer.getDescription()+"");
                }
            },5000);
            validate =false;
        }

        if (offerTitle.getEditText().getText().toString().trim().isEmpty()){
            offerTitle.setError("Title can't be empty");
            offerTitle.postDelayed(new Runnable() {
                @Override
                public void run() {
                    offerTitle.setError(null);
                    offerTitle.requestFocus();
                    offerTitle.getEditText().setText(offer.getTitle()+"");
                }
            },5000);
            validate =false;
        }


        return validate;
    }

    private boolean NeedUpdate(){
        boolean update = false;

        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMinimumFractionDigits(0);
        format.setCurrency(Currency.getInstance("LKR"));

        if(!offerTitle.getEditText().getText().toString().trim().equals(offer.getTitle())){
            offer.setTitle(offerTitle.getEditText().getText().toString().trim());
            update = true;
        }

        if(!offerDescription.getEditText().getText().toString().trim().equals(offer.getDescription())){
            offer.setDescription(offerDescription.getEditText().getText().toString().trim());
            update = true;
        }
        if(Double.parseDouble(offerPrice.getEditText().getText().toString().trim())!=offer.getPrice()){
            offer.setPrice(Double.parseDouble(offerPrice.getEditText().getText().toString().trim()));
            update = true;
        }

        return update;
    }

    public interface OnDeleteOffer{
        void onDeleteBtnClick(Offer offer);
        void onUpdateBtnClick(Offer offer);
        void onUploadImg(Uri uri,Offer offer);
    }

    public OfferViewBottomSheet(Offer offer) {
        this.offer = offer;
    }

    public void setListener(OnDeleteOffer listener) {
        this.listener = listener;
    }


    
}

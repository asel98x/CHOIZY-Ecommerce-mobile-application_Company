package lk.choizy.company.Company;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;

import lk.choizy.company.R;

public class CompanyAbout_BS extends BottomSheetDialogFragment {

    Company company;
    TextInputLayout aboutTIL,featuresTIL;
    Button updateBtn;
    AboutUpdate listener;

    @Override
    public int getTheme() {
        return R.style.myBottomSheet;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.company_about_bs,container,false);
        aboutTIL = view.findViewById(R.id.CompAboutSheet_AboutTIL);
        featuresTIL = view.findViewById(R.id.CompAboutSheet_FeaturesTIL);
        updateBtn = view.findViewById(R.id.CompAboutSheet_UpdateBtn);

        aboutTIL.getEditText().setText(company.getCompanyAbout());
        featuresTIL.getEditText().setText(company.getCompanyFeatures());

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(updateCheck()){
                    if(listener!= null){
                        listener.onAboutUpdate(company);

                    }

                }
            }
        });






        return view;
    }

    private boolean updateCheck(){
        boolean needUpdate = false;
        if(!aboutTIL.getEditText().getText().toString().trim().equals(company.getCompanyAbout())){
            company.setCompanyAbout(aboutTIL.getEditText().getText().toString().trim());
            needUpdate = true;
        }

        if(!featuresTIL.getEditText().getText().toString().trim().equals(company.getCompanyFeatures())){
            company.setCompanyFeatures(featuresTIL.getEditText().getText().toString().trim());
            needUpdate = true;
        }
        return needUpdate;
    }

    public CompanyAbout_BS(Company company) {
        this.company = company;
    }

    public interface AboutUpdate{
        void onAboutUpdate(Company company);
    }

    public void setListener(AboutUpdate listener) {
        this.listener = listener;
    }
}

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

public class Terms_conditions_BS extends BottomSheetDialogFragment {

    TextInputLayout termsTIL;
    Button updateBtn;
    onTermsChange listener;
    Company company;

    @Override
    public int getTheme() {
        return R.style.myBottomSheet;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.terms_bs,container,false);

        termsTIL = view.findViewById(R.id.termsBS_termsTIL);
        updateBtn = view.findViewById(R.id.termsBS_udateBtn);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!termsTIL.getEditText().getText().toString().trim().equals(company.getCompanyTerms())){
                    company.setCompanyTerms(termsTIL.getEditText().getText().toString().trim());
                    if(listener != null){
                        listener.onTermsUpdated(company);
                        dismiss();
                    }
                }else {
                    dismiss();
                }
            }
        });



        return view;
    }

    public Terms_conditions_BS(Company company) {
        this.company = company;
    }

    public interface onTermsChange{
        void onTermsUpdated(Company company);
    }

    public void setListener(onTermsChange listener) {
        this.listener = listener;
    }
}

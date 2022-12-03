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

public class Discount_Range_BS extends BottomSheetDialogFragment {
    TextInputLayout discountRangeTIL;
    Button updateBtn;
    onDiscountRangeChange listener;

    Company company;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.discount_range_bs,container,false);
        discountRangeTIL = view.findViewById(R.id.DiscountRangeTIL);
        updateBtn = view.findViewById(R.id.DiscountRange_UpdateBtn);

        discountRangeTIL.getEditText().setText(company.getCompanyDiscountRange());

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!discountRangeTIL.getEditText().getText().toString().trim().isEmpty()){
                    if(!discountRangeTIL.getEditText().getText().toString().trim().equals(company.getCompanyDiscountRange())){
                        company.setCompanyDiscountRange(discountRangeTIL.getEditText().getText().toString().trim());
                        if(listener != null){
                            listener.onDiscountRangeUpdate(company);
                            dismiss();
                        }

                    }else {
                        dismiss();
                    }
                }else {
                    discountRangeTIL.setError("Discount Range can't be empty");

                    discountRangeTIL.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            discountRangeTIL.setError(null);
                            discountRangeTIL.requestFocus();
                            discountRangeTIL.getEditText().setText(company.getCompanyDiscountRange());
                        }
                    },5000);
                }

            }
        });


        return view;
    }

    public Discount_Range_BS(Company company) {
        this.company = company;
    }

    public interface onDiscountRangeChange{
        void onDiscountRangeUpdate(Company company);
    }

    public void setListener(onDiscountRangeChange listener) {
        this.listener = listener;
    }
}

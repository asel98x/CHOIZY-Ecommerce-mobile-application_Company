package lk.choizy.company.Company;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import lk.choizy.company.Company.Branch;
import lk.choizy.company.R;

public class OfferBLAdapter extends ArrayAdapter<Branch> {



    public OfferBLAdapter(@NonNull Context context, int resource, @NonNull List<Branch> objects) {
        super(context, 0, objects);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return CustomView2(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable @org.jetbrains.annotations.Nullable View convertView, @NonNull @NotNull ViewGroup parent) {
        return CustomView(position, convertView, parent);
    }

    private View CustomView(int position,View view,ViewGroup viewGroup){
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.offer_branch_list, viewGroup, false);
        }

        TextView textView = view.findViewById(R.id.OfferBranchList);

        Branch branch = getItem(position);

        if(branch != null){
            textView.setText(branch.getName());
        }

        return view;
    }

    private View CustomView2(int position,View view,ViewGroup viewGroup){
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.offer_branch_list2, viewGroup, false);
        }

        TextView textView = view.findViewById(R.id.OfferBranchList);


        Branch branch = getItem(position);

        if(branch != null){
            textView.setText(branch.getName());
        }

        return view;
    }
}

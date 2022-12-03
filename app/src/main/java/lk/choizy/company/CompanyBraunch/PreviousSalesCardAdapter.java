package lk.choizy.company.CompanyBraunch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.ArrayList;

import lk.choizy.company.BranchHelper;
import lk.choizy.company.R;

public class PreviousSalesCardAdapter extends RecyclerView.Adapter<PreviousSalesCardAdapter.View_Holder> {
    ArrayList<Sales> list = new ArrayList<>();

    @NonNull
    @Override
    public View_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.previous_salas_card,parent,false);
        return new View_Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull View_Holder holder, int position) {
        Sales sales = list.get(position);
        holder.monthTxt.setText(sales.getYearMonth());
        holder.amountTxt.setText(BranchHelper.getFormattedPrice(sales.getTotalPrice()));

    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class View_Holder extends RecyclerView.ViewHolder {
        TextView monthTxt;
        TextView amountTxt;

        public View_Holder(@NonNull View itemView) {
            super(itemView);
            monthTxt = itemView.findViewById(R.id.PreviousSaleMonth);
            amountTxt = itemView.findViewById(R.id.PreviousSaleTotal);

        }
    }

    public void setList(ArrayList<Sales> list) {
        this.list = list;
    }
}

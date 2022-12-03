package lk.choizy.company;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import lk.choizy.company.CompanyBraunch.Sales;

public class SalesCardAdapter extends RecyclerView.Adapter<SalesCardAdapter.Viewholder> {
    ArrayList<Orders> list = new ArrayList<>();

    @NonNull
    @NotNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sales_card,parent,false);

        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Viewholder holder, int position) {
        Orders sales = list.get(position);
        holder.offerName.setText(sales.getOrderID());
        holder.amount.setText(BranchHelper.getFormattedPrice(sales.getTotal()));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView offerName,amount;
        public Viewholder(@NonNull @NotNull View itemView) {
            super(itemView);
            offerName = itemView.findViewById(R.id.salesCard_Offer);
            amount = itemView.findViewById(R.id.salesCard_Amount);
        }
    }

    public void setList(ArrayList<Orders> list) {
        this.list = list;
    }
}

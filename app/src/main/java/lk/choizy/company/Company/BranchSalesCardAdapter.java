package lk.choizy.company.Company;

import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import lk.choizy.company.CompanyBraunch.Sales;
import lk.choizy.company.R;

public class BranchSalesCardAdapter extends RecyclerView.Adapter<BranchSalesCardAdapter.Viewholder> {
    ArrayList<BranchSalesModel> list = new ArrayList<>();

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.branch_sales_card,parent,false);

        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        BranchSalesModel sales = list.get(position);
        holder.offerName.setText(sales.getOfferName());
        holder.dateTxt.setText(sales.getDate());
        holder.price.setText(CompanyHelper.getFormattedPrice(sales.getPrice()));
        holder.timeTxt.setText(sales.getTime());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class SalesDecoration extends RecyclerView.ItemDecoration{
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.bottom =15;
        }
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        TextView dateTxt,timeTxt,offerName,price;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            dateTxt = itemView.findViewById(R.id.branchSaleCard_Date);
            timeTxt = itemView.findViewById(R.id.branchSaleCard_Time);
            offerName = itemView.findViewById(R.id.branchSaleCard_Offer);
            price = itemView.findViewById(R.id.branchSaleCard_Price);

        }
    }

    public void setList(ArrayList<BranchSalesModel> list) {
        this.list = list;
    }
}

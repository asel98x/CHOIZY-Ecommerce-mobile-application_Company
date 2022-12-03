package lk.choizy.company.CompanyBraunch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import lk.choizy.company.R;

public class OrderListItemAdapter extends RecyclerView.Adapter<OrderListItemAdapter.View_Holder> {
    ArrayList<Cart> list = new ArrayList<>();

    @NonNull
    @Override
    public View_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_list_card,parent,false);

        return new View_Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull View_Holder holder, int position) {
        Cart cart = list.get(position);
        holder.offerName.setText(cart.getOffer().getTitle());
        holder.qut.setText(cart.getQut()+"");

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class View_Holder extends RecyclerView.ViewHolder {
        TextView offerName,qut;
        public View_Holder(@NonNull View itemView) {
            super(itemView);
            offerName = itemView.findViewById(R.id.orderItems_OferName);
            qut = itemView.findViewById(R.id.orderItems_Qut);

        }
    }

    public void setList(ArrayList<Cart> list) {
        this.list = list;
    }
}

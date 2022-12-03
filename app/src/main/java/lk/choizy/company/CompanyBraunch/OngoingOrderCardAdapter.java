package lk.choizy.company.CompanyBraunch;

import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import lk.choizy.company.Orders;
import lk.choizy.company.R;

public class OngoingOrderCardAdapter extends RecyclerView.Adapter<OngoingOrderCardAdapter.Viewholder> {

    ArrayList<Orders> list =new ArrayList<>();
    onItemSelected listener;

    @NonNull
    @NotNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_card,parent,false);

        return new Viewholder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Viewholder holder, int position) {
        Orders orders = list.get(position);
        holder.customerName.setText(orders.getStudentName());
        if(orders.getCartList().size()>1){
            holder.offerName.setText(orders.getCartList().get(0).getOffer().getTitle()+"..+more");
        }else{
            holder.offerName.setText(orders.getCartList().get(0).getOffer().getTitle());
        }

        holder.orderID.setText("Order ID :"+orders.getOrderID());
        holder.time.setText(orders.getTime());

        Picasso.get().load(orders.getStudent().getImageURL()).into(holder.customerImg);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class OrderCardDecoration extends RecyclerView.ItemDecoration{
        @Override
        public void getItemOffsets(@NonNull @NotNull Rect outRect, @NonNull @NotNull View view, @NonNull @NotNull RecyclerView parent, @NonNull @NotNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.bottom=15;
        }
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ShapeableImageView customerImg;
        TextView offerName,customerName,orderID,time;
        public Viewholder(@NonNull @NotNull View itemView,onItemSelected listener) {
            super(itemView);
            customerImg = itemView.findViewById(R.id.orderCard_CusomerImg);
            offerName = itemView.findViewById(R.id.orderCard_OfferName);
            customerName = itemView.findViewById(R.id.orderCard_CustomerName);
            orderID = itemView.findViewById(R.id.orderCard_orderID);
            time = itemView.findViewById(R.id.orderCard_time);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!= null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.ItemSelected(position);
                        }
                    }
                }
            });

        }
    }



    public void setList(ArrayList<Orders> list) {
        this.list = list;
    }

    public interface onItemSelected{
        void ItemSelected(int position);
    }

    public void setListener(onItemSelected listener) {
        this.listener = listener;
    }
}

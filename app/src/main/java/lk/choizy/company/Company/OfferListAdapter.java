package lk.choizy.company.Company;

import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import lk.choizy.company.R;

public class OfferListAdapter extends RecyclerView.Adapter<OfferListAdapter.Viewholder> {

    ArrayList<Offer> list = new ArrayList<>();
    OnItemClick listener;

    @NonNull
    @NotNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ads_list_card,parent,false);
        return new Viewholder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Viewholder holder, int position) {
        Offer offer = list.get(position);
        Picasso.get().load(offer.getOfferUrl()).centerCrop().fit().into(holder.offerImg);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder {
        ImageView offerImg;
        public Viewholder(@NonNull @NotNull View itemView, OnItemClick listener) {
            super(itemView);
            offerImg = itemView.findViewById(R.id.adsBannerImg);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!= null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClickLister(position);
                        }
                    }
                }
            });

        }
    }

    public static class OfferDecoration extends RecyclerView.ItemDecoration{
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            outRect.bottom =25;
            outRect.right = 20;
            outRect.left = 20;
        }
    }

    public void setList(ArrayList<Offer> list) {
        this.list = list;
    }


    public interface OnItemClick{
        void onItemClickLister(int position);
    }

    public void setListener(OnItemClick listener) {
        this.listener = listener;
    }
}

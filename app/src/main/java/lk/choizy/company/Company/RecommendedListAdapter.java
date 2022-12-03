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

public class RecommendedListAdapter extends RecyclerView.Adapter<RecommendedListAdapter.Viewholder> {

    ArrayList<RecommendedAD> list;
    OnItemListener listener;

    @NonNull
    @NotNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ads_list_card,parent,false);


        return new Viewholder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Viewholder holder, int position) {
        RecommendedAD ad = list.get(position);
        Picasso.get().load(ad.getUrl()).centerCrop().fit().into(holder.AdBanner);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder {
        ImageView AdBanner;
        public Viewholder(@NonNull @NotNull View itemView,OnItemListener listener) {
            super(itemView);
            AdBanner = itemView.findViewById(R.id.adsBannerImg);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.ItemClickListener(position);
                        }
                    }
                }
            });


        }
    }

    public static class RecommendedDecoration extends RecyclerView.ItemDecoration{
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            if(parent.getChildAdapterPosition(view)==0){
                outRect.top=25;
            }
            outRect.bottom = 25;
            outRect.left =20;
            outRect.right =20;
        }
    }

    public RecommendedListAdapter() {
        this.list = new ArrayList<>();
    }

    public void setList(ArrayList<RecommendedAD> list) {
        this.list = list;
    }

    public interface OnItemListener{
        void ItemClickListener(int position);
    }

    public void setListener(OnItemListener listener) {
        this.listener = listener;
    }
}

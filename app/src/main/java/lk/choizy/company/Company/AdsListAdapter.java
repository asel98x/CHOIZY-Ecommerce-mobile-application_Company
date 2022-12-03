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

public class AdsListAdapter extends RecyclerView.Adapter<AdsListAdapter.Viewholder> {


    ArrayList<UpcomingAD> list = new ArrayList<>();
    onItemSelected listener;

    @NonNull
    @NotNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ads_list_card,parent,false);


        return new Viewholder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Viewholder holder, int position) {
        UpcomingAD ad = list.get(position);
        Picasso.get().load(ad.getUrl()).centerCrop().fit().into(holder.banner);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class AdDecoration extends RecyclerView.ItemDecoration{
        @Override
        public void getItemOffsets(@NonNull @NotNull Rect outRect, @NonNull @NotNull View view, @NonNull @NotNull RecyclerView parent, @NonNull @NotNull RecyclerView.State state) {
            if(parent.getChildAdapterPosition(view)==0){
                outRect.top=25;
            }
            outRect.bottom = 25;
            outRect.left =20;
            outRect.right =20;
        }
    }

    public static class Viewholder extends RecyclerView.ViewHolder {

        ImageView banner;
        public Viewholder(@NonNull @NotNull View itemView,onItemSelected listener) {
            super(itemView);
            banner = itemView.findViewById(R.id.adsBannerImg);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.ItemSelected(position);
                        }
                    }
                }
            });


        }
    }

    public AdsListAdapter() {

    }

    public void setList(ArrayList<UpcomingAD> list) {
        if (list != null){
            this.list = list;
        }

    }

    public interface onItemSelected{
        void ItemSelected(int position);
    }

    public void setListener(onItemSelected listener) {
        this.listener = listener;
    }
}

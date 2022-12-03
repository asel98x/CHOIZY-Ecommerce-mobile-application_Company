package lk.choizy.company.Company;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import lk.choizy.company.R;

public class BranchListAdapter extends RecyclerView.Adapter<BranchListAdapter.Viewholder> {

    ArrayList<Branch> list = new ArrayList<>();
    private OnItemClick listener;


    @NonNull
    @NotNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.branch_list_card,parent,false);


        return new Viewholder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Viewholder holder, int position) {
        Branch branch = list.get(position);
        String address = branch.getNo_adres()+" "+branch.getStreetAddress()+" "+branch.getCity();
        holder.nameTxt.setText(branch.getName());
        holder.emailTxt.setText(branch.getEmail());
        holder.addressTxt.setText(address);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder {

        TextView nameTxt,emailTxt,addressTxt;
        ImageButton editBtn;
        public Viewholder(@NonNull @NotNull View itemView,OnItemClick listener) {
            super(itemView);
            nameTxt = itemView.findViewById(R.id.card_branch_Name);
            emailTxt = itemView.findViewById(R.id.card_branch_Emaill);
            addressTxt = itemView.findViewById(R.id.card_branch_Address);
            editBtn = itemView.findViewById(R.id.card_branch_EditBtn);

            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position !=RecyclerView.NO_POSITION){
                            listener.onItemEdit(position);
                        }
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position !=RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public interface OnItemClick{
        void onItemClick(int position);
        void onItemEdit(int position);
    }

    public void setListener(OnItemClick listener) {
        this.listener = listener;
    }

    public void setList(ArrayList<Branch> list) {
        if(list != null){
            this.list = list;
        }

    }



}

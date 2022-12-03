package lk.choizy.company.CompanyBraunch;

import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import lk.choizy.company.Feedback;
import lk.choizy.company.R;

public class FeedbackCardAdapter extends RecyclerView.Adapter<FeedbackCardAdapter.View_Holder> {
    ArrayList<Feedback> list = new ArrayList<>();

    @NonNull
    @Override
    public View_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_card,parent,false);

        return new View_Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull View_Holder holder, int position) {
        Feedback feedback = list.get(position);
        holder.feedBack.setText(feedback.getMsg());
        holder.studentName.setText(feedback.getStudentName());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class feedbackDecoration extends RecyclerView.ItemDecoration{
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            if(parent.getChildAdapterPosition(view) == 0){
                outRect.top =30;
            }
            outRect.bottom = 50;

        }
    }

    public class View_Holder extends RecyclerView.ViewHolder {
        TextView feedBack;
        TextView studentName;
        public View_Holder(@NonNull View itemView) {
            super(itemView);
            feedBack = itemView.findViewById(R.id.feedbackCard_Msg);
            studentName = itemView.findViewById(R.id.feedbackCard_StudentName);


        }
    }


    public void setList(ArrayList<Feedback> list) {
        this.list = list;
    }
}

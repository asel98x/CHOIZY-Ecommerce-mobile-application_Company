package lk.choizy.company.Company;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import lk.choizy.company.CompanyBraunch.FeedbackCardAdapter;
import lk.choizy.company.Feedback;
import lk.choizy.company.R;

public class CompBranchFeedback extends AppCompatActivity {

    RecyclerView feedbackLV;
    FeedbackCardAdapter adapter;
    ValueEventListener feedbackVEl;
    ArrayList<Feedback> feedbacks;
    CompanyHelper db_helper;
    String branchID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_feed_back);
        feedbackLV = findViewById(R.id.feedbackLV);
        adapter = new FeedbackCardAdapter();
        feedbackLV.addItemDecoration(new FeedbackCardAdapter.feedbackDecoration());
        db_helper = new CompanyHelper(getApplicationContext());
        feedbacks = new ArrayList<>();
        Intent intent = getIntent();
        branchID = intent.getStringExtra("BranchID");


//        branchID =

        feedbackLV.setAdapter(adapter);
        feedbackLV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        loadFeedback();
    }

    private void loadFeedback() {
        feedbackVEl = db_helper.getFeedBack(branchID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                feedbacks.clear();
                for (DataSnapshot one:dataSnapshot.getChildren()) {
                    Feedback feedback = one.getValue(Feedback.class);
                    feedbacks.add(feedback);
                }
                adapter.setList(feedbacks);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStop() {
        db_helper.getFeedBack(branchID).removeEventListener(feedbackVEl);
        super.onStop();
    }
}
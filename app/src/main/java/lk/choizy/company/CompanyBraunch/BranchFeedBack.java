package lk.choizy.company.CompanyBraunch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import lk.choizy.company.BranchHelper;
import lk.choizy.company.Feedback;
import lk.choizy.company.R;
import lk.choizy.company.network_change_listner;

public class BranchFeedBack extends AppCompatActivity {

    RecyclerView feedbackLV;
    FeedbackCardAdapter adapter;
    BranchHelper db_helper;
    ValueEventListener feedbackVEl;
    ArrayList<Feedback> feedbacks;
    network_change_listner networkChangeListener = new network_change_listner();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_feed_back);
        feedbackLV = findViewById(R.id.feedbackLV);
        adapter = new FeedbackCardAdapter();
        feedbackLV.addItemDecoration(new FeedbackCardAdapter.feedbackDecoration());
        feedbacks = new ArrayList<>();
        db_helper = new BranchHelper(getApplicationContext());


        feedbackLV.setAdapter(adapter);
        feedbackLV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        loadFeedBack();
    }

    private void loadFeedBack() {
        feedbackVEl = db_helper.getFeedBack().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot one:dataSnapshot.getChildren()) {
                    Feedback feedback = one.getValue(Feedback.class);
                    feedbacks.add(feedback);
                }
                Collections.reverse(feedbacks);
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
        db_helper.getFeedBack().removeEventListener(feedbackVEl);
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,filter);
        super.onStart();
    }

}
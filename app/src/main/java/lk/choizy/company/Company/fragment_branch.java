package lk.choizy.company.Company;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import lk.choizy.company.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_branch#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_branch extends Fragment implements BranchListAdapter.OnItemClick {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final String CASharePreference = "Choizy_SharedPreference";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView branchList;
    private FloatingActionButton AddBranchBtn;
    private SearchView branchSeachTxt;
    private ArrayList<Branch> list = new ArrayList<>();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private BranchListAdapter adapter;
    private ValueEventListener dbListener;
    private ProgressBar BranchLV_PB;

    public fragment_branch() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_branch.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_branch newInstance(String param1, String param2) {
        fragment_branch fragment = new fragment_branch();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_branch, container, false);
        branchList = view.findViewById(R.id.BranchListView);
        BranchLV_PB = view.findViewById(R.id.BranchLV_PB);

        adapter = new BranchListAdapter();
        branchList.setAdapter(adapter);
        branchList.setLayoutManager(new LinearLayoutManager(getContext()));
        branchList.addItemDecoration(new ListDecoration());
        adapter.setListener(this);
        AddBranchBtn = view.findViewById(R.id.AddBranchBtn);

        BranchSearch("");
        branchSeachTxt = view.findViewById(R.id.BranchSeachTxt);



        branchSeachTxt.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                BranchSearch(newText);
                return true;
            }
        });



        AddBranchBtn.setOnClickListener(v -> {

            Intent intent = new Intent(getActivity(), Branch_Create.class);
            startActivity(intent);
        });





        return view;
    }

    private void BranchSearch(String SearchTxt){
        DatabaseReference myRef = database.getReference(Branch.class.getSimpleName());

        if(dbListener != null){
            myRef.removeEventListener(dbListener);
        }

        if(SearchTxt.equals("")){
        dbListener = myRef.orderByChild("compID").equalTo(getCompanyId()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                    BranchLV_PB.setVisibility(View.INVISIBLE);
                    list.clear();
                    for(DataSnapshot one:dataSnapshot.getChildren()){
                        Branch branch = one.getValue(Branch.class);
                        branch.setID(one.getKey());
                        list.add(branch);
                    }

                    adapter.setList(list);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {


                }
            });
        }else{
            dbListener = myRef.orderByChild("compID").equalTo(getCompanyId()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                    list.clear();
                    for(DataSnapshot one:dataSnapshot.getChildren()){
                        Branch branch = one.getValue(Branch.class);
                        branch.setID(one.getKey());

                        if(branch.getEmail().toLowerCase().contains(SearchTxt.toLowerCase()) ||branch.getName().toLowerCase().contains(SearchTxt.toLowerCase()) ){
                            list.add(branch);
                        }

                    }

                    adapter.setList(list);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {


                }
            });
        }

    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), ViewBranchDetails.class);
        intent.putExtra("BranchID",list.get(position).getID());
        intent.putExtra("BranchName",list.get(position).getName());
        startActivity(intent);

    }

    @Override
    public void onItemEdit(int position) {
        Intent intent = new Intent(getActivity(), Branch_Update.class);
        intent.putExtra("BranchID",list.get(position).getID());
        startActivity(intent);

    }

    private String getCompanyId(){
        SharedPreferences preferences = requireContext().getSharedPreferences(CASharePreference, MODE_PRIVATE);

        return preferences.getString("CompanyID",null);
    }
}
package lk.choizy.company.CompanyBraunch;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import lk.choizy.company.BranchHelper;
import lk.choizy.company.Company.ListDecoration;
import lk.choizy.company.Orders;
import lk.choizy.company.R;
import lk.choizy.company.SalesCardAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CurrentMonth_Sales#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CurrentMonth_Sales extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView currentSaleLV;
    BranchHelper db_helper;
    ArrayList<Orders> list;
    ValueEventListener currentSalesVEL;
    SalesCardAdapter adapter;

    public CurrentMonth_Sales() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CurrentMonth_Sales.
     */
    // TODO: Rename and change types and number of parameters
    public static CurrentMonth_Sales newInstance(String param1, String param2) {
        CurrentMonth_Sales fragment = new CurrentMonth_Sales();
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
        View view =inflater.inflate(R.layout.fragment_current_month_sales, container, false);
        db_helper = new BranchHelper(getContext());
        currentSaleLV = view.findViewById(R.id.currentSaleLV);
        list = new ArrayList<>();
        adapter = new SalesCardAdapter();
        adapter.setList(list);
        currentSaleLV.setAdapter(adapter);
        currentSaleLV.addItemDecoration(new ListDecoration());
        currentSaleLV.setLayoutManager(new LinearLayoutManager(getContext()));


        loadCurrentSales();

        return view;
    }

    private void loadCurrentSales(){
        db_helper.getCurrentSale().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot one: dataSnapshot.getChildren()) {
                    Orders orders = one.getValue(Orders.class);
                    orders.setOrderID(one.getKey());
                    list.add(orders);
                }
                adapter.setList(list);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
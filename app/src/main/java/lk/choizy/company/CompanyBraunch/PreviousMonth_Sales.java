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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import lk.choizy.company.BranchHelper;
import lk.choizy.company.Company.ListDecoration;
import lk.choizy.company.Orders;
import lk.choizy.company.R;
import lk.choizy.company.SalesCardAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PreviousMonth_Sales#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PreviousMonth_Sales extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
    SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView PreviousSaleLV;
    ArrayList<Sales> list;
    PreviousSalesCardAdapter adapter;
    ValueEventListener previousSalesVEL;
    BranchHelper db_helper;

    public PreviousMonth_Sales() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PreviousMonth_Sales.
     */
    // TODO: Rename and change types and number of parameters
    public static PreviousMonth_Sales newInstance(String param1, String param2) {
        PreviousMonth_Sales fragment = new PreviousMonth_Sales();
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
        View view = inflater.inflate(R.layout.fragment_previous_month_sales, container, false);
        PreviousSaleLV = view.findViewById(R.id.PreviousSaleLV);
        list = new ArrayList<>();
        db_helper = new BranchHelper(getContext());

        adapter = new PreviousSalesCardAdapter();

//        adapter.setLi
        PreviousSaleLV.setAdapter(adapter);
        PreviousSaleLV.addItemDecoration(new ListDecoration());
        PreviousSaleLV.setLayoutManager(new LinearLayoutManager(getContext()));

        loadPreviousSales();
        return view;
    }

    private void loadPreviousSales() {
        previousSalesVEL = db_helper.getPreviousSale().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();

                for (DataSnapshot one :dataSnapshot.getChildren()) {
                    String yearMonth = one.getKey();
                    double total = 0;
                    for (DataSnapshot two:one.getChildren()) {
                        Orders orders = two.getValue(Orders.class);
                        orders.setOrderID(two.getKey());
                        total +=orders.getTotal();
                    }
                    String year = yearMonth.substring(0,4);
                    String month = yearMonth.substring(4);

                    list.add(new Sales(year+"-"+month,total));


                }
                adapter.setList(list);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStop() {
        db_helper.getPreviousSale().removeEventListener(previousSalesVEL);
        super.onStop();
    }
}
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
import java.util.Collections;
import java.util.Iterator;

import lk.choizy.company.BranchHelper;
import lk.choizy.company.Orders;
import lk.choizy.company.R;
import lk.choizy.company.Student;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderHistoryTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderHistoryTab extends Fragment implements OngoingOrderCardAdapter.onItemSelected{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView orderHistoryLV;
    BranchHelper branchHelper;
    OngoingOrderCardAdapter adapter;
    ArrayList<Orders> list;
    ValueEventListener orderHistoryVEL;

    public OrderHistoryTab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderHistoryTab.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderHistoryTab newInstance(String param1, String param2) {
        OrderHistoryTab fragment = new OrderHistoryTab();
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
        View view = inflater.inflate(R.layout.order_history_tab, container, false);
        orderHistoryLV = view.findViewById(R.id.orderHistoryLV);
        adapter = new OngoingOrderCardAdapter();
        list = new ArrayList<>();
        branchHelper = new BranchHelper(getContext());

        adapter.setListener(this);

        orderHistoryLV.setLayoutManager(new LinearLayoutManager(getContext()));
        orderHistoryLV.setAdapter(adapter);

        orderHistoryLV.addItemDecoration(new OngoingOrderCardAdapter.OrderCardDecoration());
        loadOrderHistory();

        return view;
    }

    public void loadOrderHistory(){
        orderHistoryVEL = branchHelper.getHistoryOrder(branchHelper.getBranchId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                while (iterator.hasNext()) {
                    DataSnapshot one = iterator.next();
                    Orders orders = one.getValue(Orders.class);
                    orders.setOrderID(one.getKey());

                    branchHelper.studentGetDetailsByKey(orders.getStudentKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Student student = dataSnapshot.getValue(Student.class);
                            student.setKeey(one.getKey());
                            orders.setStudent(student);
                            list.add(orders);
                            if(!iterator.hasNext()){
                                Collections.reverse(list);
                                adapter.setList(list);
                                adapter.notifyDataSetChanged();

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void ItemSelected(int position) {
        OrderFinishView_BS finishView_bs = new OrderFinishView_BS(list.get(position));
        finishView_bs.show(getChildFragmentManager(),"Order history");
    }
}
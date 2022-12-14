package lk.choizy.company.CompanyBraunch;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import lk.choizy.company.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Orders#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Orders extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TabLayout orderTabL;
    private ViewPager2 orderVP2;

    public Fragment_Orders() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Orders.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Orders newInstance(String param1, String param2) {
        Fragment_Orders fragment = new Fragment_Orders();
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
        View view = inflater.inflate(R.layout.fragment__orders, container, false);

        orderTabL = view.findViewById(R.id.orderTabL);
        orderVP2 = view.findViewById(R.id.ordersVP2);
        OrdersTabAdapter adapter = new OrdersTabAdapter(getChildFragmentManager(),getLifecycle());
        orderVP2.setAdapter(adapter);
        orderTabL.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                orderVP2.setCurrentItem(orderTabL.getSelectedTabPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        orderVP2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
//                super.onPageSelected(position);
                orderTabL.selectTab(orderTabL.getTabAt(position));
            }
        });

        return view;
    }
}
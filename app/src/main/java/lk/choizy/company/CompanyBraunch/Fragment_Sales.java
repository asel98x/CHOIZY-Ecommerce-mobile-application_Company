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
 * Use the {@link Fragment_Sales#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Sales extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TabLayout salesTavLayout;
    private ViewPager2 salesTabViewPager;

    public Fragment_Sales() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Sales.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Sales newInstance(String param1, String param2) {
        Fragment_Sales fragment = new Fragment_Sales();
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
        View view = inflater.inflate(R.layout.fragment__sales, container, false);
        salesTavLayout = view.findViewById(R.id.SalesTavLayout);
        salesTabViewPager = view.findViewById(R.id.SalesTabViewPager);
        SalesTabAdapter adapter = new SalesTabAdapter(getChildFragmentManager(),getLifecycle());
        salesTabViewPager.setAdapter(adapter);
        salesTavLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                salesTabViewPager.setCurrentItem(salesTavLayout.getSelectedTabPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        salesTabViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
//                super.onPageSelected(position);
                salesTavLayout.selectTab(salesTavLayout.getTabAt(position));
            }
        });


        return view;
    }
}
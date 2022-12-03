package lk.choizy.company.Company;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import lk.choizy.company.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAdvertisement#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAdvertisement extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TabLayout adsTabL;
    private ViewPager2 adsVP2;
    private FloatingActionButton fabAddAds;

    public FragmentAdvertisement() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentAdvertisement.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentAdvertisement newInstance(String param1, String param2) {
        FragmentAdvertisement fragment = new FragmentAdvertisement();
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
        View view = inflater.inflate(R.layout.fragment_advertisement, container, false);
        adsTabL =view.findViewById(R.id.AdsTabL);
        adsVP2 = view.findViewById(R.id.AdsVP2);
        AdvertisementTabAdapter adapter = new AdvertisementTabAdapter(getChildFragmentManager(),getLifecycle());
        adsVP2.setAdapter(adapter);
        adsTabL.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                adsVP2.setCurrentItem(adsTabL.getSelectedTabPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        adsVP2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
//                super.onPageSelected(position);
                adsTabL.selectTab(adsTabL.getTabAt(position));
            }
        });


        return view;
    }
}
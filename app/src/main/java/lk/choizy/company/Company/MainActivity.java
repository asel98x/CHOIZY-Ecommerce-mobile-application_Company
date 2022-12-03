package lk.choizy.company.Company;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import lk.choizy.company.Company.FragmentAdvertisement;
import lk.choizy.company.Company.FragmentCompanyProfile;
import lk.choizy.company.Company.FragmentOffers;
import lk.choizy.company.Company.fragment_branch;
import lk.choizy.company.R;
import lk.choizy.company.network_change_listner;

public class MainActivity extends AppCompatActivity {


    ChipNavigationBar navigationBar;
    network_change_listner networkChangeListener = new network_change_listner();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigationBar = findViewById(R.id.companyBottomNav);
        navigationBar.setItemSelected(R.id.nav_branch,true);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,new fragment_branch()).commit();
        bottomNav();



    }

    private void bottomNav() {

        navigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;

                switch (i){
                    case R.id.nav_branch:
                    fragment = new fragment_branch();
                    break;
                    case R.id.nav_advertising:
                    fragment = new FragmentAdvertisement();
                    break;
                    case  R.id.nav_offers:
                    fragment = new FragmentOffers();
                    break;
                    case R.id.nav_profile:
                    fragment = new FragmentCompanyProfile();
                    break;

                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,fragment).commit();

            }
        });

    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }

}
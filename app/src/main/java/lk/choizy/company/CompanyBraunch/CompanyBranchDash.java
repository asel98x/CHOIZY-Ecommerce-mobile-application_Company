package lk.choizy.company.CompanyBraunch;

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

public class CompanyBranchDash extends AppCompatActivity {

    ChipNavigationBar navBar;
    network_change_listner networkChangeListener = new network_change_listner();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_branch_dash);
        navBar = findViewById(R.id.branchBNav);
        navBar.setItemSelected(R.id.nav_Orders,true);
        getSupportFragmentManager().beginTransaction().replace(R.id.branchFragLoder,new Fragment_Orders()).commit();
        bottomNav();
    }

    private void bottomNav() {
        navBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;

                switch (i){
                    case R.id.nav_Orders:
                        fragment = new Fragment_Orders();
                        break;
                    case R.id.nav_branch_Offers:
                        fragment = new Fragment_Branch_Offers();
                        break;
                    case  R.id.nav_SaleHistory:
                        fragment = new Fragment_Sales();
                        break;
                    case R.id.nav_Branch_Profile:
                        fragment = new Fragment_Branch_Profile();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.branchFragLoder,fragment).commit();
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
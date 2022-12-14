package lk.choizy.company.Company;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.jetbrains.annotations.NotNull;

public class AdvertisementTabAdapter extends FragmentStateAdapter {


    public AdvertisementTabAdapter(@NonNull @NotNull FragmentManager fragmentManager, @NonNull @NotNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new UpcomingAdsTab();
            case 1:
                return new RecommendedAdsTab();
        }

        return null;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

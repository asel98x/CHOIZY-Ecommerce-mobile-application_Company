package lk.choizy.company.CompanyBraunch;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.jetbrains.annotations.NotNull;

public class SalesTabAdapter extends FragmentStateAdapter {
    public SalesTabAdapter(@NonNull @NotNull FragmentManager fragmentManager, @NonNull @NotNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new CurrentMonth_Sales();
            case 1:
               return new PreviousMonth_Sales();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

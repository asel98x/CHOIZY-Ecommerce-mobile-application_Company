package lk.choizy.company.Company;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class ListDecoration extends RecyclerView.ItemDecoration {

    @Override
    public void getItemOffsets(@NonNull @NotNull Rect outRect, @NonNull @NotNull View view, @NonNull @NotNull RecyclerView parent, @NonNull @NotNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        outRect.bottom = 40;
        outRect.right = 5;
        outRect.left = 5;

        if(parent.getChildAdapterPosition(view) == 0){
            outRect.top = 10;
        }
    }
}

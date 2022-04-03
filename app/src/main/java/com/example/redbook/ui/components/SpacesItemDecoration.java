package com.example.redbook.ui.components;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        int position = parent.getChildAdapterPosition(view); // item position

        if (position == 0) {
            outRect.left = space * 2;
            outRect.right = 0;
        } else if (position == parent.getAdapter().getItemCount() - 1) {
            outRect.left = space;
            outRect.right = space * 2;
        } else {
            outRect.left = space;
            outRect.right = 0;
        }

    }
}

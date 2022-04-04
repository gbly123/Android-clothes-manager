package com.example.redbook.utils;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class GridItemDecoration extends RecyclerView.ItemDecoration {

    private int space;  //位移间距

    public GridItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) % 2 == 0) {
            outRect.left = 0; //第一列左边贴边
        } else {
            if (parent.getChildAdapterPosition(view) % 2 == 1) {
                outRect.left = space;//第二列移动一个位移间距
            } else {
                outRect.left = space * 2;//由于第二列已经移动了一个间距，所以第三列要移动两个位移间距就能右边贴边，且item间距相等
            }
        }

        if (parent.getChildAdapterPosition(view) >= 2) {
            outRect.top = 10;
        } else {
            outRect.top = 0;
        }
    }
}

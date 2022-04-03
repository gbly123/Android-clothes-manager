package com.example.redbook.ui.components.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redbook.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder {

    public TextView categoryItemTv;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        init();
    }

    private void init() {
        categoryItemTv = itemView.findViewById(R.id.category_tv);
    }
}

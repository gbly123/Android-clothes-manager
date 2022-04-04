package com.example.redbook.ui.detail;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redbook.R;

public class HvpViewHolder extends RecyclerView.ViewHolder {

    public ImageView mIv;

    public HvpViewHolder(@NonNull View itemView) {
        super(itemView);
        init();
    }

    private void init() {
        mIv = itemView.findViewById(R.id.iv);
    }
}

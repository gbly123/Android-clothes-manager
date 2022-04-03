package com.example.redbook.ui.add;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redbook.R;

public class PicViewHolder extends RecyclerView.ViewHolder {

    public ImageView picIv;

    public PicViewHolder(@NonNull View itemView) {
        super(itemView);
        init();
    }

    private void init() {
        picIv = itemView.findViewById(R.id.pic_iv);
    }
}

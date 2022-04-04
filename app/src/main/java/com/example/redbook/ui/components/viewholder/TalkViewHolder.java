package com.example.redbook.ui.components.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redbook.R;

public class TalkViewHolder extends RecyclerView.ViewHolder {

    public TextView resultItemTv;
    public ImageView addIv;

    public TalkViewHolder(@NonNull View itemView) {
        super(itemView);
        init();
    }


    private void init() {
        resultItemTv = itemView.findViewById(R.id.result_tv);
        addIv = itemView.findViewById(R.id.add);
    }
}

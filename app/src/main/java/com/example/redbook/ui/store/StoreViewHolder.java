package com.example.redbook.ui.store;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redbook.R;

import org.w3c.dom.Text;

public class StoreViewHolder extends RecyclerView.ViewHolder {

    public ImageView cover;
    public TextView title;
    public TextView time;

    public StoreViewHolder(@NonNull View itemView) {
        super(itemView);
        init();
    }

    private void init() {
        cover = itemView.findViewById(R.id.iv);
        title = itemView.findViewById(R.id.title);
        time = itemView.findViewById(R.id.time);
    }
}

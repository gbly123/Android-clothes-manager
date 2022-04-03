package com.example.redbook.ui.add;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.redbook.R;

import java.util.ArrayList;
import java.util.List;

public class PicAdapter extends RecyclerView.Adapter<PicViewHolder> {

    private List<Uri> list = new ArrayList<>();
    private int width;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        onItemClickListener = itemClickListener;
    }


    public PicAdapter(int width) {
        this.width = width;
    }

    public List<Uri> getData() {
        return list;
    }

    public void setData(List<Uri> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.pic_item_layout, null, false);
        return new PicViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull PicViewHolder holder, int position) {
        Context context = holder.itemView.getContext();

        if (position < list.size()) {
            Glide.with(context).load(list.get(position)).into(holder.picIv);
        } else {
            Glide.with(context).load(R.drawable.icon_add_pic).into(holder.picIv);
        }
        ViewGroup.LayoutParams layoutParams = holder.picIv.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = width;
        holder.picIv.setLayoutParams(layoutParams);

        holder.picIv.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.itemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size() + 1 >= 9 ? 9 : list.size() + 1;
    }

    public interface OnItemClickListener {
        void itemClick(int position);
    }

}

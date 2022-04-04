package com.example.redbook.ui.detail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.redbook.R;

import java.util.Arrays;
import java.util.List;

public class HvpAdapter extends RecyclerView.Adapter<HvpViewHolder> {

    private List<String> mList;

    public HvpAdapter( String picPath) {
        String[] split = picPath.split("\\|");
        mList = Arrays.asList(split);
    }

    @NonNull
    @Override
    public HvpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.big_pic_item_layout, parent, false);
        return new HvpViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull HvpViewHolder holder, int position) {

        Glide.with(holder.mIv.getContext()).load(mList.get(position)).into(holder.mIv);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}

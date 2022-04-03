package com.example.redbook.ui.components.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redbook.R;
import com.example.redbook.ui.components.viewholder.CategoryViewHolder;
import com.example.redbook.ui.components.viewholder.ResultViewHolder;

import java.util.ArrayList;
import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultViewHolder> {

    private List<String> mList = new ArrayList<>();

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.talk_result_item_layout, null);
        return new ResultViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        String s = mList.get(position);
        holder.resultItemTv.setText(s);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setData(List<String> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }
}

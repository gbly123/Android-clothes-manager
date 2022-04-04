package com.example.redbook.ui.components.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redbook.R;
import com.example.redbook.db.entity.TalkCategory;
import com.example.redbook.ui.components.viewholder.CategoryViewHolder;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

    private List<TalkCategory> mList = new ArrayList<>();

    private OnCategoryItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnCategoryItemClickListener itemClickListener) {
        onItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.talk_category_item_layout, null);
        return new CategoryViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

        if (position >= mList.size()) {
            //ADD
            holder.addIv.setVisibility(View.VISIBLE);
            holder.categoryItemTv.setVisibility(View.GONE);
            holder.addIv.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.addCategoryClick();
                }
            });
        } else {
            holder.addIv.setVisibility(View.GONE);
            holder.categoryItemTv.setVisibility(View.VISIBLE);

            TalkCategory talkCategory = mList.get(position);
            holder.categoryItemTv.setText(talkCategory.name);

            holder.categoryItemTv.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.categoryItemClick(talkCategory);
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return mList.size() + 1;
    }

    public void setData(List<TalkCategory> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public interface OnCategoryItemClickListener {
        void categoryItemClick(TalkCategory category);

        void addCategoryClick();
    }
}

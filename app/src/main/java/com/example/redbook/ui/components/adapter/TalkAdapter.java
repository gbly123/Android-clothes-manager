package com.example.redbook.ui.components.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redbook.R;
import com.example.redbook.db.entity.Talk;
import com.example.redbook.ui.components.viewholder.TalkViewHolder;

import java.util.ArrayList;
import java.util.List;

public class TalkAdapter extends RecyclerView.Adapter<TalkViewHolder> {

    private List<Talk> mList = new ArrayList<>();

    private OnTalkItemClickListener onItemClickListener;
    private int category;

    public void setOnItemClickListener(OnTalkItemClickListener itemClickListener) {
        onItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public TalkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.talk_result_item_layout, null);
        return new TalkViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull TalkViewHolder holder, int position) {

        if (position >= mList.size()) {
            //ADD
            holder.addIv.setVisibility(View.VISIBLE);
            holder.resultItemTv.setVisibility(View.GONE);
            holder.addIv.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.addTalkClick(category);
                }
            });
        } else {
            holder.addIv.setVisibility(View.GONE);
            holder.resultItemTv.setVisibility(View.VISIBLE);

            Talk talk = mList.get(position);
            holder.resultItemTv.setText("# " + talk.name);

            holder.resultItemTv.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.talkItemClick(talk);
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return mList.size() + 1;
    }

    public void setData(List<Talk> list, int category) {
        mList.clear();
        mList.addAll(list);
        this.category = category;
        notifyDataSetChanged();
    }

    public interface OnTalkItemClickListener {
        void talkItemClick(Talk talk);

        void addTalkClick(int category);
    }
}

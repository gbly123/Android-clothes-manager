package com.example.redbook.ui.components.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redbook.R;
import com.example.redbook.db.entity.Talk;
import com.example.redbook.ui.add.PicAdapter;
import com.example.redbook.ui.components.viewholder.TalkViewHolder;

import java.util.ArrayList;
import java.util.List;

public class TalkAdapter extends RecyclerView.Adapter<TalkViewHolder> {

    private List<Talk> mList = new ArrayList<>();

    private OnTalkItemClickListener onItemClickListener;

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
        Talk talk = mList.get(position);
        holder.resultItemTv.setText("# " + talk.name);

        holder.resultItemTv.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.talkItemClick(talk);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setData(List<Talk> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public interface OnTalkItemClickListener {
        void talkItemClick(Talk talk);
    }
}

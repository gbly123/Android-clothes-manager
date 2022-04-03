package com.example.redbook.ui.store;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.redbook.R;
import com.example.redbook.db.entity.Diary;
import com.example.redbook.utils.GlideCircleTransform;
import com.example.redbook.utils.GlideRoundTransform;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter<StoreViewHolder> {

    private final int screenWidth;
    private List<Diary> mList = new ArrayList<>();
    private final Context mContext;

    public StoreAdapter(Context context, int screenWidth) {
        mContext = context;
        this.screenWidth = screenWidth;
    }

    @NonNull
    @Override
    public StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.diary_item_layout, parent, false);
        return new StoreViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreViewHolder holder, int position) {
        Diary diary = mList.get(position);
        String picPath = diary.picPath;
        String uri = picPath.split("\\|")[0];
        ViewGroup.LayoutParams layoutParams = holder.cover.getLayoutParams();
        layoutParams.width = screenWidth / 2 - 10;
        layoutParams.height = (int) (layoutParams.width * 1.5);
        holder.cover.setLayoutParams(layoutParams);
        Glide.with(mContext)
                .load(uri)
                .centerCrop()
                .bitmapTransform(new GlideRoundTransform(mContext, 10))
                .crossFade().diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.cover);
        holder.title.setText(diary.title);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = new Date(diary.time);
        String format = sdf.format(date);
        holder.time.setText(format);

        Glide.with(mContext)
                .load(R.drawable.capa_demo_filter_12)
                .centerCrop()
                .bitmapTransform(new GlideCircleTransform(mContext))
                .crossFade().diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.head);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setData(List<Diary> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }
}

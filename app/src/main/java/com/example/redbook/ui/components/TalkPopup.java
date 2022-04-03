package com.example.redbook.ui.components;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redbook.R;
import com.example.redbook.ui.components.adapter.CategoryAdapter;
import com.example.redbook.ui.components.adapter.ResultAdapter;

import java.util.ArrayList;
import java.util.List;

public class TalkPopup extends PopupWindow {

    private RecyclerView categoryTalkRv;
    private RecyclerView resultTalkRv;
    private final Context mContext;

    private CategoryAdapter categoryAdapter;
    private ResultAdapter resultAdapter;

    public TalkPopup(Context context, int height) {
        super(context);
        mContext = context;
        View inflate = LayoutInflater.from(context).inflate(R.layout.talk_popup_layout, null);
        setContentView(inflate);
        initView(inflate);

        //设置PopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置PopupWindow弹出窗体的高
        this.setHeight(height);
        //设置PopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.Animation);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

    }

    private void initView(View inflate) {
        categoryTalkRv = inflate.findViewById(R.id.category_talk_rv);
        LinearLayoutManager hManager = new LinearLayoutManager(mContext);
        hManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryTalkRv.setLayoutManager(hManager);
        categoryTalkRv.addItemDecoration(new SpacesItemDecoration(10));
        categoryAdapter = new CategoryAdapter();
        categoryTalkRv.setAdapter(categoryAdapter);

        resultTalkRv = inflate.findViewById(R.id.result_talk_rv);
        LinearLayoutManager vManager = new LinearLayoutManager(mContext);
        vManager.setOrientation(LinearLayoutManager.VERTICAL);
        resultTalkRv.setLayoutManager(vManager);
        resultAdapter = new ResultAdapter();
        resultTalkRv.setAdapter(resultAdapter);
    }

    public void setCategory(List<String> list) {
        categoryAdapter.setData(list);
        categoryAdapter.notifyDataSetChanged();
    }

    public void setResult(List<String> list) {
        resultAdapter.setData(list);
        resultAdapter.notifyDataSetChanged();
    }
}

package com.example.redbook.ui.components;

import static com.example.redbook.ui.talk.AddTalkActivity.ADD_CATEGORY;
import static com.example.redbook.ui.talk.AddTalkActivity.ADD_TALK;
import static com.example.redbook.ui.talk.AddTalkActivity.ADD_TYPE;
import static com.example.redbook.ui.talk.AddTalkActivity.CATEGORY_ID;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redbook.R;
import com.example.redbook.db.RedBookDataBase;
import com.example.redbook.db.dao.TalkCategoryDao;
import com.example.redbook.db.dao.TalkDao;
import com.example.redbook.db.entity.Talk;
import com.example.redbook.db.entity.TalkCategory;
import com.example.redbook.ui.components.adapter.CategoryAdapter;
import com.example.redbook.ui.components.adapter.TalkAdapter;
import com.example.redbook.ui.talk.AddTalkActivity;

import java.util.List;

public class TalkPopup extends PopupWindow implements CategoryAdapter.OnCategoryItemClickListener, TalkAdapter.OnTalkItemClickListener {

    private RecyclerView categoryTalkRv;
    private RecyclerView resultTalkRv;
    private final Context mContext;

    private CategoryAdapter categoryAdapter;
    private TalkAdapter talkAdapter;
    private LifecycleOwner mLifecycleOwner;
    private MutableLiveData<Integer> talkLiveData;


    private OnTalkItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnTalkItemClickListener itemClickListener) {
        onItemClickListener = itemClickListener;
    }

    public TalkPopup(Context context, int height, LifecycleOwner lifecycleOwner) {
        super(context);
        mContext = context;
        mLifecycleOwner = lifecycleOwner;
        View inflate = LayoutInflater.from(context).inflate(R.layout.talk_popup_layout, null);
        setContentView(inflate);
        initView(inflate);

        //??????PopupWindow??????????????????
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //??????PopupWindow??????????????????
        this.setHeight(height);
        //??????PopupWindow?????????????????????
        this.setFocusable(true);
        //??????SelectPicPopupWindow????????????????????????
        this.setAnimationStyle(R.style.Animation);
        //???????????????ColorDrawable??????????????????
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //??????SelectPicPopupWindow?????????????????????
        this.setBackgroundDrawable(dw);

        initData();
    }

    private void initData() {

        RedBookDataBase dataBase = RedBookDataBase.getRedBookDataBaseInstance(mContext);

        talkLiveData = new MutableLiveData<>();
        talkLiveData.observe(mLifecycleOwner, integer -> {
            TalkDao talkDao = dataBase.getTalkDao();
            talkDao.getTalksByCategory(integer).observe(mLifecycleOwner, talks -> setTalk(talks, integer));
        });


        TalkCategoryDao talkCategoryDao = dataBase.getTalkCategoryDao();
        talkCategoryDao.getAllCategory().observe(mLifecycleOwner, talkCategories -> {
            setCategory(talkCategories);
            talkLiveData.postValue(talkCategories.get(0).id);
        });


    }

    private void initView(View inflate) {
        categoryTalkRv = inflate.findViewById(R.id.category_talk_rv);
        LinearLayoutManager hManager = new LinearLayoutManager(mContext);
        hManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryTalkRv.setLayoutManager(hManager);
        categoryTalkRv.addItemDecoration(new SpacesItemDecoration(10));
        categoryAdapter = new CategoryAdapter();
        categoryTalkRv.setAdapter(categoryAdapter);
        categoryAdapter.setOnItemClickListener(this);


        resultTalkRv = inflate.findViewById(R.id.result_talk_rv);
        LinearLayoutManager vManager = new LinearLayoutManager(mContext);
        vManager.setOrientation(LinearLayoutManager.VERTICAL);
        resultTalkRv.setLayoutManager(vManager);
        talkAdapter = new TalkAdapter();
        resultTalkRv.setAdapter(talkAdapter);
        talkAdapter.setOnItemClickListener(this);
    }

    public void setCategory(List<TalkCategory> talkCategories) {
        if (talkCategories == null) {
            return;
        }
        categoryAdapter.setData(talkCategories);
    }

    public void setTalk(List<Talk> talks, int category) {
        if (talks == null) {
            return;
        }
        talkAdapter.setData(talks, category);
    }

    @Override
    public void categoryItemClick(TalkCategory category) {
        talkLiveData.postValue(category.id);
    }

    @Override
    public void addCategoryClick() {
        Intent intent = new Intent(mContext, AddTalkActivity.class);
        intent.putExtra(ADD_TYPE, ADD_CATEGORY);
        mContext.startActivity(intent);
    }

    @Override
    public void talkItemClick(Talk talk) {
        if (onItemClickListener != null) {
            onItemClickListener.talkItemClick(talk);
        }
    }

    @Override
    public void addTalkClick(int category) {
        Intent intent = new Intent(mContext, AddTalkActivity.class);
        intent.putExtra(ADD_TYPE, ADD_TALK);
        intent.putExtra(CATEGORY_ID, category);
        mContext.startActivity(intent);
    }


    public interface OnTalkItemClickListener {
        void talkItemClick(Talk talk);
    }

}

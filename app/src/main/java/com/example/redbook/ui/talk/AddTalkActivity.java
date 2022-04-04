package com.example.redbook.ui.talk;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.redbook.databinding.ActivityAddTalkBinding;
import com.example.redbook.db.RedBookDataBase;
import com.example.redbook.db.entity.Talk;
import com.example.redbook.db.entity.TalkCategory;
import com.example.redbook.utils.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

public class AddTalkActivity extends AppCompatActivity {

    //0.ADD CATEGORY  1.ADD TALK
    public static final String ADD_TYPE = "ADD_TYPE";
    public static final String CATEGORY_ID = "CATEGORY_ID";
    public static final String ADD_CATEGORY = "0";
    public static final String ADD_TALK = "1";
    private ActivityAddTalkBinding binding;
    private String currentType;
    private int addCategoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        transparentStatusBar(getWindow());
        binding = ActivityAddTalkBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int statusBarHeight = StatusBarUtils.getStatusBarHeight(this);
        ConstraintLayout container = binding.container;
        container.setPadding(0, statusBarHeight, 0, 0);


        binding.submitTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentType.equals(ADD_CATEGORY)) {
                    List<TalkCategory> list = new ArrayList<>();
                    String category = binding.categoryEt.getText().toString();
                    list.add(new TalkCategory(category));
                    RedBookDataBase.getRedBookDataBaseInstance(AddTalkActivity.this).getTalkCategoryDao().insertCategory(list);

                } else {
                    List<Talk> list = new ArrayList<>();
                    String talk = binding.talkEt.getText().toString();
                    list.add(new Talk(talk, addCategoryId));
                    RedBookDataBase.getRedBookDataBaseInstance(AddTalkActivity.this).getTalkDao().insertTalk(list);
                }
                finish();
            }
        });

        initData();
    }

    private void initData() {

        Intent intent = getIntent();
        if (intent != null) {
            currentType = intent.getStringExtra(ADD_TYPE);
            addCategoryId = intent.getIntExtra(CATEGORY_ID, -1);
            binding.submitTv.setVisibility(View.VISIBLE);
            if (currentType.equals(ADD_CATEGORY)) {
                binding.categoryTv.setVisibility(View.VISIBLE);
                binding.categoryEt.setVisibility(View.VISIBLE);
                binding.talkTv.setVisibility(View.INVISIBLE);
                binding.talkEt.setVisibility(View.INVISIBLE);
            } else if (currentType.equals(ADD_TALK) && addCategoryId != -1) {
                binding.categoryTv.setVisibility(View.INVISIBLE);
                binding.categoryEt.setVisibility(View.INVISIBLE);
                binding.talkTv.setVisibility(View.VISIBLE);
                binding.talkEt.setVisibility(View.VISIBLE);
            }


        }

    }

    public void transparentStatusBar(@NonNull final Window window) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            int option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            int vis = window.getDecorView().getSystemUiVisibility();
            window.getDecorView().setSystemUiVisibility(option | vis);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}
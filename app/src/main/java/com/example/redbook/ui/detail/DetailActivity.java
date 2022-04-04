package com.example.redbook.ui.detail;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.example.redbook.databinding.ActivityDetailBinding;
import com.example.redbook.db.entity.Diary;
import com.example.redbook.utils.StatusBarUtils;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding binding;
    public static final String KEY_DIARY = "KEY_DIARY";
    private Diary diary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        transparentStatusBar(getWindow());
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int statusBarHeight = StatusBarUtils.getStatusBarHeight(this);

        ConstraintLayout container = binding.container;
        container.setPadding(0, statusBarHeight, 0, 0);

        initData();
        initView();
    }

    private void initView() {
        ViewPager2 vp = binding.vp;
        int width = getWindowManager().getDefaultDisplay().getWidth();
        ViewGroup.LayoutParams layoutParams = vp.getLayoutParams();
        layoutParams.height = (int) (width * 1.2);
        vp.setLayoutParams(layoutParams);
        HvpAdapter adapter = new HvpAdapter(diary.picPath);
        vp.setAdapter(adapter);

        binding.title.setText(diary.title);
        binding.content.setText(diary.content);
    }

    private void initData() {

        Bundle bundle = getIntent().getExtras();
        diary = (Diary) bundle.getSerializable(KEY_DIARY);
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
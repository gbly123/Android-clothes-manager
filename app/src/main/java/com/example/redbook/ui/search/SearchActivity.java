package com.example.redbook.ui.search;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redbook.databinding.ActivitySearchBinding;
import com.example.redbook.db.RedBookDataBase;
import com.example.redbook.db.entity.Diary;
import com.example.redbook.ui.detail.DetailActivity;
import com.example.redbook.ui.store.StoreAdapter;
import com.example.redbook.utils.GridItemDecoration;
import com.example.redbook.utils.StatusBarUtils;

import java.util.List;

public class SearchActivity extends AppCompatActivity implements StoreAdapter.OnItemClickListener {

    @NonNull
    private ActivitySearchBinding binding;
    private StoreAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        transparentStatusBar(getWindow());
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int statusBarHeight = StatusBarUtils.getStatusBarHeight(this);
        ConstraintLayout container = binding.container;
        container.setPadding(0, statusBarHeight, 0, 0);

        RecyclerView recyclerView = binding.list;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new GridItemDecoration(10));
        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();

        adapter = new StoreAdapter(this, screenWidth);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);


        binding.topNav.back.setOnClickListener(v -> finish());

        binding.topNav.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSearch();
            }
        });
    }

    private void startSearch() {

        String key = binding.topNav.et.getText().toString();
        if (TextUtils.isEmpty(key)) {
            Toast.makeText(this, "关键字为空", Toast.LENGTH_SHORT).show();
            return;
        }

        RedBookDataBase.getRedBookDataBaseInstance(this).getDiaryDao().getDiaryByKey(key).observe(this, new Observer<List<Diary>>() {
            @Override
            public void onChanged(List<Diary> diaries) {
                adapter.setData(diaries);
            }
        });
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

    @Override
    public void itemClick(Diary diary) {
        Intent intent = new Intent(this, DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(DetailActivity.KEY_DIARY, diary);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
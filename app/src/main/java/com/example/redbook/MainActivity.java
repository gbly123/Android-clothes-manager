package com.example.redbook;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.redbook.databinding.ActivityMainBinding;
import com.example.redbook.db.RedBookDataBase;
import com.example.redbook.db.dao.TalkCategoryDao;
import com.example.redbook.db.dao.TalkDao;
import com.example.redbook.db.entity.Talk;
import com.example.redbook.db.entity.TalkCategory;
import com.example.redbook.ui.add.AddActivity;
import com.example.redbook.utils.StatusBarUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        transparentStatusBar(getWindow());
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        MenuItem item = navView.getMenu().getItem(1);
        item.setTitle("");
        item.setIcon(null);

        int statusBarHeight = StatusBarUtils.getStatusBarHeight(this);

        ConstraintLayout container = binding.container;
        container.setPadding(0, statusBarHeight, 0, 0);

        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();

        navView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                navView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int measuredHeight = navView.getMeasuredHeight();
                ViewGroup.LayoutParams layoutParams = binding.addIvContainer.getLayoutParams();
                layoutParams.width = screenWidth / 3;
                layoutParams.height = measuredHeight;
                binding.addIvContainer.setLayoutParams(layoutParams);
            }
        });

        binding.addIvContainer.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddActivity.class);
            startActivity(intent);
        });

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        initDbData();
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

    private void initDbData() {
        TalkCategoryDao talkCategoryDao = RedBookDataBase.getRedBookDataBaseInstance(this).getTalkCategoryDao();
        talkCategoryDao.getAllCategory().observe(this, talkCategories -> {
            if (talkCategories == null || talkCategories.size() == 0) {
                //初始化数据
                initCategory(talkCategoryDao);
            }
        });

    }

    /**
     * 衣物种类：外套，裤子…；
     * 饰品：项链，戒指…；
     * 季节：春、夏、秋、冬；
     * 温度；
     * 体感：潮湿，干燥，适宜；
     * 风格：简约、温柔、性感、小香风、复古、法式、日系、慵懒、韩版、欧美、街头、港风、校园、甜美）
     *
     * @param talkCategoryDao
     */
    private void initCategory(TalkCategoryDao talkCategoryDao) {

        String[] categorys = {"衣物", "饰品", "季节", "温度", "体感", "风格"};
        List<TalkCategory> list = new ArrayList<>();
        for (int i = 0; i < categorys.length; i++) {
            list.add(new TalkCategory(categorys[i]));
        }

        talkCategoryDao.insertCategory(list);
        initTalk();
    }

    private void initTalk() {
        TalkDao talkDao = RedBookDataBase.getRedBookDataBaseInstance(this).getTalkDao();
        talkDao.getAllTalks().observe(this, new Observer<List<Talk>>() {
            @Override
            public void onChanged(List<Talk> talks) {

                if (talks == null || talks.size() == 0) {

                    String[] type1 = {"外套", "裤子"};
                    String[] type2 = {"项链", "戒指"};
                    String[] type3 = {"春", "夏", "秋", "冬"};
                    String[] type4 = {"0-5℃", "6-10℃", "11-15℃", "16-20℃", "21-25℃", "26-30℃"};
                    String[] type5 = {"潮湿", "干燥", "适宜"};
                    String[] type6 = {"简约", "温柔", "性感", "小香风", "复古", "法式", "日系", "慵懒", "韩版", "欧美", "街头", "港风", "校园", "甜美"};

                    String[][] types = {type1, type2, type3, type4, type5, type6};


                    for (int i = 0; i < types.length; i++) {
                        String[] type = types[i];
                        List<Talk> list = new ArrayList<>();
                        for (int j = 0; j < type.length; j++) {
                            Talk talk = new Talk(type[j], i + 1);
                            list.add(talk);
                        }
                        talkDao.insertTalk(list);
                    }
                }
            }
        });


    }

}
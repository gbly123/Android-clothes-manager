package com.example.redbook;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.example.redbook.db.RedBookDataBase;
import com.example.redbook.db.dao.TalkCategoryDao;
import com.example.redbook.db.dao.TalkDao;
import com.example.redbook.db.entity.Talk;
import com.example.redbook.db.entity.TalkCategory;
import com.example.redbook.ui.add.AddActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.redbook.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        MenuItem item = navView.getMenu().getItem(1);
        item.setTitle("");
        item.setIcon(null);


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
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        initDbData();
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
package com.example.redbook.ui.add;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.redbook.R;
import com.example.redbook.ui.components.SpacesItemDecoration;
import com.example.redbook.utils.CheckPermission;
import com.example.redbook.utils.MyGlideEngine;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.w3c.dom.Text;

import java.util.List;

public class AddActivity extends AppCompatActivity implements PicAdapter.OnItemClickListener, TextWatcher {

    private static final int REQUEST_CODE_CHOOSE = 0;
    private static final int MAX_PIC = 9;
    private static final int MAX_TITLE_LENGTH = 20;
    private RecyclerView picRv;
    private PicAdapter adapter;
    private EditText titleEt;
    private EditText contentEt;
    private TextView titleNumTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        initView();
        checkPermission(9);

    }

    private void initView() {
        picRv = findViewById(R.id.pic_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        picRv.setLayoutManager(linearLayoutManager);
        picRv.addItemDecoration(new SpacesItemDecoration(10));
        int width = getWindowManager().getDefaultDisplay().getWidth();
        adapter = new PicAdapter(width / 4);
        adapter.setOnItemClickListener(this);
        picRv.setAdapter(adapter);

        titleEt = findViewById(R.id.title_et);
        contentEt = findViewById(R.id.content_et);
        titleEt.addTextChangedListener(this);
        titleNumTv = findViewById(R.id.titie_num_tv);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            List<Uri> list = Matisse.obtainResult(data);
            if (list == null || list.size() == 0) {
                return;
            }
            adapter.setData(list);
        }
    }

    private void checkPermission(int count) {

        final String[] strings = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

        new CheckPermission(this).requestPermission(strings, new CheckPermission.PermissionLinstener() {
            @Override
            public void onSuccess(Context context, List<String> data) {
                openPicture(count);
            }

            @Override
            public void onFailed(Context context, List<String> data) {
                Toast.makeText(context, "申请失败", Toast.LENGTH_SHORT).show();
            }

            /**
             *  可在此调用，GuidePermission();引导用户前往系统设置页面获取权限
             * @param context
             * @param data
             */
            @Override
            public void onNotApply(final Context context, List<String> data) {
                Toast.makeText(context, "用户点击了不再提示", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void openPicture(int count) {
        Matisse.from(this)
                .choose(MimeType.ofImage())
                //是否只显示选择的类型的缩略图，就不会把所有图片视频都放在一起，而是需要什么展示什么
                .showSingleMediaType(true)
                //这两行要连用 是否在选择图片中展示照相 和适配安卓7.0 FileProvider
                .capture(true)
                .captureStrategy(new CaptureStrategy(true, "PhotoPicker"))
                //有序选择图片 123456...
                .countable(true)
                //最大选择数量为9
                .maxSelectable(count)
                .thumbnailScale(0.8f)
                //黑色主题
                .theme(R.style.Matisse_Dracula)
                //Glide加载方式
                .imageEngine(new MyGlideEngine())
                //请求码
                .forResult(REQUEST_CODE_CHOOSE);
    }

    @Override
    public void itemClick(int position) {
        int itemCount = adapter.getData().size();
        if (position < itemCount) {
            //打开图片
        } else {
            //继续添加
            checkPermission(MAX_PIC - itemCount);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        int length = titleEt.getText().length();
        titleNumTv.setText(String.valueOf(MAX_TITLE_LENGTH - length));
    }
}
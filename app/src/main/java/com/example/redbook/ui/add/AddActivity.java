package com.example.redbook.ui.add;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.redbook.R;
import com.example.redbook.db.RedBookDataBase;
import com.example.redbook.db.entity.Diary;
import com.example.redbook.db.entity.Talk;
import com.example.redbook.ui.components.SpacesItemDecoration;
import com.example.redbook.ui.components.TalkPopup;
import com.example.redbook.utils.CheckPermission;
import com.example.redbook.utils.MyGlideEngine;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.List;

public class AddActivity extends AppCompatActivity implements PicAdapter.OnItemClickListener, TextWatcher, View.OnClickListener, TalkPopup.OnTalkItemClickListener {

    private static final int REQUEST_CODE_CHOOSE = 0;
    private static final int MAX_PIC = 9;
    private static final int MAX_TITLE_LENGTH = 20;
    private RecyclerView picRv;
    private PicAdapter picAdapter;
    private EditText titleEt;
    private EditText contentEt;
    private TextView titleNumTv;
    private TalkPopup talkPopup;
    private int lastSelection;

    public static final String LEFT_SPACE = " #";
    public static final String RIGHT_SPACE = " ";

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
        picAdapter = new PicAdapter(width / 4);
        picAdapter.setOnItemClickListener(this);
        picRv.setAdapter(picAdapter);

        titleEt = findViewById(R.id.title_et);
        contentEt = findViewById(R.id.content_et);
        titleEt.addTextChangedListener(this);
        titleNumTv = findViewById(R.id.titie_num_tv);

        View submitTv = findViewById(R.id.submit_tv);
        submitTv.setOnClickListener(this);
        View addTalkTv = findViewById(R.id.add_talk_tv);
        addTalkTv.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            List<Uri> list = Matisse.obtainResult(data);
            if (list == null || list.size() == 0) {
                return;
            }
            picAdapter.setData(list);
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
        int itemCount = picAdapter.getData().size();
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.submit_tv) {
            //提交
            submit();
        } else if (id == R.id.add_talk_tv) {
            //保存光标位置
            lastSelection = contentEt.getSelectionStart();
            //话题
            showPop();
        }
    }

    private void submit() {
        String title = titleEt.getText().toString();
        String content = contentEt.getText().toString();
        List<Uri> data = picAdapter.getData();
        String uriString = getUriString(data);
        String talkString = getTalkString(content);
        Diary diary = new Diary(title, content, uriString, talkString, System.currentTimeMillis());
        RedBookDataBase.getRedBookDataBaseInstance(this).getDiaryDao().insertTalk(diary);
    }

    private String getUriString(List<Uri> data) {
        StringBuilder sb = new StringBuilder();
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                if (i == data.size() - 1) {
                    sb.append(data.get(i));
                } else {
                    sb.append(data.get(i)).append("|");
                }
            }
        }
        return sb.toString();
    }

    private String getTalkString(String content) {

        StringBuilder sb = new StringBuilder();

        int lastIndex = 0;
        //遍历字符窜长度
        for (int i = 0; i < content.length(); i++) {
            //先看左边界
            int leftIndex = content.indexOf(LEFT_SPACE, lastIndex);
            if (leftIndex != -1) {
                //再看有边界
                int rightIndex = content.indexOf(RIGHT_SPACE, leftIndex + 1);
                //存在话题
                if (rightIndex != -1) {
                    //保存，下一次使用
                    lastIndex = rightIndex;
                    String talk = content.substring(leftIndex + 2, rightIndex);
                    if (!TextUtils.isEmpty(talk)) {
                        sb.append(talk).append("|");
                    }
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        if (sb.toString().endsWith("|")) {
            return sb.substring(0, sb.toString().length() - 1);
        } else {
            sb.toString();
        }

        return sb.toString();
    }

    private void showPop() {
        if (talkPopup == null) {
            int height = getWindowManager().getDefaultDisplay().getHeight();
            talkPopup = new TalkPopup(this, (int) (height / 2.5), this);
            talkPopup.setOnItemClickListener(this);
        }
        talkPopup.showAtLocation(findViewById(R.id.submit_tv), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    @Override
    public void talkItemClick(Talk talk) {
        String name = talk.name;
        insertTalk2Content(name);

        Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
        if (talkPopup != null) {
            talkPopup.dismiss();
        }

    }


    private void insertTalk2Content(String name) {
        contentEt.requestFocus();
        int selectionStart = contentEt.getSelectionStart();

        Editable editable = contentEt.getText();
        editable.insert(selectionStart, LEFT_SPACE + name + RIGHT_SPACE);


    }

    private void changeText(String text) {

        SpannableString spannableString = new SpannableString(text);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.BLUE);
        int lastIndex = 0;
        //遍历字符窜长度
        for (int i = 0; i < text.length(); i++) {
            //先看左边界
            int leftIndex = text.indexOf(LEFT_SPACE, lastIndex);
            if (leftIndex != -1) {
                //再看有边界
                int rightIndex = text.indexOf(RIGHT_SPACE, leftIndex);
                //存在话题
                if (rightIndex != -1) {
                    //保存，下一次使用
                    lastIndex = rightIndex;
                    spannableString.setSpan(foregroundColorSpan, leftIndex, rightIndex + 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        contentEt.setText(spannableString);
    }

}
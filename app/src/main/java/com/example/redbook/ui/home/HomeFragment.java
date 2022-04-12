package com.example.redbook.ui.home;

import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.example.redbook.databinding.FragmentHomeBinding;
import com.example.redbook.db.RedBookDataBase;
import com.example.redbook.db.entity.Diary;
import com.example.redbook.model.Weather;
import com.example.redbook.ui.detail.DetailActivity;
import com.example.redbook.ui.search.SearchActivity;
import com.example.redbook.ui.store.StoreAdapter;
import com.example.redbook.utils.GridItemDecoration;
import com.example.redbook.viewModel.RedBookViewModel;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPInputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeFragment extends Fragment implements StoreAdapter.OnItemClickListener {

    private static final String TAG = "HomeFragment";
    private RedBookViewModel redViewModel;
    private FragmentHomeBinding binding;
    private StoreAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        redViewModel =
                new ViewModelProvider(getActivity()).get(RedBookViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        initData();
        Toast.makeText(getActivity(), "city", Toast.LENGTH_SHORT).show();
        binding.topNav.rightIv.setOnClickListener(v -> search());
        RecyclerView recyclerView = binding.list;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new GridItemDecoration(10));
        int screenWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();

        adapter = new StoreAdapter(getContext(), screenWidth);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);

        return root;
    }

    private void search() {
        Intent intent = new Intent(getContext(), SearchActivity.class);
        startActivity(intent);
    }

    private void initData() {
        redViewModel.getAddressLiveData().observe(getViewLifecycleOwner(), new Observer<Address>() {
            @Override
            public void onChanged(Address address) {
                String city = address.getLocality();
                if (city!=null&&city.endsWith("市")) {
                    city = city.substring(0, city.length() - 1);//截取字符串（0，3）表示0，1，2，3（含头不含尾即0，1，2三个）
                }
                city = "上海";
                String url = "http://wthrcdn.etouch.cn/weather_mini?city=" + city;
                Log.e(TAG, url);
                getAsyn(url);
            }
        });


    }


    public void getAsyn(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String s = e.toString();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();//getJsonStringFromGZIP(response);// 获取到解压缩之后的字符串
                    Weather weather = new Weather(result);
                    Log.e(TAG, JSON.toJSONString(weather));

                    String wendu = "当前气温：" + weather.wendu + "℃ ," + weather.ganmao;

                    getActivity().runOnUiThread(() -> {
                        binding.wendu.setText(wendu);
                        binding.wendu.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                        binding.wendu.setSingleLine(true);
                        binding.wendu.setSelected(true);
                        binding.wendu.setFocusable(true);
                        binding.wendu.setFocusableInTouchMode(true);

                        getDiary(weather.wendu);
                    });

                }
            }
        });
    }


    private void getDiary(String wendu) {
        RedBookDataBase.getRedBookDataBaseInstance(getContext()).getDiaryDao().getAllDiary().observe(getViewLifecycleOwner(), new Observer<List<Diary>>() {
            @Override
            public void onChanged(List<Diary> list) {
                //判断温度
                getRightDiary(list, wendu);
            }
        });
    }

    private String getSeason() {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        int currentMonth = instance.get(Calendar.MONTH) + 1;
        if (currentMonth >= 3 && currentMonth <= 5) {
            return "春";
        } else if (currentMonth > 5 && currentMonth <= 8) {
            return "夏";
        } else if (currentMonth > 8 && currentMonth <= 11) {
            return "秋";
        } else {
            return "冬";
        }
    }

    private void getRightDiary(List<Diary> list, String temperature) {

        List<Diary> rightList = new ArrayList<>();

        String season = getSeason();

        for (Diary diary : list) {
            String talks = diary.talk;
            String[] split = talks.split("\\|");
            for (int i = 0; i < split.length; i++) {
                String talk = split[i].trim();
                if (talk.endsWith("℃")) {
                    String replace = talk.replace("℃", "");
                    if (replace.contains("-")) {
                        String[] nums = replace.split("-");
                        if (nums.length == 2) {
                            try {
                                int low = Integer.parseInt(nums[0]);
                                int high = Integer.parseInt(nums[1]);
                                int nowTemp = Integer.parseInt(temperature);
                                if (nowTemp >= low && nowTemp <= high) {
                                    rightList.add(diary);
                                    break;
                                }
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }  else if (talk.equals(season)) {
                    rightList.add(diary);
                    break;
                }
            }
        }

        if (rightList.size() > 0) {
            Collections.shuffle(rightList);
            adapter.setData(rightList);
        }

    }
    private static String convertStreamToString(InputStream is) {
        StringBuilder sb1 = new StringBuilder();
        byte[] bytes = new byte[4096];
        int size;

        try {
            while ((size = is.read(bytes)) > 0) {
                String str = new String(bytes, 0, size, "UTF-8");
                sb1.append(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb1.toString();
    }
    private String getJsonStringFromGZIP(Response response) {
        String jsonString = null;
        try {
            InputStream is = response.body().byteStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bis.mark(2);
            // 取前两个字节
            byte[] header = new byte[2];
            int result = bis.read(header);
            // reset输入流到开始位置
            bis.reset();
            // 判断是否是GZIP格式
            int headerData = getShort(header);
            if (result != -1 && headerData == 0x1f8b) {
                is = new GZIPInputStream(bis);
            } else {
                is = bis;
            }
            InputStreamReader reader = new InputStreamReader(is, "utf-8");
            char[] data = new char[100];
            int readSize;
            StringBuffer sb = new StringBuffer();
            while ((readSize = reader.read(data)) > 0) {
                sb.append(data, 0, readSize);
            }
            jsonString = sb.toString();
            bis.close();
            reader.close();
        } catch (Exception e) {
            Log.e("HttpTask", e.toString(), e);
        }
        return jsonString;
    }
    private int getShort(byte[] data) {
        return (int) ((data[0] << 8) | data[1] & 0xFF);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void itemClick(Diary diary) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(DetailActivity.KEY_DIARY, diary);
        intent.putExtras(bundle);
        startActivity(intent);
    }


}
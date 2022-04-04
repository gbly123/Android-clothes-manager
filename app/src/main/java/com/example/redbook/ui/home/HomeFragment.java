package com.example.redbook.ui.home;

import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.example.redbook.ui.store.StoreAdapter;
import com.example.redbook.utils.GridItemDecoration;
import com.example.redbook.viewModel.RedBookViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

        return root;
    }

    private void initData() {

        redViewModel.getAddressLiveData().observe(getViewLifecycleOwner(), new Observer<Address>() {
            @Override
            public void onChanged(Address address) {
                String city = address.getLocality();
                if (city.endsWith("市")) {
                    city = city.substring(0, city.length() - 1);
                }
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
                    String result = response.body().string();
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
        RecyclerView recyclerView = binding.list;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new GridItemDecoration(10));
        int screenWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();

        adapter = new StoreAdapter(getContext(), screenWidth);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);

        RedBookDataBase.getRedBookDataBaseInstance(getContext()).getDiaryDao().getAllDiary().observe(getViewLifecycleOwner(), new Observer<List<Diary>>() {
            @Override
            public void onChanged(List<Diary> list) {
                //判断温度
                getRightDiary(list, wendu);
            }
        });
    }

    private void getRightDiary(List<Diary> list, String temperature) {

        List<Diary> rightList = new ArrayList<>();

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
                                }
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }

        adapter.setData(rightList);
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
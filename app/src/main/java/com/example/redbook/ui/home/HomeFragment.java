package com.example.redbook.ui.home;

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

import com.alibaba.fastjson.JSON;
import com.example.redbook.databinding.FragmentHomeBinding;
import com.example.redbook.model.Weather;
import com.example.redbook.viewModel.RedBookViewModel;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private RedBookViewModel redViewModel;
    private FragmentHomeBinding binding;

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
                    });

                }
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
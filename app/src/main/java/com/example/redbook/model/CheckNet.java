package com.example.redbook.model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 作者： lijinche
 * 时间： 2022.4.11
 * 描述：
 */
public class CheckNet {
    public final static int NET_NONE = 0;
    public final static int NET_WIFI = 1;
    public final static int NET_MOBILE = 2;
    public static int getNetState(Context context)
    {

        ConnectivityManager connectivityManager =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo == null)
            return NET_NONE;
        int type = networkInfo.getType();
        if(type == ConnectivityManager.TYPE_MOBILE)
            return NET_MOBILE;
        else if(type == ConnectivityManager.TYPE_WIFI)
            return NET_WIFI;
        return NET_MOBILE;
    }

}

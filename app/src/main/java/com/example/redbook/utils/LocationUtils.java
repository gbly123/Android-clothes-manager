package com.example.redbook.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.util.List;
import java.util.Locale;

public class LocationUtils {

    private final Context context;

    public LocationUtils(Context context) {
        this.context = context;
    }


    private List<Address> getAddress(Location location) {
        List<Address> result = null;
        try {
            if (location != null) {
                Geocoder gc = new Geocoder(context, Locale.getDefault());
                result = gc.getFromLocation(location.getLatitude(),
                        location.getLongitude(), 1);
                if (onAddressCallBack != null && result != null && result.size() > 0) {
                    onAddressCallBack.addressCallback(result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public void checkPermission() {

        final String[] strings = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        new CheckPermission(context).requestPermission(strings, new CheckPermission.PermissionLinstener() {
            @Override
            public void onSuccess(Context context, List<String> data) {
                getLocation();
            }

            @Override
            public void onFailed(Context context, List<String> data) {
                Toast.makeText(context, "申请失败", Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onNotApply(final Context context, List<String> data) {
                Toast.makeText(context, "用户点击了不再提示", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void getLocation() {
        String locationProvider;
        //获取地理位置管理器
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //获取所有可用的位置提供器
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else {
            Toast.makeText(context, "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
            return;
        }
        //获取Location
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationProvider);
        if (location != null) {
            //不为空,显示地理位置经纬度
            getAddress(location);
        }
        //监视地理位置变化
//        locationManager.requestLocationUpdates(locationProvider, 3000, 1, locationListener);
    }


    /**
     * LocationListern监听器
     * 参数：地理位置提供器、监听位置变化的时间间隔、位置变化的距离间隔、LocationListener监听器
     */

    LocationListener locationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onLocationChanged(Location location) {

        }
    };

    private OnAddressCallBack onAddressCallBack;

    public void setAddressListener(OnAddressCallBack onAddressCallBack) {
        this.onAddressCallBack = onAddressCallBack;
    }

    public interface OnAddressCallBack {
        void addressCallback(List<Address> result);
    }

}

package com.example.redbook.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import java.util.List;

public class CheckPermission {
    private Context context;
    private static CheckPermission checkPermission;
    private static final String PACKAGE_HEADER = "package:";


    public CheckPermission(Context context) {
        this.context = context;
    }

    /**
     * 获取权限
     *
     * @param listener
     * @param permission
     */
    @SuppressLint("WrongConstant")
    public void requestPermission(String[] permission, final PermissionLinstener listener) {
        AndPermission.with(context)
                .runtime()
                .permission(permission)
//                    .rationale(context)//添加拒绝权限回调
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        // data.get(0);
                        Log.d("permission", data.get(0));
                        listener.onSuccess(context, data);
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        /**
                         * 当用户没有允许该权限时，回调该方法
                         */
                        listener.onFailed(context, data);
                        /**
                         * 判断用户是否点击了禁止后不再询问，AndPermission.hasAlwaysDeniedPermission(MainActivity.this, data)
                         * 如要引导用户跳转至应该权限设置页面，可用intent
                         */
                        if (AndPermission.hasAlwaysDeniedPermission(context, data)) {
                            //true，弹窗再次向用户索取权限
                            listener.onNotApply(context, data);
                        }
                    }
                }).start();
    }

    /**
     * 引导用户到系统设置页面
     */
    public void GuidePermission() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE_HEADER + context.getPackageName()));
        context.startActivity(intent);

    }

    public interface PermissionLinstener {
        void onSuccess(Context context, List<String> data);

        void onFailed(Context context, List<String> data);

        void onNotApply(Context context, List<String> data);
    }
}

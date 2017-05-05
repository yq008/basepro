package com.yq008.basepro.applib.util.permission;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.yanzhenjie.permission.PermissionListener;
import com.yq008.basepro.applib.AppActivity;

import java.util.List;

import static com.yq008.basepro.App.context;

/**
 * 权限回调
 * Created by Xiay on 2017/5/5.
 */

public abstract class PermissionCallback implements PermissionListener {

    public Activity activity;
    public Fragment fragment;

    public PermissionCallback(AppActivity activity) {
        this.activity = activity;
    }
    public PermissionCallback(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
        if (context instanceof AppActivity)
        PermissionBase.showNoPermissionDialog(activity!=null?activity:fragment.getActivity(),requestCode,deniedPermissions);
    }
}

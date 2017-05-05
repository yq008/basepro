package com.yq008.basepro.applib.util.permission;

import android.Manifest;
import android.app.Activity;
import android.support.v4.app.Fragment;

import com.yanzhenjie.permission.PermissionListener;

/**
 * 获取相机权限
 * */
public  class PermissionCamera extends PermissionBase {
    public static final   int PERMISSION_CODE=1004;
    public PermissionCamera(Activity activity,PermissionListener permissionListener){
        super(activity,permissionListener);
    }
    public PermissionCamera(Fragment fragment,PermissionListener permissionListener){
        super(fragment,permissionListener);
    }
    public  String[] getPermissions(){
        return new String[]{Manifest.permission.CAMERA};
    }

    @Override
    public int getPermissCode() {
        return PERMISSION_CODE;
    }

}

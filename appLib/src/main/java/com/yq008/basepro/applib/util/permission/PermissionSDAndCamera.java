package com.yq008.basepro.applib.util.permission;

import android.Manifest;
import android.app.Activity;
import android.support.v4.app.Fragment;

import com.yanzhenjie.permission.PermissionListener;

/**
 * 获取SD卡和相机权限
 * */
public  class PermissionSDAndCamera extends PermissionBase {
    public static final   int PERMISSION_CODE=1002;
    public PermissionSDAndCamera(Activity activity,PermissionListener permissionListener){
        super(activity,permissionListener);
    }
    public PermissionSDAndCamera(Fragment fragment,PermissionListener permissionListener){
        super(fragment,permissionListener);
    }
    public  String[] getPermissions(){
        return new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA};
    }
    @Override
    public int getPermissCode() {
        return PERMISSION_CODE;
    }

}

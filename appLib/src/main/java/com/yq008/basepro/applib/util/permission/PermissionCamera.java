package com.yq008.basepro.applib.util.permission;

import android.Manifest;
import android.app.Activity;
import android.support.v4.app.Fragment;

/**
 * 获取相机权限
 * */
public  class PermissionCamera extends PermissionBase {
    public static final   int PERMISSION_CODE=1004;
    public PermissionCamera(Activity activity){
        super(activity);
    }
    public PermissionCamera(Fragment fragment){
        super(fragment);
    }
    public  String[] getPermissions(){
        return new String[]{Manifest.permission.CAMERA};
    }

    @Override
    public int getPermissCode() {
        return PERMISSION_CODE;
    }

}

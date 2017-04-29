package com.yq008.basepro.applib.util.permission;

import android.Manifest;
import android.app.Activity;
import android.support.v4.app.Fragment;

/**
 * 获取SD卡权限
 * */
public  class PermissionSD extends PermissionBase {
    public static final   int PERMISSION_CODE=1000;
    public PermissionSD(Activity activity){
        super(activity);
    }
    public PermissionSD(Fragment fragment){
        super(fragment);
    }
    public  String[] getPermissions(){
        return new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }

    @Override
    public int getPermissCode() {
        return PERMISSION_CODE;
    }

}

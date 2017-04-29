package com.yq008.basepro.applib.util.permission;

import android.Manifest;
import android.app.Activity;
import android.support.v4.app.Fragment;

/**
 * 获取拨打电话权限
 * */
public  class PermissionCallPhone extends PermissionBase {
    public static final   int PERMISSION_CODE=1003;
    public PermissionCallPhone(Activity activity){
        super(activity);
    }
    public PermissionCallPhone(Fragment fragment){
        super(fragment);
    }
    public  String[] getPermissions(){
        return new String[]{Manifest.permission.CALL_PHONE};
    }

    @Override
    public int getPermissCode() {
        return PERMISSION_CODE;
    }

}

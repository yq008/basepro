package com.yq008.basepro.applib.util.permission;

import android.Manifest;
import android.app.Activity;
import android.support.v4.app.Fragment;

import com.yanzhenjie.permission.PermissionListener;

/**
 * 获取定位权限
 * */
public  class PermissionLocation extends PermissionBase {
    public static final   int PERMISSION_CODE=1001;
    public PermissionLocation(Activity activity,PermissionListener permissionListener){
        super(activity,permissionListener);
    }
    public PermissionLocation(Fragment fragment,PermissionListener permissionListener){
        super(fragment,permissionListener);
    }
    public  String[] getPermissions(){
        return new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
    }

    @Override
    public int getPermissCode() {
        return PERMISSION_CODE;
    }

}

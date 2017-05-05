package com.yq008.basepro.applib.util.permission;

import android.Manifest;
import android.app.Activity;
import android.support.v4.app.Fragment;

import com.yanzhenjie.permission.PermissionListener;

/**
 * 获取录音权限
 * */
public  class PermissionRecordAudio extends PermissionBase {
    public static final   int PERMISSION_CODE=1005;
    public PermissionRecordAudio(Activity activity,PermissionListener permissionListener){
        super(activity,permissionListener);
    }
    public PermissionRecordAudio(Fragment fragment,PermissionListener permissionListener){
        super(fragment,permissionListener);
    }
    public  String[] getPermissions(){
        return new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO};
    }

    @Override
    public int getPermissCode() {
        return PERMISSION_CODE;
    }

}

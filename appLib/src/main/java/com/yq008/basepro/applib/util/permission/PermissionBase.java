package com.yq008.basepro.applib.util.permission;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;
import com.yanzhenjie.permission.RationaleRequest;
import com.yq008.basepro.applib.R;
import com.yq008.basepro.widget.Toast;

import java.util.List;
import java.util.Locale;


public abstract class PermissionBase {

    public Activity act;
    public Fragment fragment;
    PermissionListener permissionListener;
    int requestCode;
    public PermissionBase(Activity act,PermissionListener permissionListener) {
        this.act = act;
        this.permissionListener = permissionListener;
        requestPermission(getPermissCode(),getPermissions());
    }

    public PermissionBase(Fragment fragment,PermissionListener permissionListener) {
        this.fragment = fragment;
        this.permissionListener = permissionListener;
        requestPermission(getPermissCode(),getPermissions());
    }

    /**
     * 申请SD卡权限，单个的。
     */
    public void requestPermission(int requestCode, String... permissions) {
        RationaleRequest permission;
        this.requestCode = requestCode;
        if (act != null) {
            permission = AndPermission.with(act);
        } else {
            permission = AndPermission.with(fragment);
        }
        permission.requestCode(requestCode)
                .permission(permissions)
                .rationale(rationaleListener)
                .callback(permissionListener)
                .start();
    }

    //
//    public static void showNoPermissionDialog(final Activity act, String permissionName, String... permissions) {
//        List<String> deniedPermissions=new ArrayList<>();
//        for (int i = 0; i <permissions.length ; i++) {
//            deniedPermissions.add(permissions[i]);
//        }
//        if (AndPermission.hasAlwaysDeniedPermission(act, deniedPermissions)) {
//            Toast.makeText(act, "获取" + permissionName + "权限失败", Toast.LENGTH_SHORT).show();
//        } else {
//            // 第一种：用默认的提示语。
//            AndPermission.defaultSettingDialog(act, 0).show();
////            new AlertDialog.Builder(act)
////                    .setTitle("友好提醒")
////                    .setMessage("您已拒绝了" + permissionName + "权限，并且下次不再提示，如果要使用此功能，请在设置中为授权" + permissionName + "权限！")
////                    .setPositiveButton("好，去设置", new DialogInterface.OnClickListener() {
////                        @Override
////                        public void onClick(DialogInterface dialog, int which) {
////                            openAppDetailSettingIntent(act);
////                        }
////                    })
////                    .setNegativeButton("再次拒绝", new DialogInterface.OnClickListener() {
////                        @Override
////                        public void onClick(DialogInterface dialog, int which) {
////                            dialog.cancel();
////                        }
////                    }).show();
//        }
//
//    }
    public static void showNoPermissionDialog(final Activity act, int requestCode, List<String> deniedPermissions) {
        switch (requestCode) {
            case PermissionSD.PERMISSION_CODE: {
                Toast.show(String.format(Locale.CHINA, act.getString(R.string.permission_fail), "SD卡"));
                break;
            }
            case PermissionSDAndCamera.PERMISSION_CODE: {
                Toast.show(String.format(Locale.CHINA, act.getString(R.string.permission_fail), "SD卡和相机"));
                break;
            }
            case PermissionCamera.PERMISSION_CODE: {
                Toast.show(String.format(Locale.CHINA, act.getString(R.string.permission_fail), "相机"));
                break;
            }
            case PermissionCallPhone.PERMISSION_CODE: {
                Toast.show(String.format(Locale.CHINA, act.getString(R.string.permission_fail), "拨打电话"));
                break;
            }
            case PermissionRecordAudio.PERMISSION_CODE: {
                Toast.show(String.format(Locale.CHINA, act.getString(R.string.permission_fail), "录音"));
                break;
            }
        }
        // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
        if (AndPermission.hasAlwaysDeniedPermission(act, deniedPermissions)) {
            // 第一种：用默认的提示语。
            AndPermission.defaultSettingDialog(act, requestCode).show();
            // 第二种：用自定义的提示语。
//             AndPermission.defaultSettingDialog(this, REQUEST_CODE_SETTING)
//                     .setTitle("权限申请失败")
//                     .setMessage("我们需要的一些权限被您拒绝或者系统发生错误申请失败，请您到设置页面手动授权，否则功能无法正常使用！")
//                     .setPositiveButton("好，去设置")
//                     .show();

//            第三种：自定义dialog样式。
//            SettingService settingHandle = AndPermission.defineSettingDialog(this, REQUEST_CODE_SETTING);
//            你的dialog点击了确定调用：
//            settingHandle.execute();
//            你的dialog点击了取消调用：
//            settingHandle.cancel();
        }


    }

    private RationaleListener rationaleListener = new RationaleListener() {
        @Override
        public void showRequestPermissionRationale(int requestCode, final Rationale rationale) {
            AndPermission.rationaleDialog(act == null ? fragment.getActivity() : act, rationale).show();
//            new AlertDialog.Builder(act)
//                    .setTitle("友好提醒")
//                    .setMessage("您已拒绝过访问" + PERMISSION_NICE_NAME + "权限，如果要使用此功能，请把权限赐给我吧！")
//                    .setPositiveButton("好，给你", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.cancel();
//                            rationale.resume();
//                        }
//                    })
//                    .setNegativeButton("我拒绝", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.cancel();
//                            rationale.cancel();
//                        }
//                    }).show();
        }
    };
    public abstract String[] getPermissions();
    public abstract int getPermissCode();
}

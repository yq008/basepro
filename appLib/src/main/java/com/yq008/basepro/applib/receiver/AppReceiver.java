package com.yq008.basepro.applib.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yq008.basepro.applib.util.AppUtil;

import com.yq008.basepro.widget.Toast;
import com.yq008.basepro.util.AppActivityManager;
import com.yq008.basepro.util.AppHelper;

public class AppReceiver extends BroadcastReceiver {
    public static String INSTALL ="install";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (INSTALL.equals(intent.getAction())){
            AppHelper.getInstance().installApk(context,intent.getStringExtra("filePath"));
            if (AppUtil.isForceUpdate){
                AppActivityManager.getInstance().removeAllActivity();
                Toast.show("请安装最新版");
            }
        }
    }
}

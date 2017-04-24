package com.yq008.basepro.applib.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.yq008.basepro.widget.Toast;

/**
 * Created by Xiay on 2016/4/27.
 */
public class AppNetBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = manager.getActiveNetworkInfo();
        if (activeInfo==null){
            Toast.show("网络已断开");
          //  appNetListener.onNetChangeListener(false);
        }else {
            Toast.show("网络已连接");
        }
    }
}

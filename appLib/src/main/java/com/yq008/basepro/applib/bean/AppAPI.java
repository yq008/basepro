package com.yq008.basepro.applib.bean;

import android.content.Context;

import com.yq008.basepro.applib.AppActivity;
import com.yq008.basepro.applib.listener.HttpCallBack;
import com.yq008.basepro.http.extra.request.ParamsString;
import com.yq008.basepro.util.SPUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/4/28.
 */

public class AppAPI {
/**
 * 安装统计
 * @param ctx
 */

    public static void countInstall(Context ctx){
        AppActivity appActivity=(AppActivity)ctx;
        countInstall(ctx,new ParamsString("installCount"));
    }
/**
 * 安装统计
 * @param ctx
 */
    public static void countInstall(Context ctx,ParamsString params){
        boolean isInstall= SPUtil.getInstance().getBoolean("isAppIntall");
        if (!isInstall){
            AppActivity appActivity=(AppActivity)ctx;
            appActivity.sendStringPost(params,new HttpCallBack<String>(){
                @Override
                public void onSucceed(int what, String response) {
                    try {
                        JSONObject jObj=new JSONObject(response);
                        if (jObj.getInt("status")==1)
                            SPUtil.getInstance().saveBoolean("isAppIntall",true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}

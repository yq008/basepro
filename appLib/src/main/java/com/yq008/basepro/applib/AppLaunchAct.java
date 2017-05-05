package com.yq008.basepro.applib;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.yq008.basepro.applib.bean.DataGuidePic;
import com.yq008.basepro.applib.db.bean.TBGuide;
import com.yq008.basepro.applib.db.bean.TBGuidePic;
import com.yq008.basepro.applib.db.dao.GuideDao;
import com.yq008.basepro.applib.db.dao.GuidePicDao;
import com.yq008.basepro.applib.listener.HttpCallBack;
import com.yq008.basepro.applib.service.AppDownPicService;
import com.yq008.basepro.http.extra.EncryptHelper;
import com.yq008.basepro.http.extra.request.MyJsonObject;
import com.yq008.basepro.http.extra.request.ParamsString;
import com.yq008.basepro.util.AppHelper;
import com.yq008.basepro.util.rxjava.RxUtil;
import com.yq008.basepro.util.rxjava.bean.RxIOTask;
import com.yq008.basepro.util.rxjava.bean.RxIOUITask;
import com.yq008.basepro.widget.dialog.DialogClickListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * 启动页面
 *
 * @author Xiay
 */
public abstract class AppLaunchAct extends AppActivity {
    /**是否使用网络图片*/
    private   boolean isUseUrlGuidePic=false;
    /**是否使用网络图片*/
    public void setUseUrlGuidePicEnable(boolean useUrlGuidePic) {
        isUseUrlGuidePic = useUrlGuidePic;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxUtil.executeRxTaskInIO(new RxIOUITask<Boolean>() {
            @Override
            public void doInIOThread() {
                if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {//避免华为手机返回桌面后重启应用
                    finish();
                    return;
                }
                setConfigUrl();
                EncryptHelper.getInstance().setPassword(getRequestPassword());
                setValue(AppHelper.getInstance().isNetworkConnected());
            }

            @Override
            public void doInUIThread() {
                if (!getValue()) {
                    getDialog().showCancle("请先连接网络后来哦~").setOnClickView(R.id.btn_mid, new DialogClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(0);
                        }
                    }).setCancelable(false);
                } else {
                    if (isUseUrlGuidePic){
                        checkGuidePic();
                    }else {
                        GuideDao guideDao=new GuideDao();
                        TBGuide dbGuide = guideDao.queryForFirst();
                        if (dbGuide == null) {//如果是第一次启动
                            dbGuide = new TBGuide();
                            dbGuide.isFirstLunch = false;
                            guideDao.save(dbGuide);//保存最新数据
                            isShowGuideWithApp = true;
                        }
                        isShowGuide(isShowGuideWithApp, isShowGuideWithSDPic);
                    }

                }
            }
        });
    }
    /**如果设置加密密码，必须是16位字符串，不开启加密，返回null即可*/
    protected abstract String getRequestPassword();

    boolean isShowGuideWithApp, isShowGuideWithSDPic;
    public void checkGuidePic() {
        sendJsonObjectPost(getAppGuideParams(), new HttpCallBack<MyJsonObject>() {
            @Override
            public void onSucceed(int what, final MyJsonObject responseData) {
                RxUtil.doInIOThread(new RxIOTask() {
                    @Override
                    public void doInIOThread() {
                        try {
                            GuideDao guideDao=new GuideDao();
                            TBGuide dbGuide = guideDao.queryForFirst();
                            if (responseData.isSuccess()) {
                                JSONArray jArr = responseData.getJsonDataArray();
                                if (dbGuide == null) {//如果是第一次启动
                                    dbGuide = new TBGuide();
                                    dbGuide.oldData = jArr.toString();
                                    dbGuide.isFirstLunch = false;
                                    guideDao.save(dbGuide);//保存最新数据
                                    isShowGuideWithApp = true;
                                    startDownPic(jArr, null);
                                } else if (!jArr.toString().equals(dbGuide.oldData)) {//如果服务器数据和本地数据不同相同下载图片，等图片全部下载成功后oldData改成newData,避免下载失败后无法重新下载问题
                                    dbGuide.newData = jArr.toString();
                                    guideDao.save(dbGuide);
                                    GuidePicDao guidePicDao=new GuidePicDao();
                                    List<TBGuidePic> dbGuidePics =new GuidePicDao().queryAllData();
                                    if (dbGuidePics.size() > 0) {//表示服务器修改了引导图，需重新下载
                                        startDownPic(jArr, guidePicDao);
                                    }
                                }else if (dbGuide.isShowNewGuide){
                                    isShowGuideWithSDPic=true;
                                    dbGuide.isShowNewGuide=false;
                                    guideDao.save(dbGuide);
                                }
                            }else {//请求接口返回失败
                                if (dbGuide == null) {//如果是第一次启动
                                    isShowGuideWithApp = true;
                                    guideDao.save(new TBGuide());//保存最新数据
                                }
                            }
                            isShowGuide(isShowGuideWithApp, isShowGuideWithSDPic);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailed(int what, MyJsonObject responseData) {
                isShowGuide(isShowGuideWithApp, isShowGuideWithSDPic);
            }
        });
    }
    /**可由子类覆写此方法*/
    public ParamsString getAppGuideParams() {
        return  new ParamsString("getAppGuide");
    }

    /**
     * 下载图片
     */
    private void startDownPic(JSONArray jArr, GuidePicDao guidePicDao) throws JSONException {
        if (jArr.length() > 0) {
            DataGuidePic guidePic = new DataGuidePic();
            ArrayList<TBGuidePic> guidePics = new ArrayList<>();
            for (int i = 0; i < jArr.length(); i++) {
                TBGuidePic dbGuidePic = new TBGuidePic(jArr.getJSONObject(i).getString("url"));
                guidePics.add(dbGuidePic);
                guidePic.pics.add(jArr.getJSONObject(i).getString("url"));
            }
            if (guidePicDao != null) {
                guidePicDao.deleteAll();
                guidePicDao.insert(guidePics);
            }
            Intent i = new Intent(getApplicationContext(), AppDownPicService.class);
            i.putExtra(AppDownPicService.GUIDE_PICS, guidePic);
            startService(i);
        }
    }

    /**
     * 是否第一次启动
     *
     * @param isShowGuideWithApp  //是否显示app默认图片
     * @param isShowGuideWithSDPic //是否显示下载好的SD卡中的图片
     */

    protected abstract void isShowGuide(boolean isShowGuideWithApp, boolean isShowGuideWithSDPic);

    /**
     * 设置请求的Url地址
     */
    protected abstract void setConfigUrl();

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isAddBackButton() {
        return false;
    }
}

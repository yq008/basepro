/*************************************************************************************************
 * 版权所有 (C)2012,  深圳市康佳集团股份有限公司
 * <p>
 * 文件名称：AppUpdateService.java
 * 内容摘要：升级服务
 * 当前版本：
 * 作         者： hexiaoming
 * 完成日期：2012-12-24
 * 修改记录：
 * 修改日期：
 * 版   本  号：
 * 修   改  人：
 * 修改内容：
 ************************************************************************************************/
package com.yq008.basepro.applib.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.yq008.basepro.applib.R;
import com.yq008.basepro.applib.bean.DataGuidePic;
import com.yq008.basepro.applib.db.bean.TBGuide;
import com.yq008.basepro.applib.db.bean.TBGuidePic;
import com.yq008.basepro.applib.db.dao.GuideDao;
import com.yq008.basepro.applib.db.dao.GuidePicDao;
import com.yq008.basepro.http.Headers;
import com.yq008.basepro.http.Http;
import com.yq008.basepro.http.download.DownloadListener;
import com.yq008.basepro.http.download.DownloadRequest;
import com.yq008.basepro.http.error.NetworkError;
import com.yq008.basepro.http.error.ServerError;
import com.yq008.basepro.http.error.StorageReadWriteError;
import com.yq008.basepro.http.error.StorageSpaceNotEnoughError;
import com.yq008.basepro.http.error.TimeoutError;
import com.yq008.basepro.http.error.URLError;
import com.yq008.basepro.http.error.UnKnownHostError;
import com.yq008.basepro.http.extra.HttpHelper;
import com.yq008.basepro.util.AppHelper;
import com.yq008.basepro.util.log.Log;
import com.yq008.basepro.util.rxjava.RxUtil;
import com.yq008.basepro.util.rxjava.bean.RxIOTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
/*##################################################
                       _ooOoo_
                      o8888888o
                      88" . "88
                      (| -_- |)
                      O\  =  /O
                   ____/`---'\____
                 .'  \\|     |//  `.
                /  \\|||  :  |||//  \
               /  _||||| -:- |||||-  \
               |   | \\\  -  /// |   |
               | \_|  ''\---/''  |   |
               \  .-\__  `-`  ___/-. /
             ___`. .'  /--.--\  `. . __
          ."" '<  `.___\_<|>_/___.'  >'"".
         | | :  `- \`.;`\ _ /`;.`/ - ` : | |
         \  \ `-.   \_ __\ /__ _/   .-` /  /
    ======`-.____`-.___\_____/___.-`____.-'======
                       `=---='
    ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
                佛祖保佑       永无BUG
#########################################################
*/

/***
 * 下载图片服务，成功之后返回Bitmap
 * @author xiay
 *
 */

public class AppDownPicService extends Service {
    /**同时下载多张图片*/
    public static String GUIDE_PICS = "GUIDE_PICS";
    public static String ON_PICS_DONE = "ON_PICS_DONE";
    private DataGuidePic guidePic;
    private ArrayList<String> donePics;
    /**下载出错的位置*/
    ArrayList<Integer> downErrorIndex = new ArrayList();
    int picCount;
    int downIndex;
    /**
     * 下载任务列表。
     */
    private List<DownloadRequest> mDownloadRequests = new ArrayList<>();

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        if (intent != null) {
            guidePic = intent.getParcelableExtra(GUIDE_PICS);
            if (guidePic != null) {
                RxUtil.doInIOThread(new RxIOTask() {
                    @Override
                    public void doInIOThread() {
                        donePics = new ArrayList<>();
                        String url, fileName , downPath = AppHelper.getInstance().getDataDirectory().getAbsolutePath();
                        picCount = guidePic.pics.size();
                        for (int i = 0; i < guidePic.pics.size(); i++) {
                            url = guidePic.pics.get(i);
                            fileName = url.substring(url.lastIndexOf("/") + 1);
                            DownloadRequest downloadRequest = Http.createDownloadRequest(url, downPath, fileName, false, false);
                            Log.i("xxxx url="+url);
                            mDownloadRequests.add(downloadRequest);
                        }
                        for (int i = 0; i < mDownloadRequests.size(); i++) {
                            HttpHelper.getDownloadInstance(1).add(i, mDownloadRequests.get(i), listener);
                        }
                    }
                });
            }

        }
        return super.onStartCommand(intent, flags, startId);
    }

    //	public  void loadImages() {
//		//图片缓存位置是在/data/data/包名/image_manager_disk_cache
//		Glide.with(getApplicationContext()).load(guidePic.pics.get(downIndex)).asBitmap().into(new SimpleTarget<Bitmap>(720,1280) {
//			@Override
//			public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
//				downIndex++;
//				Log.i("xxxx loadImageFinish:"+downIndex);
//				donePics.add(bitmap);
//				if (downIndex<picCount){
//					loadImages();
//				}else {
//					RxBus.get().post(ON_PICS_DONE,donePics);
//					stopSelf();
//				}
//			}
//			@Override
//			public void onLoadFailed(Exception e, Drawable errorDrawable) {
//				downIndex++;
//			}
//		});
//	}

    DownloadListener listener = new DownloadListener() {
        @Override
        public void onDownloadError(int what, Exception exception) {
            String message = getString(R.string.download_error);
            String messageContent;
            if (exception instanceof ServerError) {
                messageContent = getString(R.string.download_error_server);
            } else if (exception instanceof NetworkError) {
                messageContent = getString(R.string.download_error_network);
            } else if (exception instanceof StorageReadWriteError) {
                messageContent = getString(R.string.download_error_storage);
            } else if (exception instanceof StorageSpaceNotEnoughError) {
                messageContent = getString(R.string.download_error_space);
            } else if (exception instanceof TimeoutError) {
                messageContent = getString(R.string.download_error_timeout);
            } else if (exception instanceof UnKnownHostError) {
                messageContent = getString(R.string.download_error_un_know_host);
            } else if (exception instanceof URLError) {
                messageContent = getString(R.string.download_error_url);
            } else {
                messageContent = getString(R.string.error_unknow);
            }
            message = String.format(Locale.getDefault(), message, messageContent);
            Log.i("``` what:"+what+";请求出错:" + message);
            downErrorIndex.add(what);
            downIndex++;
            picCount--;
            //	stopSelf();
        }

        @Override
        public void onStart(int what, boolean isResume, long rangeSize, Headers responseHeaders, long allCount) {
        }

        @Override
        public void onProgress(int what, int progress, long fileCount, long speed) {

        }

        @Override
        public void onFinish(int what, String filePath) {
            Log.i("xxxx  downloadFinish what=" + what + "    filePath=" + filePath);
            donePics.add(filePath);
            downIndex++;
            if (donePics.size() == picCount) {//下载完成
                RxUtil.doInIOThread(new RxIOTask() {
                    @Override
                    public void doInIOThread() {
                        if (donePics.size() < guidePic.pics.size()) {//如果有下载失败
                            for (int i = 0; i < downErrorIndex.size(); i++) {
                                HttpHelper.getDownloadInstance(1).add(i, mDownloadRequests.get(downErrorIndex.get(i)), listener);
                            }
                        }else {//保存下载路径到数据库
                            GuidePicDao guidePicDao=new GuidePicDao();
                            guidePicDao.deleteAll();
                            guidePicDao.deleteAll();
                            ArrayList<TBGuidePic> dBGuidePics=new ArrayList<>();
                            for (String url: donePics) {
                                dBGuidePics.add(new TBGuidePic(url));
                            }
                            guidePicDao.insert(dBGuidePics);
                            //更新最新数据
                            GuideDao guideDao=new GuideDao();
                            TBGuide dbGuide = guideDao.queryForFirst();
                            if (dbGuide!=null){
                                if (null!=dbGuide.newData)
                                   dbGuide.oldData=dbGuide.newData;
                                dbGuide.isShowNewGuide=true;
                                guideDao.save(dbGuide);
                            }
                        }
                        Log.i("xxxx  allDownloadFinish="+(donePics.size() == picCount));
                        stopSelf();
                    }
                });
            }
        }
        @Override
        public void onCancel(int what) {
            stopSelf();
        }
    };
}
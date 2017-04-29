package com.yq008.basepro.applib;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.yq008.basepro.util.rxjava.RxManager;


public abstract class AppFragment extends Fragment {
    public boolean isLoadData;
    public AppActivity activity;
    private RxManager rxManager;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (AppActivity) context;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setPageTitle(setTitle());
        setPageHeadColor(setPageHeadColor());
//        if (isScaleView){
//           if(view instanceof ViewGroup)
//               ViewUtil.scaleContentView((ViewGroup) getView());
//            else
//               ViewUtil.scaleView(view);
//        }

    }

    public RxManager getRxManager() {
        if (rxManager == null)
            rxManager = new RxManager();
        return rxManager;
    }

    protected abstract String setTitle();

    protected abstract int setPageHeadColor();
    //protected  abstract void   getData();


    /**
     * 替换Fragment
     *
     * @param viewId
     * @param fragment
     * @param isAddToBack
     */
    public void replace(int viewId, Fragment fragment, boolean isAddToBack) {
        if (activity != null) {
            activity.replace(viewId, fragment, isAddToBack);
        }
    }

    /**
     * 替换Fragment
     *
     * @param viewId
     * @param fragment
     */
    public void replace(int viewId, Fragment fragment) {
        replace(viewId, fragment, false);
    }


    /**
     * 跳转activity,代工厂参数
     */
    public void openActivity(Class<?> cls) {
        activity.openActivity(cls);
    }

    /**
     * 跳转activity,代工厂参数
     */
    public void openActivityForResult(Class<?> cls, int requestCode) {
        activity.openActivityForResult(cls, requestCode);
    }

    /**
     * 跳转activity,代工厂参数
     */
    public void openActivityForResult(Intent intent, int requestCode) {
        activity.openActivityForResult(intent, requestCode);
    }

    /**
     * 跳转activity,代工厂参数
     */
    public void openActivity(Intent intent) {
        activity.openActivity(intent);
    }

    /**
     * 跳转activity
     *
     * @param clazz
     * @param parmas 传值格式  key,value
     */
    public void openActivity(Class clazz, Object... parmas) {
        activity.openActivity(clazz, parmas);
    }

    protected void setPageTitle(String title) {
        if (title != null) {
            View view = getView().findViewById(R.id.tv_pageHeadName);
            if (view != null)
                ((TextView) view).setText(title);
        }

    }

    protected void setPageHeadColor(int backgroundColorResources) {
        View rl_page_head = getView().findViewById(R.id.rl_page_head);
        if (rl_page_head != null)
            rl_page_head.setBackgroundColor(backgroundColorResources);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (rxManager != null) {
            rxManager.clear();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if ((isVisibleToUser && isResumed())) {
            onResume();
        } else if (!isVisibleToUser) {
            onPause();
        }
    }
}

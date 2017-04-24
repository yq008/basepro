package com.yq008.basepro.applib.listener;


import com.yq008.basepro.http.extra.request.BaseBean;
import com.yq008.basepro.applib.widget.dialog.MyToast;

public class HttpBaseBeanCallBack extends HttpCallBack<BaseBean> {
    @Override
    public void onSucceed(int what, BaseBean responseData) {
            if (responseData.isSuccess()){
                MyToast.showOk(responseData.msg);
                onSucceed(responseData);
            }else
                MyToast.showError(responseData.msg);

    }

    @Override
    public void onFailed(int what, BaseBean responseData) {
        super.onFailed(what, responseData);
    }

    public void onSucceed(BaseBean responseData) {}
    public void onFailed(BaseBean responseData) {}
}

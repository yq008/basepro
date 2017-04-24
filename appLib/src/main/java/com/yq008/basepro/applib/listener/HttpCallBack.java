package com.yq008.basepro.applib.listener;

import com.yq008.basepro.http.extra.HttpListener;
import com.yq008.basepro.http.rest.Response;


public abstract class HttpCallBack<T> implements HttpListener<T> {
    @Override
    public abstract void onSucceed(int what, T responseData);

    @Override
    public void onFailed(int what, Response<T> responseData) {
        onFailed(what,responseData.get());
    }
    public void onFailed(int what, T responseData) {
    }
}

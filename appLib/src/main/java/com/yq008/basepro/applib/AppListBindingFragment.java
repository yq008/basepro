package com.yq008.basepro.applib;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yq008.basepro.applib.widget.recyclerview.RecycleBindingHolder;
import com.yq008.basepro.applib.widget.recyclerview.RecyclerBaseAdapter;

/**
 * Created by Xiay on 2017/4/10.
 */

public abstract class AppListBindingFragment<LT,RQT, ADT,AD extends RecyclerBaseAdapter<ADT,RecycleBindingHolder>> extends AppListBaseFragment<RQT,ADT,RecycleBindingHolder,AD> {
    public LT binding;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewDataBinding viewBinding = DataBindingUtil.inflate(inflater, setContentView(), container, false);
        binding=(LT)viewBinding;
        return viewBinding.getRoot();
    }
    public abstract int setContentView();
}

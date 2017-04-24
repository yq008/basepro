/**
 * Copyright 2013 Joan Zapata
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY RecyclerViewHolderIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yq008.basepro.applib.widget.recyclerview;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.View;
import android.view.ViewGroup;

import com.yq008.basepro.applib.R;
import com.yq008.basepro.util.autolayout.utils.AutoUtils;

import java.util.List;


public abstract class RecyclerBindingAdapter<ADT> extends RecyclerBaseAdapter<ADT,RecycleBindingHolder> {

    public RecyclerBindingAdapter(int layoutResId, List<ADT> data) {
        super(layoutResId, data);
    }

    public RecyclerBindingAdapter(int layoutResId) {
        super(layoutResId);
    }

    public RecyclerBindingAdapter(List<ADT> data) {
        super(data);
    }
    public RecyclerBindingAdapter() {
        super();
    }
    @Override
    protected void convert(RecycleBindingHolder holder, ADT item) {
        ViewDataBinding binding = holder.getBinding();
        convert(holder,binding,item);
        binding.executePendingBindings();
    }
    protected abstract void convert(RecycleBindingHolder holder, ViewDataBinding binding, ADT item);
    @Override
    protected View getItemView(int layoutResId, ViewGroup parent) {
        ViewDataBinding binding = DataBindingUtil.inflate(mLayoutInflater, layoutResId, parent, false);
        if (binding == null) {
            return super.getItemView(layoutResId, parent);
        }
        View view = binding.getRoot();
        AutoUtils.autoSize(view);
        view.setTag(R.id.RecyclerBaseAdapter_databinding_support, binding);
        return view;
    }
}

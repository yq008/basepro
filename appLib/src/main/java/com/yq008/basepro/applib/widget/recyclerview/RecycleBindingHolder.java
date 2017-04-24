package com.yq008.basepro.applib.widget.recyclerview;

import android.databinding.ViewDataBinding;
import android.view.View;

import com.yq008.basepro.applib.R;

/**
 * Created by Xiay on 2017/4/10.
 */

public class RecycleBindingHolder extends RecyclerViewHolder{
    public RecycleBindingHolder(View view) {
        super(view);
    }
    public ViewDataBinding getBinding() {
        return (ViewDataBinding) getConvertView().getTag(R.id.RecyclerBaseAdapter_databinding_support);
    }

}

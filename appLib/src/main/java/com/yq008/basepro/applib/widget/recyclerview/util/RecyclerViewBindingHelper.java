package com.yq008.basepro.applib.widget.recyclerview.util;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yq008.basepro.applib.widget.recyclerview.RecycleBindingHolder;
import com.yq008.basepro.applib.widget.recyclerview.RecyclerBaseAdapter;
import com.yq008.basepro.applib.widget.recyclerview.RecyclerBindingAdapter;

/**
 * Created by Xiay on 2017/4/10.
 */

public class RecyclerViewBindingHelper< ADT,AD extends RecyclerBindingAdapter<ADT>> extends RecyclerViewBaseHelper<ADT,RecycleBindingHolder,AD>  {
    public RecyclerViewBindingHelper() {
    }
    public RecyclerViewBindingHelper(Activity activity) {
        super(activity);
    }
    public RecyclerViewBindingHelper(Fragment fragment) {
        super(fragment);
    }
    public RecyclerViewBindingHelper(View view) {
        super(view);
    }
    public RecyclerViewBindingHelper(RecyclerView rv_list, RecyclerBaseAdapter<ADT, RecycleBindingHolder> adapter) {
        super(rv_list, adapter);
    }

    public RecyclerViewBindingHelper(RecyclerView rv_list, RecyclerBaseAdapter<ADT, RecycleBindingHolder> adapter, int perPageSize, String emptyMessage) {
        super(rv_list, adapter, perPageSize, emptyMessage);
    }
}

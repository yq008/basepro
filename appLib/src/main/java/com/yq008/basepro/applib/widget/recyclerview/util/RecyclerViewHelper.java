package com.yq008.basepro.applib.widget.recyclerview.util;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

import com.yq008.basepro.applib.widget.recyclerview.RecyclerBaseAdapter;
import com.yq008.basepro.applib.widget.recyclerview.RecyclerAdapter;
import com.yq008.basepro.applib.widget.recyclerview.RecyclerViewHolder;

/**
 * Created by Xiay on 2017/4/10.
 */

public class RecyclerViewHelper< ADT,AD extends RecyclerAdapter<ADT>> extends RecyclerViewBaseHelper<ADT,RecyclerViewHolder,AD>  {
    public RecyclerViewHelper() {
        super();
    }
    public RecyclerViewHelper(Activity activity) {
        super(activity);
    }
    public RecyclerViewHelper(Fragment fragment) {
        super(fragment);
    }
    public RecyclerViewHelper(RecyclerView rv_list, RecyclerBaseAdapter<ADT, RecyclerViewHolder> adapter) {
        super(rv_list, adapter);
    }

    public RecyclerViewHelper(RecyclerView rv_list, RecyclerBaseAdapter<ADT, RecyclerViewHolder> adapter, int perPageSize, String emptyMessage) {
        super(rv_list, adapter, perPageSize, emptyMessage);
    }


}

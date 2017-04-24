package com.yq008.basepro.applib.widget.recyclerview.listener;

import android.view.View;

import com.yq008.basepro.applib.widget.recyclerview.RecyclerBaseAdapter;
import com.yq008.basepro.applib.widget.recyclerview.RecyclerAdapter;

/**
 * Created by Xiay on 2017/4/10.
 */
public abstract   class OnItemChildLongClickListener<ADT,AD> implements RecyclerAdapter.OnItemChildLongClickListener {
    @Override
    public void onItemChildLongClick(RecyclerBaseAdapter adapter, View view, int position) {
        onItemChildLongClick((AD)adapter,view,(ADT)adapter.getItem(position),position);
    }

    public abstract void onItemChildLongClick(AD adapter, View view, ADT item,int position) ;

}

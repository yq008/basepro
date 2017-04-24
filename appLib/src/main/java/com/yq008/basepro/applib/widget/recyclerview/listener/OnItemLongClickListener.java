package com.yq008.basepro.applib.widget.recyclerview.listener;

import android.view.View;

import com.yq008.basepro.applib.widget.recyclerview.RecyclerBaseAdapter;
import com.yq008.basepro.applib.widget.recyclerview.RecyclerAdapter;


/**
 * Created by Xiay on 2017/4/10.
 */
public abstract   class OnItemLongClickListener<ADT,AD> implements RecyclerAdapter.OnItemLongClickListener {
    @Override
    public boolean onItemLongClick(RecyclerBaseAdapter adapter, View view, int position) {
       return onItemLongClick((AD)adapter,view,(ADT)adapter.getItem(position),position);
    }

    public abstract boolean onItemLongClick(AD adapter, View view, ADT item,int position) ;

}

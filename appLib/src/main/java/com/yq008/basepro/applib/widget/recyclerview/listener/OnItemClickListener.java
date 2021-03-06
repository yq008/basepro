package com.yq008.basepro.applib.widget.recyclerview.listener;

import android.view.View;

import com.yq008.basepro.applib.widget.recyclerview.RecyclerBaseAdapter;
import com.yq008.basepro.applib.widget.recyclerview.RecyclerAdapter;

/**
 * Created by Xiay on 2017/4/10.
 */
public abstract   class OnItemClickListener<ADT,AD> implements RecyclerAdapter.OnItemClickListener {
    @Override
    public void onItemClick(RecyclerBaseAdapter adapter, View view, int position) {
        onItemClick((AD)adapter,view,(ADT)adapter.getItem(position),position);
    }

    public abstract void onItemClick(AD adapter, View view, ADT item,int position) ;

}

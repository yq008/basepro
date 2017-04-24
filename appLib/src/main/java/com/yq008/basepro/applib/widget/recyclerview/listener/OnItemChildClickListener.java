package com.yq008.basepro.applib.widget.recyclerview.listener;

import android.view.View;

import com.yq008.basepro.applib.widget.recyclerview.RecyclerBaseAdapter;
import com.yq008.basepro.applib.widget.recyclerview.RecyclerAdapter;

/**
 * Created by Xiay on 2017/4/10.
 */
public abstract   class OnItemChildClickListener<ADT,AD> implements RecyclerAdapter.OnItemChildClickListener {
    @Override
    public boolean onItemChildClick(RecyclerBaseAdapter adapter, View view, int position) {
       return onItemChildClick((AD)adapter,view,(ADT)adapter.getItem(position),position);
    }

    public abstract boolean onItemChildClick(AD adapter, View view, ADT item,int position) ;

}

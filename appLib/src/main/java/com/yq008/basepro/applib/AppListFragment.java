package com.yq008.basepro.applib;

import com.yq008.basepro.applib.widget.recyclerview.RecyclerBaseAdapter;
import com.yq008.basepro.applib.widget.recyclerview.RecyclerViewHolder;

/**
 * Created by Xiay on 2017/4/10.
 */
@Deprecated
public abstract class AppListFragment<RQT, ADT,AD extends RecyclerBaseAdapter<ADT,RecyclerViewHolder>> extends AppListBaseFragment<RQT,ADT,RecyclerViewHolder,AD> {
}

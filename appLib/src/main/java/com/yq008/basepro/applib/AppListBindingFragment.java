package com.yq008.basepro.applib;

import com.yq008.basepro.applib.widget.recyclerview.RecyclerBaseAdapter;
import com.yq008.basepro.applib.widget.recyclerview.RecycleBindingHolder;

/**
 * Created by Xiay on 2017/4/10.
 */

public abstract class AppListBindingFragment<RQT, ADT,AD extends RecyclerBaseAdapter<ADT,RecycleBindingHolder>> extends AppListBaseFragment<RQT,ADT,RecycleBindingHolder,AD> {
}

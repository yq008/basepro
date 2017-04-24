package com.yq008.basepro.applib.widget.recyclerview;

import android.support.annotation.LayoutRes;
import android.util.SparseIntArray;
import android.view.ViewGroup;

import com.yq008.basepro.applib.widget.recyclerview.entity.MultiItemEntity;

import java.util.List;


public abstract class RecyclerMultiItemAdapter<T extends MultiItemEntity> extends RecyclerAdapter<T> {

    /**
     * layouts indexed with their types
     */
    private SparseIntArray layouts;

    private static final int DEFAULT_VIEW_TYPE = -0xff;


    public RecyclerMultiItemAdapter() {
    }
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data    A new list is created out of this one to avoid mutable list
     */
    public RecyclerMultiItemAdapter( List<T> data) {
        super( data);
    }

    @Override
    protected int getDefItemViewType(int position) {
        Object item = mData.get(position);
        if (item instanceof MultiItemEntity) {
            return ((MultiItemEntity)item).getItemType();
        }
        return DEFAULT_VIEW_TYPE;
    }

    protected void setDefaultViewTypeLayout(@LayoutRes int layoutResId) {
        addItemType(DEFAULT_VIEW_TYPE, layoutResId);
    }

    @Override
    protected RecyclerViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        return createBaseViewHolder(parent, getLayoutId(viewType));
    }

    private int getLayoutId(int viewType) {
        return layouts.get(viewType);
    }

    protected void addItemType(int type, @LayoutRes int layoutResId) {
        if (layouts == null) {
            layouts = new SparseIntArray();
        }
        layouts.put(type, layoutResId);
    }



}



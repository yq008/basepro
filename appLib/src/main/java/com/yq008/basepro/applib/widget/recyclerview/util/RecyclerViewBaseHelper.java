package com.yq008.basepro.applib.widget.recyclerview.util;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yq008.basepro.applib.R;
import com.yq008.basepro.applib.widget.TitleBar;
import com.yq008.basepro.applib.widget.recyclerview.RecyclerBaseAdapter;
import com.yq008.basepro.applib.widget.recyclerview.RecyclerViewHolder;
import com.yq008.basepro.applib.widget.recyclerview.WrapContentLinearLayoutManager;
import com.yq008.basepro.applib.widget.recyclerview.listener.OnItemChildClickListener;
import com.yq008.basepro.applib.widget.recyclerview.listener.OnItemChildLongClickListener;
import com.yq008.basepro.applib.widget.recyclerview.listener.OnItemClickListener;
import com.yq008.basepro.applib.widget.recyclerview.listener.OnItemLongClickListener;
import com.yq008.basepro.applib.widget.recyclerview.recyclerviewflexibledivider.GridSpacingItemDecoration;
import com.yq008.basepro.applib.widget.recyclerview.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import com.yq008.basepro.util.autolayout.utils.AutoUtils;
import com.yq008.basepro.widget.Toast;

import java.util.List;

import static com.yq008.basepro.http.Http.getContext;

/**
 * Created by Xiay on 2017/2/14.
 */

public class RecyclerViewBaseHelper<ADT, HT extends RecyclerViewHolder, AD extends RecyclerBaseAdapter<ADT, HT>> {
    int currentPage = 1;
    protected int perPageSize = 10;
    private RecyclerBaseAdapter<ADT,HT> adapter;
    /**
     * 是否显示没有更多数据View
     */
    private boolean isShowEnd = true;
    private ViewGroup emptyView;
    private RecyclerView rv_list;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    int customEmptyViewLayout = R.layout.app_empty_view;
    private  String emptyMessage;

    /**
     * 设置自定义EmptyView
     *
     * @param customEmptyViewProvider
     */
    public void setCustomEmptyViewProvider(CustomEmptyViewProvider customEmptyViewProvider) {
        this.customEmptyViewLayout = customEmptyViewProvider.getCustomEmptyViewLayout();
    }

    public interface CustomEmptyViewProvider {
        int getCustomEmptyViewLayout();
    }
    /**列表没有文字的时候显示的提示内容*/
    public void setEmptyMessage(String emptyMessage) {
        this.emptyMessage = emptyMessage;
    }

    public RecyclerViewBaseHelper() {
    }
    public RecyclerViewBaseHelper(Activity activity) {
        findView(activity);
    }
    public RecyclerViewBaseHelper(Fragment fragment) {
        findView(fragment);
    }
    public RecyclerViewBaseHelper(View view) {
        findView(view);
    }

    /**
     * @param rv_list      RecyclerView
     * @param adapter      RecyclerBaseAdapter
     * @param perPageSize  每页显示多少条数据
     * @param emptyMessage 没有数据的时候显示的文字消息
     */
    public RecyclerViewBaseHelper(RecyclerView rv_list, RecyclerBaseAdapter<ADT, HT> adapter, int perPageSize, String emptyMessage) {
        this.perPageSize = perPageSize;
        this.adapter = adapter;
        this.rv_list = rv_list;
        rv_list.setLayoutManager(new WrapContentLinearLayoutManager(rv_list.getContext(), LinearLayoutManager.VERTICAL, false));
        //      添加动画
        rv_list.setItemAnimator(new DefaultItemAnimator());
        this.adapter = adapter;
        rv_list.setAdapter(adapter);
        if (emptyMessage != null){
            this.emptyMessage = emptyMessage;
            emptyView = getEmptyView();
        }

    }


    public RecyclerViewBaseHelper(RecyclerView rv_list, RecyclerBaseAdapter<ADT, HT> adapter) {
        this(rv_list, adapter, 10, null);
    }

    public void setItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        if (hasRecyclerView())
            rv_list.addItemDecoration(itemDecoration);
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.rv_list = recyclerView;
    }
    public void setAdapter(AD adapter) {
        setAdapter(adapter,null);

    }
    /**
     * @param adapter      RecyclerBaseAdapter
     * @param emptyMessage 没有数据的时候显示的文字消息
     */
    public void setAdapter(AD adapter, String emptyMessage) {
        if (hasRecyclerView()) {
            if (emptyMessage != null){
                this.emptyMessage = emptyMessage;
                emptyView = getEmptyView();
            }else if (emptyView==null){
                emptyView= getEmptyView();
            }
            adapter.bindToRecyclerView(rv_list);
            rv_list.setLayoutManager(new WrapContentLinearLayoutManager(rv_list.getContext(), LinearLayoutManager.VERTICAL, false));
            //      添加动画
            rv_list.setItemAnimator(new DefaultItemAnimator());
            this.adapter = adapter;
            rv_list.setAdapter(adapter);
        }

    }

    private boolean hasRecyclerView() {
        if (rv_list != null) {
            return true;
        }
        Toast.show("Not found RecyclerView");
        return false;
    }

    public void setShowEnd(boolean showEnd) {
        isShowEnd = showEnd;
    }


    /**
     * 设置下拉刷新监听
     *
     * @param onRefreshListener
     */
    public void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener onRefreshListener) {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setColorSchemeResources(R.color.red, R.color.green, R.color.yellow);
            mSwipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        }
    }

    /**
     * @param dividerHeight -2 使用默认高度和颜色
     * @param dividerColor  -1 使用透明颜色
     * @return
     */
    public RecyclerView.ItemDecoration getItemDecoration(int dividerHeight, int dividerColor) {
        if (!hasRecyclerView())
            return null;
        RecyclerView.ItemDecoration itemDecoration = null;
        if (dividerHeight != -2) {//如果不是默认
            if (dividerColor == -1)
                dividerColor = R.color.transparent;
            if (dividerHeight != -1)
                itemDecoration = new HorizontalDividerItemDecoration.Builder(rv_list.getContext()).size(AutoUtils.getWidthSizeBigger(dividerHeight)).color(rv_list.getContext().getResources().getColor(dividerColor)).build();
        } else {
            itemDecoration = new HorizontalDividerItemDecoration.Builder(getContext()).size(AutoUtils.getWidthSizeBigger(1)).color(rv_list.getContext().getResources().getColor(R.color.gray_listLine)).build();
        }
        return itemDecoration;

    }

    public RecyclerView.ItemDecoration getItemDecoration() {
        return getItemDecoration(-2, -1);
    }

    /**
     * 设置表格布局横向item个数
     *
     * @param count
     */
    public void setGridLayoutCount(int count) {
        if (hasRecyclerView()) {
            rv_list.setLayoutManager(new GridLayoutManager(rv_list.getContext(), count));
        }

    }
    /**
     * 设置表格布局横向item个数
     *
     * @param count 横向显示个数
     * @param spacing 控件间距
     */
    public void setGridLayoutCount(int count,int spacing) {
        if (hasRecyclerView()) {
            rv_list.setLayoutManager(new GridLayoutManager(rv_list.getContext(), count));
            rv_list.addItemDecoration(new GridSpacingItemDecoration(count, spacing, false));
        }

    }

    /***
     * 设置当前页数
     *
     * @param currentPage
     */
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setListData(List<ADT> newData) {
        setListData(newData,true);
    }

    public void setListData(List<ADT> newData,  boolean isShowEmptyView) {
        if (!hasRecyclerView())
            return;
        if (newData == null) {
            if (mSwipeRefreshLayout != null)
                mSwipeRefreshLayout.setRefreshing(false);
            if (isShowEmptyView)
                addEmptyView();
            adapter.setNewData(newData);
         //   adapter.getData().clear();
            adapter.notifyDataSetChanged();
       //     adapter.removeAllFooterView();
            return;
        }
        if (currentPage == 1) {
            if (newData.size() == 0) {
                if (isShowEmptyView)
                    addEmptyView();
                adapter.getData().clear();
                adapter.notifyDataSetChanged();
                adapter.removeAllFooterView();
            } else if (newData.size() < perPageSize) {
                adapter.setNewData(newData);
                toEnd();
            } else {
                adapter.setNewData(newData);
            }
        } else {
            if (newData.size() < perPageSize) {
                if (newData.size() > 0)
                    adapter.addData(newData);
                toEnd();
            } else {
                adapter.addData(newData);
                adapter.loadMoreComplete();
            }
        }
        if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setRefreshing(false);
    }



    private void addEmptyView() {
        adapter.setEmptyView(emptyView);
    }

    public RecyclerBaseAdapter<ADT, HT> getAdapter() {
        return adapter;
    }


    public void toEnd() {
        if (adapter != null) {
            adapter.loadMoreEnd(isShowEnd);
        }
    }

    protected ViewGroup getEmptyView() {
        if (emptyView == null) {
            emptyView = (ViewGroup) LayoutInflater.from(rv_list.getContext()).inflate(customEmptyViewLayout, (ViewGroup) rv_list.getParent(), false);
            if (emptyMessage != null) {
                View v_emptyText = emptyView.findViewById(R.id.tv_empty);
                if (v_emptyText != null && v_emptyText instanceof TextView)
                    ((TextView) v_emptyText).setText(emptyMessage);
            }
            AutoUtils.auto(emptyView);
        }
        return emptyView;
    }

    public void onFailed() {
        if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setRefreshing(false);
        if (adapter != null)
            adapter.loadMoreFail();
    }

    public void setLoadMoreEnable(boolean isShowEnd, RecyclerBaseAdapter.RequestLoadMoreListener requestLoadMoreListener) {
        this.isShowEnd = isShowEnd;
        adapter.setOnLoadMoreListener(requestLoadMoreListener);
    }

    public void addHeaderView(View header) {
        if (!hasRecyclerView())
            return;
        adapter.removeAllHeaderView();
        adapter.addHeaderView(header);
        adapter.setHeaderAndEmpty(true);
    }

    public void setPerPageSize(int perPageSize) {
        this.perPageSize = perPageSize;
    }

    public void setOnItemClickListener(final OnItemClickListener<ADT, AD> listener) {
        if (!hasRecyclerView())
            return;
        adapter.setOnItemClickListener(new RecyclerBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerBaseAdapter adapter, View view, int position) {
                listener.onItemClick((AD) adapter, view, (ADT) adapter.getItem(position), position);
            }
        });
    }

    public void setOnItemLongClickListener(final OnItemLongClickListener<ADT, AD> listener) {
        if (!hasRecyclerView())
            return;
        adapter.setOnItemLongClickListener(new RecyclerBaseAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(RecyclerBaseAdapter adapter, View view, int position) {
                return listener.onItemLongClick((AD) adapter, view, (ADT) adapter.getItem(position), position);
            }
        });
    }

    public void setOnItemChildClickListener(final OnItemChildClickListener<ADT, AD> listener) {
        if (!hasRecyclerView())
            return;
        adapter.setOnItemChildClickListener(new RecyclerBaseAdapter.OnItemChildClickListener() {
            @Override
            public boolean onItemChildClick(RecyclerBaseAdapter adapter, View view, int position) {
                return listener.onItemChildClick((AD) adapter, view, (ADT) adapter.getItem(position), position);
            }
        });
    }

    public void setOnItemChildLongClickListener(final OnItemChildLongClickListener<ADT, AD> listener) {
        if (!hasRecyclerView())
            return;
        adapter.setOnItemChildLongClickListener(new RecyclerBaseAdapter.OnItemChildLongClickListener() {
            @Override
            public void onItemChildLongClick(RecyclerBaseAdapter adapter, View view, int position) {
                listener.onItemChildLongClick((AD) adapter, view, (ADT) adapter.getItem(position), position);
            }

        });
    }

    public RecyclerView findView(Activity activity) {
        View rootView = ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
        findChildView(rootView);
        return null;
    }
    public RecyclerView findView(Fragment fragment) {
        findChildView(fragment.getView());
        return null;
    }
    public RecyclerView findView(View view) {
        findChildView(view);
        return null;
    }

    private void findChildView(View v) {
        if (v instanceof ViewGroup) { //如果是ViewGroup，遍历下面的子view
            if (v instanceof SwipeRefreshLayout) {
                SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) v;
                this.mSwipeRefreshLayout = refreshLayout;
                View v1 = refreshLayout.getChildAt(1);
                if (v1 instanceof RecyclerView) {
                    rv_list = (RecyclerView) v1;
                    return ;
                }
            } else if (v instanceof RecyclerView) {
                rv_list = (RecyclerView) v;
                return ;
            } else {
                ViewGroup childViewGroup = (ViewGroup) v;
                int childCount = childViewGroup.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View childLv1 = childViewGroup.getChildAt(i);
                    if (childLv1 instanceof LinearLayout || childLv1 instanceof RelativeLayout || childLv1 instanceof FrameLayout) {
                        if (childLv1 instanceof TitleBar)//不查找TitleBar里面的view
                            continue;
                        findChildView(childLv1);
                        continue;
                    }
                    if (childLv1 instanceof SwipeRefreshLayout) {
                        SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) childLv1;
                        this.mSwipeRefreshLayout = refreshLayout;
                        View v1 = refreshLayout.getChildAt(1);
                        if (v1 instanceof RecyclerView) {
                            rv_list = (RecyclerView) v1;
                            return ;
                        }
                    } else if (childLv1 instanceof RecyclerView) {
                        rv_list = (RecyclerView) childLv1;
                        return ;
                    }


                }
            }
        }
    }

    public RecyclerView getRecyclerView() {
        return rv_list;
    }
}

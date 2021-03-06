package com.yq008.basepro.applib;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yq008.basepro.applib.widget.recyclerview.RecyclerBaseAdapter;
import com.yq008.basepro.applib.widget.recyclerview.RecyclerViewHolder;
import com.yq008.basepro.applib.widget.recyclerview.listener.OnItemChildClickListener;
import com.yq008.basepro.applib.widget.recyclerview.listener.OnItemChildLongClickListener;
import com.yq008.basepro.applib.widget.recyclerview.listener.OnItemClickListener;
import com.yq008.basepro.applib.widget.recyclerview.listener.OnItemLongClickListener;
import com.yq008.basepro.applib.widget.recyclerview.util.RecyclerViewHelper;
import com.yq008.basepro.http.extra.HttpListener;
import com.yq008.basepro.http.rest.Response;
import com.yq008.basepro.widget.Toast;

import java.util.List;

/***
 *@author Xiay
 * @param <RQT>请求数据类型
 * @param <ADT> Adapter 数据类型
 * @param <AD>Adapter 类型
 */

public abstract class AppListBaseFragment<RQT,ADT, HT extends RecyclerViewHolder,AD extends RecyclerBaseAdapter<ADT,HT>> extends AppFragment  implements  SwipeRefreshLayout.OnRefreshListener, com.yq008.basepro.applib.widget.recyclerview.RecyclerBaseAdapter.RequestLoadMoreListener, HttpListener<RQT> {
	public AD adapter;
	protected int currentPage = 1;
	private RecyclerViewHelper recyclerViewHelper;
	public boolean isShowEnd = true;
	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		activity.isAutoShowNoNetwork=false;
	}


	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		recyclerViewHelper = new RecyclerViewHelper(this);
	}

	public RecyclerViewHelper getRecyclerViewHelper() {
		return recyclerViewHelper;
	}
	public void initListView(AD adapter) {
		initListView(adapter,null);
	}
	public void initListView(AD adapter, String emptyMessage) {
		initListView(-2,adapter,emptyMessage);
	}
	public void initListView(int dividerHeight,AD adapter) {
		initListView(dividerHeight,-1,adapter,null);
	}
	public void initListView(int dividerHeight,AD adapter,String emptyMessage) {
		initListView(dividerHeight,-1,adapter,emptyMessage);
	}
	/**
	 *  初始化列表
	 * @param dividerHeight  水平分割线高度
	 * @param dividerColor  水平分割线颜色
	 * @param adapter
	 * @param emptyMessage   adapter为空的时候提示文字
	 */
	public void initListView(int dividerHeight,int dividerColor,AD adapter,String emptyMessage) {
		initView(adapter,emptyMessage);
		recyclerViewHelper.setItemDecoration(recyclerViewHelper.getItemDecoration(dividerHeight,dividerColor));
	}
	/**
	 * 初始化列表
	 *
	 * @param itemDecoration 分割线样式
	 * @param adapter
	 */
	public void initListView(RecyclerView.ItemDecoration itemDecoration, AD adapter) {
		initListView(itemDecoration,adapter,null);
	}
	/**
	 * 初始化列表
	 *
	 * @param itemDecoration 分割线样式
	 * @param adapter
	 * @param emptyMessage   adapter为空的时候提示文字
	 */
	public void initListView(RecyclerView.ItemDecoration itemDecoration, AD adapter, String emptyMessage) {
		initView(adapter,emptyMessage);
		recyclerViewHelper.setItemDecoration(itemDecoration);
	}

	private void initView(AD adapter, String emptyMessage) {
		this.adapter=adapter;
		recyclerViewHelper.setAdapter(adapter,emptyMessage);
		recyclerViewHelper.setOnRefreshListener(this);
	}
	public void setOnItemClickListener(final OnItemClickListener<ADT,AD> listener){
		recyclerViewHelper.setOnItemClickListener(listener);
	}
	public void setOnItemLongClickListener(final OnItemLongClickListener<ADT,AD> listener){
		recyclerViewHelper.setOnItemLongClickListener(listener);
	}
	public void setOnItemChildClickListener(final OnItemChildClickListener<ADT,AD> listener){
		recyclerViewHelper.setOnItemChildClickListener(listener);
	}
	public void setOnItemChildLongClickListener(final OnItemChildLongClickListener<ADT,AD> listener){
		recyclerViewHelper.setOnItemChildLongClickListener(listener);
	}
	/**
	 * 设置表格布局横向item个数
	 *
	 * @param count
	 */
	public void setGridLayoutCount(int count) {
		if (recyclerViewHelper ==null){
			Toast.show("请先调用initListView方法");
			return;
		}
		recyclerViewHelper.setGridLayoutCount(count);
	}
	/**
	 * 设置表格布局横向item个数
	 *
	 * @param count
	 */
	public void setGridLayoutCount(int count,int spacing) {
		if (recyclerViewHelper ==null){
			Toast.show("请先调用initListView方法");
			return;
		}
		recyclerViewHelper.setGridLayoutCount(count,spacing);
	}
	public  void setLoadMoreEnable(){
		setLoadMoreEnable(true);
	}
	/**
	 * @param isShowEnd false 不显示没有更多数据view
     */
	public  void setLoadMoreEnable(boolean isShowEnd){
		this.isShowEnd=isShowEnd;
		adapter.setOnLoadMoreListener(this);
	}


	@Override
	public void onRefresh() {
		currentPage = 1;
		getListData();
		adapter.removeAllFooterView();
	}

	@Override
	public void onLoadMore() {
		currentPage++;
		getListData();
	}



	protected void addHeaderView(View header) {
		if (recyclerViewHelper ==null){
			Toast.show("请先调用initListView方法");
			return;
		}
		recyclerViewHelper.addHeaderView(header);
	}



	protected void setListData(List<ADT> newData) {
		setListData(newData, true);
	}

	protected void setListData(List<ADT> newData, boolean isShowEmptyView) {
		if (recyclerViewHelper ==null){
			Toast.show("请先调用initListView方法");
			return;
		}
		recyclerViewHelper.setCurrentPage(currentPage);
		recyclerViewHelper.setListData(newData,isShowEmptyView);
	}
	@Override
	public void onFailed(int i, Response response) {
		recyclerViewHelper.onFailed();
	}
	/**设置每页加载多少条数据*/
	protected  void  setPerPageSize(int perPageSize){
		recyclerViewHelper.setPerPageSize(perPageSize);
	}
	public abstract void getListData();
}

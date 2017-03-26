package com.lcodecorex;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lcodecorex.tiny.IGetData;
import com.lcodecorex.tiny.IHolder;

/**
 * 懒加载仅限于在ViewPager中使用，或者主动调用 setUserVisibleHint(true)
 */
public class Tiny<T> {
    int pageItemCount = 15; // 默认itemCount是15
    boolean canLoadMore = true;
    View emptyView;
    BaseRecyclerAdapter<T> adapter;
    RecyclerView.LayoutManager layoutManager;
    IHolder<T> holder;
    boolean enableResumeRefresh = false;
    boolean enableEmptyView = true;
    int[] swipeColorResIds;
    String emptyText;
    int emptyDrawableId;
    IGetData<T> getDataImpl;
    int refreshTimeout = 2000;
    int loadMoreTimeout = 2000;
    int inflateLayoutId;
    boolean enableLazyLoad = false;


    public Tiny<T> layout(int inflateLayoutId) {
        this.inflateLayoutId = inflateLayoutId;
        return this;
    }

    public Tiny<T> pageSize(int pageItemCount) {
        this.pageItemCount = pageItemCount;
        return this;
    }

    public Tiny<T> setCanLoadMore(boolean canLoadMore) {
        this.canLoadMore = canLoadMore;
        return this;
    }

    public Tiny<T> emptyView(View emptyView) {
        this.emptyView = emptyView;
        return this;
    }

    public Tiny<T> adapter(BaseRecyclerAdapter<T> adapter) {
        this.adapter = adapter;
        return this;
    }

    public Tiny<T> layoutManager(RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        return this;
    }

    public Tiny<T> hold(IHolder<T> holder) {
        this.holder = holder;
        return this;
    }

    public Tiny<T> enableResumeRefresh(boolean enableResumeRefresh) {
        this.enableResumeRefresh = enableResumeRefresh;
        return this;
    }

    public Tiny<T> enableEmptyView(boolean enableEmptyView) {
        this.enableEmptyView = enableEmptyView;
        return this;
    }

    public Tiny<T> setSwipeColorRes(int... colorResIds) {
        this.swipeColorResIds = colorResIds;
        return this;
    }

    public Tiny<T> emptyText(String emptyText) {
        this.emptyText = emptyText;
        return this;
    }

    public Tiny<T> emptyImage(int drawableId) {
        this.emptyDrawableId = drawableId;
        return this;
    }

    public Tiny<T> refreshTimeout(int refreshTimeout) {
        this.refreshTimeout = refreshTimeout;
        return this;
    }

    public Tiny<T> loadMoreTimeout(int loadMoreTimeout) {
        this.loadMoreTimeout = loadMoreTimeout;
        return this;
    }

    public Tiny<T> lazyLoad() {
        this.enableLazyLoad = true;
        return this;
    }

    public Tiny<T> setData(IGetData<T> getDataImpl) {
        this.getDataImpl = getDataImpl;
        return this;
    }

    public TinyListFragment<T> createFragment() {
        TinyListFragment<T> fragment = new TinyListFragment<>();
        fragment.setConfig(this);
        return fragment;
    }
}
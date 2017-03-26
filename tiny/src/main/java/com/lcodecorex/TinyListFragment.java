package com.lcodecorex;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lcodecorex.tiny.DataCallback;
import com.lcodecorex.tiny.IGetData;
import com.lcodecorex.tiny.IHolder;
import com.lcodecorex.tiny.R;

import java.util.List;

/**
 * Created by lcodecore on 2017/3/25.
 * TinyListFragment contains refresh/loadmore and place-view.
 */

public class TinyListFragment<Bean> extends BaseFragment implements DataCallback<List<Bean>>, SwipeRefreshLayout.OnRefreshListener, LRecyclerView.LoadMoreListener {

    public LRecyclerView recyclerView;

    public SwipeRefreshLayout swipeRefresh;

    protected View emptyView;

    protected ImageView iv_empty;

    protected TextView tv_empty;

    protected static final int STATE_REFRESH = 0;
    protected static final int STATE_LOADMORE = 1;
    //当前的装态，在刷新和加载更多之间切换
    protected int state = STATE_REFRESH;

    //单页显示数量
    protected int pageItemCount = Integer.MAX_VALUE;
    //标识当前加载的分页
    protected int currentPage = 1;

    //是否允许显示缺省图标
    protected boolean enableEmptyView = true;

    //Adapter
    protected BaseRecyclerAdapter<Bean> adapter;

    //是否允许在resume时刷新页面
    protected boolean enableResumeRefresh;

    //SwipeRefreshLayout样式
    int[] swipeColorResIds;

    //刷新显示的超时时间
    int refreshTimeout = 2000;
    //加载更多显示的超时时间
    int loadMoreTimeout = 2000;

    //是否开启懒加载模式，默认为false
    boolean enableLazyLoad = false;

    //获取数据的业务
    private IGetData<Bean> getDataImpl;

    //配置信息
    private Tiny<Bean> config;


    @Override
    public int getInflateId() {
        if (getConfig() != null) {
            config = getConfig();
        }
        if (config != null && config.inflateLayoutId > 0) {
            return config.inflateLayoutId;
        }
        return R.layout.fragment_tiny;
    }

    public void setConfig(Tiny<Bean> config) {
        this.config = config;
    }

    public Tiny<Bean> getConfig() {
        return null;
    }

    @Override
    public void init() {
        recyclerView = bind(R.id.tiny_recyclerView);
        swipeRefresh = bind(R.id.tiny_refreshLayout);
        emptyView = bind(R.id.tiny_emptyView);
        iv_empty = bind(R.id.iv_tiny_default);
        tv_empty = bind(R.id.tv_tiny_default);

        if (config != null) {
            enableLazyLoad = config.enableLazyLoad;
        }
        if (!enableLazyLoad) {
            initConfig();
        }
    }

    private void initConfig() {
        readConfig(config);
        initRefreshLayout();
    }

    private void readConfig(Tiny<Bean> config) {
        if (config == null) return;

        if (config.canLoadMore && config.pageItemCount > 0) {
            pageItemCount = config.pageItemCount;
        }
        if (config.emptyView != null) {
            setEmptyView(config.emptyView);
        }
        if (config.adapter != null) {
            adapter = config.adapter;
            recyclerView.setAdapter(adapter);
        }
        if (config.layoutManager != null) {
            recyclerView.setLayoutManager(config.layoutManager);
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        if (config.holder != null) {
            adapter = new InnerAdapter(config.holder);
            recyclerView.setAdapter(adapter);
        }
        enableResumeRefresh = config.enableResumeRefresh;
        enableEmptyView = config.enableEmptyView;
        if (config.swipeColorResIds != null && config.swipeColorResIds.length > 0) {
            swipeColorResIds = config.swipeColorResIds;
        }
        if (config.emptyView == null && config.emptyDrawableId > 0 && iv_empty != null) {
            iv_empty.setImageResource(config.emptyDrawableId);
        }
        if (config.emptyView == null && !TextUtils.isEmpty(config.emptyText) && tv_empty != null) {
            tv_empty.setText(config.emptyText);
        }
        refreshTimeout = config.refreshTimeout;
        loadMoreTimeout = config.loadMoreTimeout;
        if (config.getDataImpl != null) {
            getDataImpl = config.getDataImpl;
        }
    }

    private void initRefreshLayout() {
        if (swipeColorResIds != null && swipeColorResIds.length > 0) {
            swipeRefresh.setColorSchemeResources(swipeColorResIds);
        }
        swipeRefresh.setProgressViewOffset(true, -20, 100);
        swipeRefresh.setOnRefreshListener(this);
        recyclerView.setLoadMoreListener(this);
        if (emptyView != null) {
            emptyView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    swipeRefresh.post(new Runnable() {
                        @Override
                        public void run() {
                            if (swipeRefresh != null) swipeRefresh.setRefreshing(true);
                            onRefresh();
                        }
                    });
                }
            });
        }
    }

    private int resumeCount = 0;

    @Override
    public void onResume() {
        super.onResume();

        if (enableLazyLoad) {
            if (!isPrepared || !isVisible) return;
            firstVisible = false;
        }

        if (resumeCount == 0) {
            //自动刷新
            swipeRefresh.post(new Runnable() {
                @Override
                public void run() {
                    if (swipeRefresh != null) swipeRefresh.setRefreshing(true);
                    onRefresh();
                }
            });
        }
        if (resumeCount < 2) resumeCount++;
        if (enableResumeRefresh) {
            if (resumeCount > 1 && currentPage == 1) {
                //resume执行多次了,且当前在显示第一页则刷新
                swipeRefresh.post(new Runnable() {
                    @Override
                    public void run() {
                        if (swipeRefresh != null) swipeRefresh.setRefreshing(true);
                        onRefresh();
                    }
                });
            }
        }
    }

    private boolean isPrepared = false;
    private boolean isVisible = false;
    private boolean firstVisible = true;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        System.out.println("视图当前1..." + enableLazyLoad);
        if (enableLazyLoad) {
            System.out.println("视图当前2..." + getUserVisibleHint());
            if (getUserVisibleHint()) {
                isVisible = true;
                if (firstVisible) initConfig();
            } else {
                isVisible = false;
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isPrepared = true;
    }

//    private void lazyLoad() {
//        if (!isPrepared || !isVisible) return;
//        initData();
//        firstVisible = false;
//    }

    @Override
    public void onRefresh() {
        state = STATE_REFRESH;
        hideDefaultView();

        if (getDataImpl != null) {
            getDataImpl.toRefresh(this);
        }
        recyclerView.loadMoreComplete();
        rootView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (swipeRefresh != null) swipeRefresh.setRefreshing(false);
            }
        }, refreshTimeout);
    }

    @Override
    public void onLoadMore() {
        state = STATE_LOADMORE;

        if (getDataImpl != null) {
            getDataImpl.toLoadMore(this, currentPage + 1);
        }
        rootView.postDelayed(new Runnable() {
            public void run() {
                if (recyclerView != null) recyclerView.loadMoreComplete();
            }
        }, loadMoreTimeout);
    }

    @Override
    public void onSuccess(List<Bean> data) {
        if (data != null && data.size() >= pageItemCount) {
            recyclerView.setCanloadMore(true);
        } else {
            recyclerView.setCanloadMore(false);
        }
        if (state == STATE_REFRESH) {//刷新模式currentPage置1
            currentPage = 1;
            if (data == null || data.size() == 0) {
                showDefaultView();
            } else {
                hideDefaultView();
            }
            adapter.setDataList(data);
        } else {//加载更多模式currentPage自增
            currentPage++;
            adapter.addItems(data);
        }
    }

    @Override
    public void onFailure(String errorCode, String msg) {
        //TODO 处理errorCode和msg
        if (adapter.getItemCount() <= 0) {
            showDefaultView();
        }
    }

    @Override
    public void onError() {
        if (adapter.getItemCount() <= 0) {
            showDefaultView();
        }
    }

    protected void showDefaultView() {
        if (enableEmptyView) {
            if (emptyView.getVisibility() != View.VISIBLE) emptyView.setVisibility(View.VISIBLE);
        } else {
            if (emptyView.getVisibility() != View.GONE) emptyView.setVisibility(View.GONE);
        }
    }

    protected void hideDefaultView() {
        if (emptyView.getVisibility() != View.GONE) emptyView.setVisibility(View.GONE);
    }

    public void setEmptyView(View view) {
        if (view != null && emptyView != null && emptyView instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) emptyView;
            group.removeAllViewsInLayout();
            group.addView(view);
            iv_empty = null;
            tv_empty = null;
        }
    }


    private class InnerAdapter extends BaseRecyclerAdapter<Bean> {
        private IHolder<Bean> holder;

        InnerAdapter(IHolder<Bean> holder) {
            this.holder = holder;
        }

        @Override
        protected CommonHolder<Bean> getViewHolder(ViewGroup parent) {
            return holder.getHolder(parent);
        }
    }
}

package com.lcodecorex;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

import com.lcodecorex.views.FooterAdapter;
import com.lcodecorex.views.FooterLayout;
import com.lcodecorex.views.IFooter;

/**
 * Created by lcodecore on 2017/3/25.
 * RecyclerView with a loadmore-footer.
 */

public class LRecyclerView extends RecyclerView {

    private FooterAdapter mFooterAdapter;
    private IFooter footer;
    private boolean isLoadingData = false;
    private LoadMoreListener loadMoreListener;
    private boolean canloadMore = true;

    public LRecyclerView(Context context) {
        this(context, null);
    }

    public LRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setFooter(new FooterLayout(context));
        setFooterGone();
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (mFooterAdapter == null) {
            mFooterAdapter = new FooterAdapter(this, footer, adapter);
        } else {
            mFooterAdapter.setInnerAdapter(adapter);
        }
        super.setAdapter(mFooterAdapter);
        adapter.registerAdapterDataObserver(mDataObserver);
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);

        if (state == RecyclerView.SCROLL_STATE_IDLE && loadMoreListener != null && !isLoadingData && canloadMore) {
            LayoutManager layoutManager = getLayoutManager();
            int lastVisibleItemPosition;
            if (layoutManager instanceof GridLayoutManager) {
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
                lastVisibleItemPosition = last(into);
            } else {
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }

            if (layoutManager.getChildCount() > 0 && lastVisibleItemPosition >= layoutManager.getItemCount() - 1) {
                setFooterVisible();
                isLoadingData = true;
                loadMoreListener.onLoadMore();
            }
        }
    }

    //设置是否可加载更多
    public void setCanloadMore(boolean flag) {
        canloadMore = flag;
    }

    public void loadMoreComplete() {
        setFooterGone();
        isLoadingData = false;
    }

    //到底了
    public void loadMoreEnd() {
        if (footer != null) {
            footer.setEnd();
        }
    }

    //设置加载更多监听
    public void setLoadMoreListener(LoadMoreListener listener) {
        loadMoreListener = listener;
    }

    //取到最后的一个节点
    private int last(int[] lastPositions) {
        int last = lastPositions[0];
        for (int value : lastPositions) {
            if (value > last) {
                last = value;
            }
        }
        return last;
    }

    private RecyclerView.AdapterDataObserver mDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            mFooterAdapter.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            mFooterAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mFooterAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            mFooterAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mFooterAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            mFooterAdapter.notifyItemMoved(fromPosition, toPosition);
        }
    };

    private void setFooterGone() {
        if (footer != null && footer.getView() != null) {
            if (footer.getView().getVisibility() != GONE) {
                footer.getView().setVisibility(GONE);
            }
        }
    }

    private void setFooterVisible() {
        if (footer != null && footer.getView() != null) {
            if (footer.getView().getVisibility() != VISIBLE) {
                footer.getView().setVisibility(VISIBLE);
            }
        }
    }

    public void setFooter(IFooter footer) {
        this.footer = footer;
    }

    public interface LoadMoreListener {
        void onLoadMore();
    }
}

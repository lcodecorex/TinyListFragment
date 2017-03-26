package com.lcodecorex;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView适配器基类
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<CommonHolder<T>> implements CommonHolder.OnNotifyChangeListener {

    private List<T> dataList = new ArrayList<>();

    private boolean enableHead = false;
    private CommonHolder headHolder;

    private ViewGroup rootView;

    protected static final int TYPE_HEAD = 0;
    protected static final int TYPE_CONTENT = 1;

    @Override
    public CommonHolder<T> onCreateViewHolder(ViewGroup parent, int position) {
        rootView = parent;
        //设置ViewHolder
        int type = getItemViewType(position);
        if (type == TYPE_HEAD) {
            return headHolder;
        } else {
            return getViewHolder(parent);
        }
    }


    @Override
    public void onBindViewHolder(CommonHolder<T> holder, int position) {
//        runEnterAnimation(holder.itemView, position);
        //数据绑定
        if (enableHead) {
            if (position == 0) {
                //头部不填充数据
            } else {
                holder.bindData(dataList.get(position - 1));
            }
        } else {
            holder.bindData(dataList.get(position));
        }

        holder.setOnNotifyChangeListener(this);
    }

    public ViewGroup getRootView() {
        return rootView;
    }

    @Override
    public int getItemCount() {
        if (enableHead) {
            return dataList.size() + 1;
        }
        return dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (enableHead) {
            if (position == 0) {
                return TYPE_HEAD;
            } else {
                return TYPE_CONTENT;
            }
        } else {
            return TYPE_CONTENT;
        }
    }

    @Override
    public void onNotify() {
        //提供给CommonHolder方便刷新视图
        notifyDataSetChanged();
    }

    public void setDataList(List<T> datas) {
        dataList.clear();
        if (null != datas) {
            dataList.addAll(datas);
        }
        notifyDataSetChanged();
    }

    public void clearDatas() {
        dataList.clear();
        notifyDataSetChanged();
    }

    /**
     * 添加数据到前面
     */
    public void addItemsAtFront(List<T> datas) {
        if (null == datas) return;
        dataList.addAll(0, datas);
        notifyDataSetChanged();
    }

    /**
     * 添加数据到尾部
     */
    public void addItems(List<T> datas) {
        if (null == datas) return;
        dataList.addAll(datas);
        notifyDataSetChanged();
    }

    /**
     * 添加单条数据
     */
    public void addItem(T data) {
        if (null == data) return;
        dataList.add(data);
        notifyDataSetChanged();
    }

    /**
     * 删除单条数据
     */

    public void deletItem(T data) {
        dataList.remove(data);
        notifyDataSetChanged();
    }


    /**
     * 设置是否显示head
     *
     * @param ifEnable 是否显示头部
     */
    public void setEnableHead(boolean ifEnable) {
        enableHead = ifEnable;
    }

    public void setHeadHolder(CommonHolder headHolder1) {
        enableHead = true;
        headHolder = headHolder1;
    }

    public void setHeadHolder(View itemView) {
        enableHead = true;
        headHolder = new HeadHolder(itemView);
        notifyItemInserted(0);
    }

    public CommonHolder getHeadHolder() {
        return headHolder;
    }

    /**
     * 子类重写实现自定义ViewHolder
     */
    protected abstract CommonHolder<T> getViewHolder(ViewGroup parent);


    private class HeadHolder extends CommonHolder<Void> {
        HeadHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bindView() {

        }

        @Override
        public void bindData(Void aVoid) {
        }
    }
}

package com.lcodecorex;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * ViewHolder 与 Adapter 解耦
 */
public abstract class CommonHolder<T> extends RecyclerView.ViewHolder {

    public CommonHolder(ViewGroup root, int layoutRes) {
        super(LayoutInflater.from(root.getContext()).inflate(layoutRes, root, false));
        bindView();
    }

    //此适配器是为了能让详情页共用列表页的ViewHolder，一般情况无需重写该构造器
    public CommonHolder(View itemView) {
        super(itemView);
        bindView();
    }

    /**
     * 绑定视图
     */
    public abstract void bindView();

    /**
     * 用给定的 data 对 holder 的 view 进行赋值
     */
    public abstract void bindData(T t);

    /**
     * 视图绑定工具
     */
    private SparseArray<View> mViews = new SparseArray<>();

    protected <V extends View> V bind(int id) {
        View view = mViews.get(id);
        if (view == null) {
            view = itemView.findViewById(id);
            mViews.put(id, view);
        }
        return (V) view;
    }

    protected <V extends View> V get(int id) {
        return bind(id);
    }

    protected Activity getActivity() {
        return (Activity) itemView.getContext();
    }

    protected Context getContext() {
        return itemView.getContext();
    }

    /**
     * 通知适配器更新布局
     */
    public interface OnNotifyChangeListener {
        void onNotify();
    }

    private OnNotifyChangeListener listener;

    public void setOnNotifyChangeListener(OnNotifyChangeListener listener) {
        this.listener = listener;
    }

    public void notifyChange() {
        listener.onNotify();
    }
}

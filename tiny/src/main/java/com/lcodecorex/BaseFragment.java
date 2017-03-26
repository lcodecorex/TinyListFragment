package com.lcodecorex;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lcodecore on 2017/3/25.
 */

public abstract class BaseFragment extends Fragment {

    protected View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == rootView) {
            rootView = inflater.inflate(getInflateId(), container, false);
            init();
        }
        return rootView;
    }

//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        init();
//    }

    public abstract int getInflateId();

    public abstract void init();

    private SparseArray<View> mViews = new SparseArray<>();

    public <T extends View> T bind(int id) {
        View view = mViews.get(id);
        if (view == null) {
            view = rootView.findViewById(id);
            if (view != null) {
                mViews.put(id, view);
            }
        }
        return (T) view;
    }

    public <T extends View> T get(int id) {
        return bind(id);
    }
}

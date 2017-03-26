package com.lcodecorex.tiny;

import android.view.ViewGroup;

import com.lcodecorex.CommonHolder;

public interface IHolder<Bean>{
        CommonHolder<Bean> getHolder(ViewGroup parent);
    }
package com.lcodecorex.tiny;

import java.util.List;

public interface IGetData<Bean> {
    void toRefresh(DataCallback<List<Bean>> callback);

    void toLoadMore(DataCallback<List<Bean>> callback, int currentPage);
}
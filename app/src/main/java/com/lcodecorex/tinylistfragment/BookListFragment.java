package com.lcodecorex.tinylistfragment;

import android.widget.Toast;

import com.lcodecorex.Tiny;
import com.lcodecorex.TinyListFragment;
import com.lcodecorex.tiny.DataCallback;
import com.lcodecorex.tiny.IGetData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lcodecore on 2017/3/26.
 */

public class BookListFragment extends TinyListFragment<Book> implements IGetData<Book> {

    @Override
    public Tiny<Book> getConfig() {
        return null;
    }

    @Override
    public void toRefresh(DataCallback<List<Book>> callback) {
        List<Book> books = new ArrayList<>();
        callback.onSuccess(books);
    }

    @Override
    public void toLoadMore(DataCallback<List<Book>> callback, int currentPage) {
        callback.onError();
    }

    @Override
    public void onFailure(String errorCode, String msg) {
        super.onFailure(errorCode, msg);
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}

package com.lcodecorex.tiny;

public interface DataCallback<T> {

    public void onSuccess(T data);

    public void onFailure(String errorCode, String msg);

    public void onError();
}
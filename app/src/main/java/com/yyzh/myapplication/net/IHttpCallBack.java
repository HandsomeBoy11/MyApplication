package com.yyzh.myapplication.net;

public interface IHttpCallBack<T> {
    void success(T t);
    void failure(String error);
}

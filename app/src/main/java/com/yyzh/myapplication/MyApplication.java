package com.yyzh.myapplication;

import android.app.Application;

import com.yyzh.myapplication.net.OkhttpClient;
import com.yyzh.myapplication.net.ProxyManager;

public class MyApplication extends Application {
    public static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance=this;
        ProxyManager.init(OkhttpClient.getInstance());//网络请求代理（okhttp）
    }
}

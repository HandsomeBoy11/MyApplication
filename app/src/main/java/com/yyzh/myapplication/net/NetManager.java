package com.yyzh.myapplication.net;

import android.util.Log;

import java.util.Map;

public class NetManager implements IHttp{
    private static final String TAG = NetManager.class.getSimpleName();
    private static NetManager mInstance;

    private NetManager() {

    }

    public static NetManager getInstance() {
        if (mInstance == null) {
            synchronized (NetManager.class) {
                if (mInstance == null) {
                    mInstance = new NetManager();
                }
            }
        }
        return mInstance;
    }

    @Override
    public void get(String url, Map<String, String> param, IHttpCallBack callBack) {
        Log.e(TAG,"NetManager的get");
    }

    @Override
    public void post(String url, Map<String, String> param, IHttpCallBack callBack) {
        Log.e(TAG,"NetManager的post");
    }
}

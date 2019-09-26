package com.yyzh.myapplication.net;

import android.util.Log;

import java.util.Map;

public class OkhttpClient implements IHttp{
    private static final String TAG = OkhttpClient.class.getSimpleName();
    private static OkhttpClient mInstance;

    private OkhttpClient() {

    }

    public static OkhttpClient getInstance() {
        if (mInstance == null) {
            synchronized (OkhttpClient.class) {
                if (mInstance == null) {
                    mInstance = new OkhttpClient();
                }
            }
        }
        return mInstance;
    }

    @Override
    public void get(String url, Map<String, String> param, IHttpCallBack callBack) {
        Log.e(TAG,"OkhttpManager的get");
        DoOkhttpQuest.getInstance().get(url,callBack);
    }

    @Override
    public void post(String url, Map<String, String> param, IHttpCallBack callBack) {
        Log.e(TAG,"OkhttpManager的post");
        DoOkhttpQuest.getInstance().post(url,param,callBack);
    }

}

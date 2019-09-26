package com.yyzh.myapplication.net;

import java.util.Map;

public interface IHttp {
    void get(String url, Map<String, String> param, IHttpCallBack callBack);
    void post(String url, Map<String, String> param, IHttpCallBack callBack);
}

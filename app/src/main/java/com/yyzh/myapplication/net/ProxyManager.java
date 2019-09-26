package com.yyzh.myapplication.net;

import android.text.TextUtils;

import java.util.Iterator;
import java.util.Map;

public class ProxyManager implements IHttp {
    private static ProxyManager mInstance;
    private static IHttp mIHttp;

    private ProxyManager() {

    }

    public static void init(IHttp iHttp) {
        mIHttp = iHttp;
    }

    public static ProxyManager getInstance() {
        if (mInstance == null) {
            synchronized (NetManager.class) {
                if (mInstance == null) {
                    mInstance = new ProxyManager();
                }
            }
        }
        return mInstance;
    }

    @Override
    public void get(String url, Map<String, String> param, IHttpCallBack callBack) {
        String appendUrl = getAppendUrl(url, param);
        mIHttp.get(appendUrl, param, callBack);
    }

    @Override
    public void post(String url, Map<String, String> param, IHttpCallBack callBack) {
        mIHttp.post(url, param, callBack);
    }

    public ProxyManager addParam(Map<String, Object> map) {
        map.put("version", "5.0.6");
        map.put("rst", "Xiaomi%23%40%23MI+5X%23%40%237.1.2%23%40%23yjq%23%40%235.0.6%23%40%23sinooceanlab");
        map.put("source", "10");
        map.put("c_datetime", "");
        map.put("projectId", "104");
        map.put("userId", "116490317");
        map.put("packageName", "com.bjyijiequ.community");
        return this;
    }

    /**
     * get请求拼接url
     * @param url
     * @param map
     * @return
     */
    public  String getAppendUrl(String url, Map<String, String> map) {
        if (map != null && !map.isEmpty()) {
            StringBuffer buffer = new StringBuffer();
            Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                if (TextUtils.isEmpty(buffer.toString())) {
                    buffer.append("?");
                } else {
                    buffer.append("&");
                }
                buffer.append(entry.getKey()).append("=").append(entry.getValue());
            }
            url += buffer.toString();
        }
        return url;
    }
}

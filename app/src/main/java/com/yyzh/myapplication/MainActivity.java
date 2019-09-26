package com.yyzh.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yyzh.myapplication.net.IHttp;
import com.yyzh.myapplication.net.IHttpCallBack;
import com.yyzh.myapplication.net.NetManager;
import com.yyzh.myapplication.net.ProxyManager;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onNetManager(View view) {
        ProxyManager.init(NetManager.getInstance());
        IHttp instance = ProxyManager.getInstance();
        instance.get("", null, new IHttpCallBack<String>() {


            @Override
            public void success(String s) {

            }

            @Override
            public void failure(String error) {

            }
        });
    }

    public void onOkhttpManager(View view) {
        for (int i = 0; i < 10; i++) {
            doGet(i);
        }
    }

    private void doGet(final int i) {
        String url = "https://d.bjyijiequ.com/qpi/rest/goodsSlideInfo/slideList";
        Map<String, String> map = new HashMap<>();
        map.put("projectId", "104");
        map.put("pictureType", "2");
        map.put("version", "5.0.6");
        map.put("userId", "116490317");
        map.put("slideType", "1");
        ProxyManager.getInstance().get(url, map, new IHttpCallBack<String>() {
            @Override
            public void success(String s) {
                Log.e(TAG + "get请求成功" + i, s);
            }

            @Override
            public void failure(String error) {
                Log.e(TAG, error);
            }
        });
    }

    public void onOkhttpDoPost(View view) {
        String url = "https://d.bjyijiequ.com/qpi/rest/goodsGroupBuyingInfo/getHomeGroupBuyingList";
        Map<String, String> map = new HashMap<>();
        Map<String, Object> arg = new HashMap<>();
        Map<String, String> map1 = new HashMap<>();
        Gson gson = new Gson();
        arg.put("service", "genPaySignature");
        map1.put("perSize", "3");
        map1.put("pageNum", "1");
        map1.put("priceSort", "");
        map1.put("timeSort", "");
        map1.put("state", "");
        arg.put("request", map1);
        IHttp instance = ProxyManager.getInstance();
        ((ProxyManager) instance).addParam(arg);
        String json = gson.toJson(arg);
        map.put("requestParams", json);
        Log.e(TAG, map.toString());
        instance.post(url, map, new IHttpCallBack<String>() {
            @Override
            public void success(String s) {
                Log.e(TAG, s);
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(String error) {
                Log.e(TAG, error);
            }
        });
    }
}

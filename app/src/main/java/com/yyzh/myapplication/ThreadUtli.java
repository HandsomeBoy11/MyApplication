package com.yyzh.myapplication;


import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 运行在UI线程、子线程
 */

public class ThreadUtli {
    private static ThreadUtli mInstance;

    private ThreadUtli() {
    }

    public static ThreadUtli getInstance() {
        if (mInstance == null) {
            synchronized (ThreadUtli.class) {
                if (mInstance == null) {
                    mInstance = new ThreadUtli();
                }
            }
        }
        return mInstance;
    }

    private static Handler mHander = new Handler(Looper.getMainLooper());
    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 5, 1, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(50));

    public void runOnIO(final ThreadIOHelper helper) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                if (helper != null) {
                    helper.runOnIO();
                }
            }
        });
    }

    public void runOnUI(final ThreadUIHelper helper) {
        mHander.post(new Runnable() {
            @Override
            public void run() {
                if (helper != null) {
                    helper.runOnUI();
                }
            }
        });
    }

    public interface ThreadIOHelper {
        void runOnIO();

    }

    public interface ThreadUIHelper {
        void runOnUI();
    }
}

package com.example.dragsimilaranjian;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

/**
 * author: eagle
 * created on: 2019-06-27 14:26
 * description:
 */
public class DragApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("测试", "初始化DragApplication");
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}

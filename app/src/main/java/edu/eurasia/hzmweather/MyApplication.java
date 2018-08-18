package edu.eurasia.hzmweather;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

/**
 * 类名: MyApplication<br>
 * 功能:(自定义全局Context)<br>
 * 作者:黄振敏 <br>
 * 日期:2018/8/18 14:33
 */
public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LitePal.initialize(context);
    }

    public static Context getContext() {
        return context;
    }

}

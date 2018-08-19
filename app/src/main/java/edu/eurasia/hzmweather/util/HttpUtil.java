package edu.eurasia.hzmweather.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 类名: HttpUtil<br>
 * 功能:(与服务器端交互)<br>
 * 作者:黄振敏 <br>
 * 日期:2018/8/18 14:46
 */
public class HttpUtil {

    public static void sendOkHttpRequest(String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}

package edu.eurasia.hzmweather.log;

import android.util.Log;

/**
 * 类名: LogUtil<br>
 * 功能:(自定义日志)<br>
 * 作者:黄振敏 <br>
 * 日期:2018/8/18 14:06
 */
public class LogUtil {

    public static final int VERBOSE = 1;
    public static final int DEBUG = 2;
    public static final int INFO = 3;
    public static final int WARN = 4;
    public static final int ERROR = 5;
    public static final int NOTHING = 6;
    public static final int level = VERBOSE;

    /**
     * 只需要修改level的变量的值(1~6),控制是否打印输出日志
     * @param tag
     * @param msg
     */

    public static void v(String tag, String msg) {
        if (level <= VERBOSE) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (level <= DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (level <= INFO) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (level <= WARN) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (level <= ERROR) {
            Log.e(tag, msg);
        }
    }
}

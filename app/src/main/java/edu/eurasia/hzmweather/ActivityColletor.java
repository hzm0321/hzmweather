package edu.eurasia.hzmweather;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * 类名: ActivityColletor<br>
 * 功能:(控制活动)<br>
 * 作者:黄振敏 <br>
 * 日期:2018/8/20 9:59
 */
public class ActivityColletor {

    public static List<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        activities.clear();
    }
}

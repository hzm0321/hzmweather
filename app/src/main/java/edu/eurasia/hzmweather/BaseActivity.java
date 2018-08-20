package edu.eurasia.hzmweather;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import edu.eurasia.hzmweather.log.LogUtil;

/**
 * 类名: BaseActivity<br>
 * 功能:(基类Activity)<br>
 * 作者:黄振敏 <br>
 * 日期:2018/8/20 10:10
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d("BaseActivity", getClass().getSimpleName());
        ActivityColletor.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityColletor.removeActivity(this);
    }
}

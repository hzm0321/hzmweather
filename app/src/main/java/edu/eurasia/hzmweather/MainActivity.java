package edu.eurasia.hzmweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.litepal.LitePal;

import edu.eurasia.hzmweather.db.City;
import edu.eurasia.hzmweather.db.Province;
import edu.eurasia.hzmweather.log.LogUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
    }

    /**
     * 初始化本地数据库数据
     */
    private void initDb() {
        LitePal.deleteAll(Province.class);
        LitePal.deleteAll(City.class);
        LitePal.deleteAll(Province.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.d("主页面","销毁方法被执行了");
        initDb();
    }
}

package edu.eurasia.hzmweather;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import edu.eurasia.hzmweather.db.City;
import edu.eurasia.hzmweather.db.Province;
import edu.eurasia.hzmweather.log.LogUtil;

public class MainActivity extends AppCompatActivity {

    public LocationClient mLocationClient = null;

    private MyLocationListener myLocationListener = new MyLocationListener();

    private String privinceName;
    private String cityName;
    private String countyName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //定位
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(myLocationListener);

        setContentView(R.layout.main_activity);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.getString("weather", null) != null) {
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
            finish();
        }

        //如果成功获取到位置,弹出对话框选择是否直接进入本地天气
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        } else {
            LogUtil.d("定位","开始定位");
            requestLocation();
        }





    }


    /**
     * 请求定位
     */
    private void requestLocation() {
//        initLocation();
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        LogUtil.d("定位请求","重新定位");
        mLocationClient.restart();
    }

    /**
     * 定位初始化
     */
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        mLocationClient.setLocOption(option);
    }

    /**
     * 请求回应
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                } else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    /**
     * 定位监听
     */
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            LogUtil.d("定位监听","进入");
            LogUtil.d("省",bdLocation.getProvince());
            LogUtil.d("市", bdLocation.getCity());
            LogUtil.d("县",bdLocation.getDistrict());

            privinceName = bdLocation.getProvince();
            cityName = bdLocation.getCity();
            countyName = bdLocation.getDistrict();

            //弹出对话框
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("获取到定位")
                    .setMessage("欢迎您~\n"+"来自" + privinceName + cityName + countyName + "的网友" + "\n" +
                            "是否要跳转到您当前位置的天气")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(MyApplication.getContext(), WeatherActivity.class);
                            intent.putExtra("weather_id", countyName);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton("取消", null);
            builder.show();

        }
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
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(
                MyApplication.getContext()).edit();
        editor.clear();
        editor.apply();
    }
}

package edu.eurasia.hzmweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.io.IOException;

import edu.eurasia.hzmweather.gson.Air;
import edu.eurasia.hzmweather.gson.Forecast;
import edu.eurasia.hzmweather.gson.Lifestyle;
import edu.eurasia.hzmweather.gson.Weather;
import edu.eurasia.hzmweather.gson.WeatherForecast;
import edu.eurasia.hzmweather.gson.WeatherLifeStyle;
import edu.eurasia.hzmweather.log.LogUtil;
import edu.eurasia.hzmweather.service.AutoUpdateService;
import edu.eurasia.hzmweather.util.HttpUtil;
import edu.eurasia.hzmweather.util.Utility;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    public SwipeRefreshLayout swipeRefresh;
    public DrawerLayout drawerLayout;

    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdatetime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;
    private ImageView bingImage;
    private String mWeatherId;
    private Button navButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_activity);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            );
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        init();

        //设置滑动按钮
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        //设置刷新
        swipeRefresh = findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather", null);
        String weatherForecastString = prefs.getString("forecastWeather", null);
        String airString = prefs.getString("air", null);
        String weatherLifeStyleString = prefs.getString("lifestyle", null);
        //获取背景图
        String bingPic = prefs.getString("bing_pic", null);
        if (bingPic != null) {
            Glide.with(this).load(bingPic).into(bingImage);
        } else {
            loadBingPic();
        }


        if (weatherString != null && weatherForecastString != null &&
                airString != null && weatherLifeStyleString != null) {

            //有缓存直接解析天气数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            WeatherForecast weatherForecast = Utility.handleWeatherForecastResponse(weatherForecastString);
            Air air = Utility.handleAirResponse(airString);
            WeatherLifeStyle weatherLifeStyle = Utility.handleWeatherLifeStyleResponse(weatherLifeStyleString);

            mWeatherId = weather.basic.weatherId;

            //显示到界面
            showWeatherInfo(weather);
            showWeatherForecastInfo(weatherForecast);
            showAirInfo(air);
            showLifeStyleInfo(weatherLifeStyle);
        } else {
            //无缓存去查询服务器数据
            mWeatherId = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(mWeatherId);
        }
        //刷新动作
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadBingPic();
                requestWeather(mWeatherId);
            }
        });
    }

    /**
     * 控件初始化
     */
    private void init() {
        weatherLayout = findViewById(R.id.sl_weather);
        titleCity = findViewById(R.id.tv_city_title);
        titleUpdatetime = findViewById(R.id.tv_update_time);
        degreeText = findViewById(R.id.tv_degree);
        weatherInfoText = findViewById(R.id.tv_weather_info);
        forecastLayout = findViewById(R.id.ll_forecast);
        aqiText = findViewById(R.id.tv_api);
        pm25Text = findViewById(R.id.tv_pm25);
        comfortText = findViewById(R.id.tv_comfort);
        carWashText = findViewById(R.id.tv_car);
        sportText = findViewById(R.id.tv_sport);
        bingImage = findViewById(R.id.iv_bing);
        drawerLayout = findViewById(R.id.drawer_layout);
        navButton = findViewById(R.id.nav_button);
    }

    /**
     * 加载必应图片
     */
    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                 e.printStackTrace();
                Toast.makeText(MyApplication.getContext(), "背景图片加载失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(
                        MyApplication.getContext()).edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(MyApplication.getContext()).load(bingPic).into(bingImage);
                    }
                });
            }
        });
    }

    /**
     * 根据天气id请求Weather数据
     */
    public void requestWeather(String weatherId) {
        //请求weather数据
        String weatherUrl = "https://free-api.heweather.com/s6/weather/now?location="+weatherId+
                "&key=c6a58c3230694b64b78facdebd7720fb";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyApplication.getContext(), "获取实时天气信息失败", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                LogUtil.d("解析后的location数据",weather.basic.cityName);
                LogUtil.d("解析后的cid数据",weather.basic.weatherId);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(
                                    MyApplication.getContext()).edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                            mWeatherId = weather.basic.weatherId;
                            LogUtil.d("实时天气返回的json数据",responseText);
                           showWeatherInfo(weather);
                        } else {
                            Toast.makeText(MyApplication.getContext(), "获取实时天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        //请求weatherForecast数据
        String weatherForecastUrl = "https://free-api.heweather.com/s6/weather/forecast?location="+weatherId+
                "&key=c6a58c3230694b64b78facdebd7720fb";
        HttpUtil.sendOkHttpRequest(weatherForecastUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyApplication.getContext(), "获取未来天气信息失败", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseForecastText = response.body().string();
                final WeatherForecast weatherForecast = Utility.handleWeatherForecastResponse(responseForecastText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weatherForecast != null && "ok".equals(weatherForecast.status)) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(
                                    MyApplication.getContext()).edit();
                            editor.putString("forecastWeather", responseForecastText);
                            editor.apply();
                            showWeatherForecastInfo(weatherForecast);
                        } else {
                            Toast.makeText(MyApplication.getContext(), "获取未来天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        //请求Air数据
        String airUrl = "https://free-api.heweather.com/s6/air/now?location="+weatherId+
        "&key=c6a58c3230694b64b78facdebd7720fb";
        HttpUtil.sendOkHttpRequest(airUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyApplication.getContext(), "F获取空气质量信息失败", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseAirText = response.body().string();
                final Air air = Utility.handleAirResponse(responseAirText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (air != null && "ok".equals(air.status)) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(
                                    MyApplication.getContext()).edit();
                            editor.putString("air", responseAirText);
                            editor.apply();
                            showAirInfo(air);

                        } else {
                            Toast.makeText(MyApplication.getContext(), "获取空气质量信息失败", Toast.LENGTH_SHORT).show();
                            aqiText.setText("");
                            pm25Text.setText("");
                        }
                    }
                });
            }
        });

        //请求lifestyle数据
        String lifestyleUrl = "https://free-api.heweather.com/s6/weather/lifestyle?location="+weatherId+
                "&key=c6a58c3230694b64b78facdebd7720fb";
        HttpUtil.sendOkHttpRequest(lifestyleUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyApplication.getContext(), "获取生活指数信息失败", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseLifeStyleText = response.body().string();
                final WeatherLifeStyle weatherLifeStyle = Utility.handleWeatherLifeStyleResponse(responseLifeStyleText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weatherLifeStyle != null && "ok".equals(weatherLifeStyle.status)) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(
                                    MyApplication.getContext()).edit();
                            editor.putString("lifestyle", responseLifeStyleText);
                            editor.apply();
                            showLifeStyleInfo(weatherLifeStyle);
                        } else {
                            Toast.makeText(MyApplication.getContext(), "获取生活指数信息失败", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });

    }

    /**
     * 处理并展示weather实体类中的数据
     */
    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        String updateTime = weather.update.updateTime;
        String degree = weather.now.temperature + "°C";
        String weatherInfo = weather.now.contText;
        titleCity.setText(cityName);
        titleUpdatetime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);

        //启动后台服务
        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);
    }

    /**
     * 处理并展示weatherForecast实体类中的数据
     */
    private void showWeatherForecastInfo(WeatherForecast weatherForecast) {
        forecastLayout.removeAllViews();
        for (Forecast forecast : weatherForecast.forecastList) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dateText = view.findViewById(R.id.tv_date);
            TextView infoText = view.findViewById(R.id.tv_info);
            TextView maxText = view.findViewById(R.id.tv_max);
            TextView minText = view.findViewById(R.id.tv_min);
            dateText.setText(forecast.date);
            infoText.setText(forecast.cond);
            maxText.setText(forecast.max);
            minText.setText(forecast.min);
            forecastLayout.addView(view);

        }
    }

    /**
     * 处理并展示Air实体类中的数据
     */
    private void showAirInfo(Air air) {
        aqiText.setText(air.airNowCity.aqi);
        pm25Text.setText(air.airNowCity.pm25);
    }

    /**
     * 处理并展示LifeStyle实体类中的数据
     */
    private void showLifeStyleInfo(WeatherLifeStyle weatherLifeStyle) {
        for (Lifestyle lifestyle : weatherLifeStyle.lifestyleList) {
            if ("comf".equals(lifestyle.lifeStyleType)) {
                comfortText.setText("舒适度:" + lifestyle.lifeStyleText);
            } else if ("cw".equals(lifestyle.lifeStyleType)) {
                carWashText.setText("洗车指数:" + lifestyle.lifeStyleText);
            } else if ("sport".equals(lifestyle.lifeStyleType)) {
                sportText.setText("运动建议:" + lifestyle.lifeStyleText);
            }
        }
        weatherLayout.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        LogUtil.d("WeatherActivity:","页面销毁了");
        super.onDestroy();
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(
                MyApplication.getContext()).edit();
        editor.clear();
        editor.apply();
    }
}

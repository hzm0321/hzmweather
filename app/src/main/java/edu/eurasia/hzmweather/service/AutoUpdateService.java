package edu.eurasia.hzmweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;

import edu.eurasia.hzmweather.MyApplication;
import edu.eurasia.hzmweather.gson.Air;
import edu.eurasia.hzmweather.gson.Weather;
import edu.eurasia.hzmweather.gson.WeatherForecast;
import edu.eurasia.hzmweather.gson.WeatherLifeStyle;
import edu.eurasia.hzmweather.log.LogUtil;
import edu.eurasia.hzmweather.util.HttpUtil;
import edu.eurasia.hzmweather.util.Utility;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AutoUpdateService extends Service {
    public AutoUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();
        updateBingPic();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        //1小时的毫秒数
        int anHour = 60 * 60 * 1000;
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, AutoUpdateService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 更新天气信息
     */
    private void updateWeather() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather", null);
        String weatherForecastString = prefs.getString("forecastWeather", null);
        String airString = prefs.getString("air", null);
        String weatherLifeStyleString = prefs.getString("lifestyle", null);
        if (weatherString != null && weatherForecastString != null &&
                airString != null && weatherLifeStyleString != null) {
            //有缓存直接解析天气数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            WeatherForecast weatherForecast = Utility.handleWeatherForecastResponse(weatherForecastString);
            Air air = Utility.handleAirResponse(airString);
            WeatherLifeStyle weatherLifeStyle = Utility.handleWeatherLifeStyleResponse(weatherLifeStyleString);
            String weatherId = weather.basic.weatherId;

            //请求weather数据
            String weatherUrl = "https://free-api.heweather.com/s6/weather/now?location="+weatherId+
                    "&key=c6a58c3230694b64b78facdebd7720fb";
            HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseText = response.body().string();
                    Weather weather = Utility.handleWeatherResponse(responseText);
                            if (weather != null && "ok".equals(weather.status)) {
                                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(
                                        MyApplication.getContext()).edit();
                                editor.putString("weather", responseText);
                                editor.apply();
                            }
                }
            });

            //请求weatherForecast数据
            String weatherForecastUrl = "https://free-api.heweather.com/s6/weather/forecast?location="+weatherId+
                    "&key=c6a58c3230694b64b78facdebd7720fb";
            HttpUtil.sendOkHttpRequest(weatherForecastUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                   e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseForecastText = response.body().string();
                    WeatherForecast weatherForecast = Utility.handleWeatherForecastResponse(responseForecastText);
                            if (weatherForecast != null && "ok".equals(weatherForecast.status)) {
                                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(
                                        MyApplication.getContext()).edit();
                                editor.putString("forecastWeather", responseForecastText);
                                editor.apply();
                            }
                }
            });

            //请求Air数据
            String airUrl = "https://free-api.heweather.com/s6/air/now?location="+weatherId+
                    "&key=c6a58c3230694b64b78facdebd7720fb";
            HttpUtil.sendOkHttpRequest(airUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String responseAirText = response.body().string();
                    final Air air = Utility.handleAirResponse(responseAirText);
                            if (air != null && "ok".equals(air.status)) {
                                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(
                                        MyApplication.getContext()).edit();
                                editor.putString("air", responseAirText);
                                editor.apply();
                            }
                }
            });

            //请求lifestyle数据
            String lifestyleUrl = "https://free-api.heweather.com/s6/weather/lifestyle?location="+weatherId+
                    "&key=c6a58c3230694b64b78facdebd7720fb";
            HttpUtil.sendOkHttpRequest(lifestyleUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String responseLifeStyleText = response.body().string();
                    final WeatherLifeStyle weatherLifeStyle = Utility.handleWeatherLifeStyleResponse(responseLifeStyleText);
                            if (weatherLifeStyle != null && "ok".equals(weatherLifeStyle.status)) {
                                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(
                                        MyApplication.getContext()).edit();
                                editor.putString("lifestyle", responseLifeStyleText);
                                editor.apply();
                            }
                }
            });
        }
    }

    /**
     * 更新必应背景
     */
    private void updateBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(
                        MyApplication.getContext()).edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
            }
        });
    }
}

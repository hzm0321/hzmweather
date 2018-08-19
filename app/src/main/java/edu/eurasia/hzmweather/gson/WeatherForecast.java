package edu.eurasia.hzmweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 类名: WeatherForecast<br>
 * 功能:(TODO用一句话描述该类的功能)<br>
 * 作者:黄振敏 <br>
 * 日期:2018/8/19 14:18
 */
public class WeatherForecast {
    public String status;
    public String date;
    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;
}

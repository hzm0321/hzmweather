package edu.eurasia.hzmweather.util;

import android.text.TextUtils;


import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.eurasia.hzmweather.db.City;
import edu.eurasia.hzmweather.db.County;
import edu.eurasia.hzmweather.db.Province;
import edu.eurasia.hzmweather.gson.Air;
import edu.eurasia.hzmweather.gson.Lifestyle;
import edu.eurasia.hzmweather.gson.Weather;
import edu.eurasia.hzmweather.gson.WeatherForecast;
import edu.eurasia.hzmweather.gson.WeatherLifeStyle;
import edu.eurasia.hzmweather.log.LogUtil;

/**
 * 类名: Utility<br>
 * 功能:(解析和处理服务器返回的数据)<br>
 * 作者:黄振敏 <br>
 * 日期:2018/8/18 14:49
 */
public class Utility {

    /**
     * 省级数据
     */
    public static boolean handleProvinceResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allProvinces = new JSONArray(response);
                for (int i = 0; i < allProvinces.length(); i++) {
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 市级数据
     */
    public static boolean handleCityResponse(String response, int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCities = new JSONArray(response);
                for (int i = 0; i < allCities.length(); i++) {
                    JSONObject cityObject = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 县级数据
     */
    public static boolean handleCountyResponse(String response, int cityId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCounties = new JSONArray(response);
                for (int i = 0; i < allCounties.length(); i++) {
                    JSONObject countyObject = allCounties.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 将返回的JSon数据解析成Weather实体类
     */
    public static Weather handleWeatherResponse(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather6");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            LogUtil.d("实时天气",weatherContent);
            //将JSon数据转换成Weather对象
            return new Gson().fromJson(weatherContent, Weather.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将返回的JSon数据解析成Air实体类
     */
    public static Air handleAirResponse(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather6");
            String airContent = jsonArray.getJSONObject(0).toString();
            LogUtil.d("空气指数",airContent);
            //将JSon数据转换成Weather对象
            return new Gson().fromJson(airContent, Air.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将返回的JSon数据解析成WeatherForecast实体类
     */
    public static WeatherForecast handleWeatherForecastResponse(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather6");
            String weatherForecastContent = jsonArray.getJSONObject(0).toString();
            LogUtil.d("未来天气",weatherForecastContent);
            //将JSon数据转换成Weather对象
            return new Gson().fromJson(weatherForecastContent, WeatherForecast.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将返回的JSon数据解析成WeatherForecast实体类
     */
    public static WeatherLifeStyle handleWeatherLifeStyleResponse(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather6");
            String lifeStyle = jsonArray.getJSONObject(0).toString();
            LogUtil.d("生活指数",lifeStyle);
            //将JSon数据转换成Weather对象
            return new Gson().fromJson(lifeStyle, WeatherLifeStyle.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

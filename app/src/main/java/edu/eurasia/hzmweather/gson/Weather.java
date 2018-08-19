package edu.eurasia.hzmweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 类名: Weather<br>
 * 功能:(引用GSon的实体类)<br>
 * 作者:黄振敏 <br>
 * 日期:2018/8/19 8:33
 */
public class Weather {
    public String status;
    public Basic basic;
    public Update update;
    public Now now;



}

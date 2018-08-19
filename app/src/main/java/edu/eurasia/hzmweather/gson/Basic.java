package edu.eurasia.hzmweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * 类名: Basic<br>
 * 功能:(Gson解析实体类)<br>
 * 作者:黄振敏 <br>
 * 日期:2018/8/19 8:10
 */
public class Basic {

    /**
     * 建立JSon和Java字段之间的映射关系
     */
    @SerializedName("location")
    public String cityName;

    @SerializedName("cid")
    public String weatherId;




}

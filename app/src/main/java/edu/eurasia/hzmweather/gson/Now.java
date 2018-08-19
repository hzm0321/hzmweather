package edu.eurasia.hzmweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * 类名: Now<br>
 * 功能:(TODO用一句话描述该类的功能)<br>
 * 作者:黄振敏 <br>
 * 日期:2018/8/19 14:41
 */
public class Now {

    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond_txt")
    public String contText;
}

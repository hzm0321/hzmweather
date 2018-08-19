package edu.eurasia.hzmweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * 类名: Forecast<br>
 * 功能:(Gson解析实体类)<br>
 * 作者:黄振敏 <br>
 * 日期:2018/8/19 10:42
 */
public class Forecast {
    public String date;

    @SerializedName("cond_txt_d")
    public String cond;

    @SerializedName("tmp_max")
    public String max;

    @SerializedName("tmp_min")
    public String min;
}

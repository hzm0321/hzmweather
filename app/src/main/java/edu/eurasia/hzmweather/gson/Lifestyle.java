package edu.eurasia.hzmweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * 类名: Lifestyle<br>
 * 功能:(TODO用一句话描述该类的功能)<br>
 * 作者:黄振敏 <br>
 * 日期:2018/8/19 16:13
 */
public class Lifestyle {

    @SerializedName("type")
    public String lifeStyleType;

    @SerializedName("brf")
    public String lifeStyleBrf;

    @SerializedName("txt")
    public String lifeStyleText;
}

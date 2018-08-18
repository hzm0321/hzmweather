package edu.eurasia.hzmweather.db;

import org.litepal.crud.LitePalSupport;

/**
 * 类名: City<br>
 * 功能:(城市实体类)<br>
 * 作者:黄振敏 <br>
 * 日期:2018/8/18 14:21
 */
public class City extends LitePalSupport {

    private int id;
    private String cityName;
    private int cityCode;
    private int provinceId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
}

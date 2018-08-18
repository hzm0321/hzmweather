package edu.eurasia.hzmweather.db;

import org.litepal.crud.LitePalSupport;

/**
 * 类名: Province<br>
 * 功能:(省份实体类)<br>
 * 作者:黄振敏 <br>
 * 日期:2018/8/18 14:17
 */
public class Province extends LitePalSupport {

    private int id;
    private String provinceName;
    private int provinceCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }
}

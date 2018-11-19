package com.suntiago.filterview.flowLayout.filter;

/**
 * Created by zaiyu on 2016/8/8.
 * 数据类
 */
public class FilterData {
    public static String TITLE = "title";
    public static String VALUE = "value";
    String name;
    String value;
    String type;//title/content
    boolean enable = true;

    public FilterData(String name, String value, String type) {
        this.name = name;
        this.value = value;
        this.type = type;
    }

    public FilterData(String name, String value) {
        this.name = name;
        this.value = value;
        this.type = FilterData.VALUE;
    }

    public FilterData(String name, String value, boolean enable) {
        this.name = name;
        this.value = value;
        this.enable = enable;
        this.type = FilterData.VALUE;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}

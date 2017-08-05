package com.zhongzhiyijian.eyan.entity;

/**
 * Created by Administrator on 2016/9/12.
 */
public class Integral {

    /**
     * integral : 2
     * sid : 1
     * time : 1472104861
     * type : 2
     * value : 消费
     */

    private int integral;
    private long time;
    private int type;
    private String value;

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Integral{" +
                "integral=" + integral +
                ", time=" + time +
                ", type=" + type +
                ", value='" + value + '\'' +
                '}';
    }
}

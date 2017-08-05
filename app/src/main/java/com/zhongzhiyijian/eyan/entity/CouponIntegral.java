package com.zhongzhiyijian.eyan.entity;

/**
 * Created by Administrator on 2016/11/20.
 */

public class CouponIntegral extends BaseEntity {

    private int couponconstant_id;
    private int integral;
    private long time;
    private String value1;
    private String value2;

    public int getCouponconstant_id() {
        return couponconstant_id;
    }

    public void setCouponconstant_id(int couponconstant_id) {
        this.couponconstant_id = couponconstant_id;
    }

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

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    @Override
    public String toString() {
        return "CouponIntegral{" +
                "couponconstant_id=" + couponconstant_id +
                ", integral=" + integral +
                ", time=" + time +
                ", value1='" + value1 + '\'' +
                ", value2='" + value2 + '\'' +
                '}';
    }
}

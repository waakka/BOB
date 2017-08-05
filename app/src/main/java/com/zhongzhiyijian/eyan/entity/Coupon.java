package com.zhongzhiyijian.eyan.entity;

/**
 * Created by Administrator on 2016/9/12.
 */
public class Coupon {

    /**
     * sid : 1
     * term_time : 1475254861
     * type : 1
     * uid : 0
     * value1 : 100
     * value2 : 20
     */

    private long term_time;
    private int type;
    private int value1;
    private int value2;
    private int state;
    private int sid;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public long getTerm_time() {
        return term_time;
    }

    public void setTerm_time(long term_time) {
        this.term_time = term_time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getValue1() {
        return value1;
    }

    public void setValue1(int value1) {
        this.value1 = value1;
    }

    public int getValue2() {
        return value2;
    }

    public void setValue2(int value2) {
        this.value2 = value2;
    }

    @Override
    public String toString() {
        return "Coupon{" +
                "term_time=" + term_time +
                ", type=" + type +
                ", value1=" + value1 +
                ", value2=" + value2 +
                ", state=" + state +
                ", sid=" + sid +
                '}';
    }
}

package com.zhongzhiyijian.eyan.entity;

/**
 * Created by Administrator on 2016/9/12.
 */
public class Msg extends BaseEntity{

    /**
     * name : 公职
     * sid : 4
     * time : 1472104937
     * type : 3
     * value : 阿嫂缩短发撒旦缩短发撒旦
     */

    private String name;
    private long time;
    private int type;
    private int sid;
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    @Override
    public String toString() {
        return "Msg{" +
                "name='" + name + '\'' +
                ", time=" + time +
                ", type=" + type +
                ", sid=" + sid +
                ", value='" + value + '\'' +
                '}';
    }
}

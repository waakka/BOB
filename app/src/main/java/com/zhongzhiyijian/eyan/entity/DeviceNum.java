package com.zhongzhiyijian.eyan.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2016/10/22.
 */
@Table(name = "bt_device_num")
public class DeviceNum {
    @Column(name = "id",isId = true,autoGen = true)
    private int id;
    @Column(name = "did")
    private int did;
    @Column(name = "name")
    private String name;
    @Column(name = "time")
    private long time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDid() {
        return did;
    }

    public void setDid(int did) {
        this.did = did;
    }

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

    @Override
    public String toString() {
        return "DeviceNum{" +
                "did=" + did +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", time=" + time +
                '}';
    }
}

package com.zhongzhiyijian.eyan.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2016/12/31.
 */
@Table(name = "userData")
public class UserData extends BaseEntity {
    @Column(name = "_id",isId = true,autoGen = true)
    private int id;
    /**
     * 模式
     */
    @Column(name = "pattern")
    private String pattern;
    /**
     * 强度
     */
    @Column(name = "strength")
    private int strength;
    /**
     * 起始时间
     */
    @Column(name = "start_time")
    private long start_time;
    /**
     * 终止时间
     */
    @Column(name = "stop_time")
    private long stop_time;
    /**
     * 是否上传
     */
    @Column(name = "isUpdate")
    private boolean isUpdate;
    /**
     * 用户token
     */
    @Column(name = "token")
    private String token;
    /**
     * 用户sid
     */
    @Column(name = "sid")
    private int sid;
    /**
     * 设备id
     */
    @Column(name = "equipment_id")
    private String equipment_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public long getStop_time() {
        return stop_time;
    }

    public void setStop_time(long stop_time) {
        this.stop_time = stop_time;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getEquipment_id() {
        return equipment_id;
    }

    public void setEquipment_id(String equipment_id) {
        this.equipment_id = equipment_id;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "id=" + id +
                ", 模式='" + pattern + '\'' +
                ", 强度=" + strength +
                ", 开始时间=" + start_time +
                ", 结束时间=" + stop_time +
                ", 是否上传=" + isUpdate +
                ", token='" + token + '\'' +
                ", sid=" + sid +
                ", equipment_id='" + equipment_id + '\'' +
                '}';
    }
}

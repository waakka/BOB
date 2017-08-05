package com.zhongzhiyijian.eyan.entity;

/**
 * Created by Administrator on 2016/9/7.
 */
public class User {
    private String token;
    private int sid;
    private String phone;
    private String email;
    private String clientid;

    public User() {
    }

    public User(String token, int sid,String phone,String email,String clientid) {
        this.token = token;
        this.phone = phone;
        this.sid = sid;
        this.email = email;
        this.clientid = clientid;
    }

    public String getClientid() {
        return clientid;
    }

    public void setClientid(String clientid) {
        this.clientid = clientid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    @Override
    public String toString() {
        return "User{" +
                "token='" + token + '\'' +
                ", sid=" + sid +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", clientid='" + clientid + '\'' +
                '}';
    }
}

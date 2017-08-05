package com.zhongzhiyijian.eyan.entity;

/**
 * Created by Administrator on 2016/9/9.
 */
public class UserInfo {

    /**
     * address :
     * birthday : 0
     * blood_type :
     * name :
     * occupation :
     * sex : 0
     * sid : 1
     * url :
     */

    private String address;
    private long birthday;
    private String blood_type;
    private String name;
    private String occupation;
    private int sex;
    private String url;
    private String mailbox;

    public UserInfo() {
    }

    public UserInfo(String address, long birthday, String blood_type, String name, String occupation, int sex, String url,String mailbox) {
        this.address = address;
        this.birthday = birthday;
        this.blood_type = blood_type;
        this.name = name;
        this.occupation = occupation;
        this.sex = sex;
        this.url = url;
        this.mailbox = mailbox;
    }

    public String getMailbox() {
        return mailbox;
    }

    public void setMailbox(String mailbox) {
        this.mailbox = mailbox;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public String getBlood_type() {
        return blood_type;
    }

    public void setBlood_type(String blood_type) {
        this.blood_type = blood_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "address='" + address + '\'' +
                ", birthday=" + birthday +
                ", blood_type='" + blood_type + '\'' +
                ", name='" + name + '\'' +
                ", occupation='" + occupation + '\'' +
                ", sex=" + sex +
                ", url='" + url + '\'' +
                ", mailbox='" + mailbox + '\'' +
                '}';
    }
}

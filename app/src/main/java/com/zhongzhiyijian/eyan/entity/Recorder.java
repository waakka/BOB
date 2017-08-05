package com.zhongzhiyijian.eyan.entity;

import java.util.List;

/**
 * Created by baby on 2016/12/19.
 */

public class Recorder {

    /**
     * lj : 222  累计使用天数
     * pjsc : 11900 平均使用时长
     * pm : 99  排名
     * list : [{"date":20161130,"duration":11900}]
     * return_code : 1000
     * ljsc : 11900 累计使用时长
     * lx : 21  连续使用天数
     */

    private int lj;
    private int pjsc;
    private int pm;
    private String return_code;
    private int ljsc;
    private int lx;
    private List<ListBean> list;

    public int getLj() {
        return lj;
    }

    public void setLj(int lj) {
        this.lj = lj;
    }

    public int getPjsc() {
        return pjsc;
    }

    public void setPjsc(int pjsc) {
        this.pjsc = pjsc;
    }

    public int getPm() {
        return pm;
    }

    public void setPm(int pm) {
        this.pm = pm;
    }

    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public int getLjsc() {
        return ljsc;
    }

    public void setLjsc(int ljsc) {
        this.ljsc = ljsc;
    }

    public int getLx() {
        return lx;
    }

    public void setLx(int lx) {
        this.lx = lx;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * date : 20161130
         * duration : 11900
         */

        private int date;
        private int duration;

        public int getDate() {
            return date;
        }

        public void setDate(int date) {
            this.date = date;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }
    }
}

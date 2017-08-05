package com.zhongzhiyijian.eyan.entity;

import java.util.List;

/**
 * Created by Administrator on 2016/9/12.
 */
public class Test {

    /**
     * listc : [{"sid":1,"term_time":1475254861,"type":1,"uid":0,"value1":100,"value2":20},{"sid":2,"term_time":1472104889,"type":1,"uid":0,"value1":100,"value2":20}]
     * return_code : 1000
     */

    private String return_code;
    /**
     * sid : 1
     * term_time : 1475254861
     * type : 1
     * uid : 0
     * value1 : 100
     * value2 : 20
     */

    private List<ListcBean> listc;

    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public List<ListcBean> getListc() {
        return listc;
    }

    public void setListc(List<ListcBean> listc) {
        this.listc = listc;
    }

    public static class ListcBean {
        private int sid;
        private int term_time;
        private int type;
        private int uid;
        private int value1;
        private int value2;

        public int getSid() {
            return sid;
        }

        public void setSid(int sid) {
            this.sid = sid;
        }

        public int getTerm_time() {
            return term_time;
        }

        public void setTerm_time(int term_time) {
            this.term_time = term_time;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
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
    }

    @Override
    public String toString() {
        return "Test{" +
                "listc=" + listc +
                ", return_code='" + return_code + '\'' +
                '}';
    }
}

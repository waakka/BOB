package com.zhongzhiyijian.eyan.util;

import com.orhanobut.logger.Logger;

/**
 * Created by Administrator on 2016/9/1.
 */
public class MsgUtil {

    public static final  int TYPE_ZHENJIU = 1;
    public static final  int TYPE_ANMO = 2;
    public static final  int TYPE_LILIAO = 3;
    public static final  int TYPE_YUELIAO = 4;

    public static byte[] getBytesQuary(String num,String type){
        long a = Long.parseLong("A5",16);
        long b = Long.parseLong("5A",16);
        long c = Long.parseLong("04",16);
        long d = Long.parseLong(num,16);
        long e = Long.parseLong("F5",16);
        long f = Long.parseLong(type,16);
        long h = a+b+c+d+e+f;
        String result = Long.toHexString(h);
        if(result.length() >2){
            result = result.substring(result.length()-2, result.length());
        }
        System.out.println(f);
        System.out.println(result);
        byte ba = (byte) Integer.parseInt("A5", 16);
        byte bb = (byte) Integer.parseInt("5A", 16);
        byte bc = (byte) Integer.parseInt("04", 16);
        byte bd = (byte) Integer.parseInt(num, 16);
        byte be = (byte) Integer.parseInt("F5", 16);
        byte bf = (byte) Integer.parseInt(type, 16);
        byte bh = (byte) Integer.parseInt(result, 16);
        byte[] bs = new byte[]{ba,bb,bc,bd,be,bf,bh};
        Logger.e("当前执行查询命令===[" + "A5" + " 5A" + " 04 " + num + " F5 " + type + " " + result + "]");
        return bs;
    }

    public static byte[] getResetByte(){
        long a = Long.parseLong("A5",16);
        long b = Long.parseLong("5A",16);
        long c = Long.parseLong("03",16);
        long d = Long.parseLong("AA",16);
        long e = Long.parseLong("FC",16);
        long h = a+b+c+d+e;
        String result = Long.toHexString(h);
        if(result.length() >2){
            result = result.substring(result.length()-2, result.length());
        }
        byte ba = (byte) Integer.parseInt("A5", 16);
        byte bb = (byte) Integer.parseInt("5A", 16);
        byte bc = (byte) Integer.parseInt("03", 16);
        byte bd = (byte) Integer.parseInt("AA", 16);
        byte be = (byte) Integer.parseInt("FC", 16);
        byte bh = (byte) Integer.parseInt(result, 16);
        byte[] bs = new byte[]{ba,bb,bc,bd,be,bh};
        Logger.e("发送复位命令");
        return bs;
    }


    public static byte[] getId(){
        return getBytesQuary("01","01");
    }
    public static byte[] getCurWorkType(){
        return getBytesQuary("02","02");
    }
    public static byte[] getPower(){
        return getBytesQuary("04","04");
    }



    public static byte[] openWork(String num,int qiangdu,int time,int type){
        String t = "";
        switch (type){
            case 1:
                t = "F1";
                break;
            case 2:
                t = "F2";
                break;
            case 3:
                t = "F3";
                break;
            case 4:
                t = "F4";
                break;
        }
        return openWork(num,qiangdu,time,t);
    }

    public static byte[] close1(){
        return closeWork("01",1);
    }
    public static byte[] close2(){
        return closeWork("02",2);
    }
    public static byte[] close3(){
        return closeWork("03",3);
    }
    public static byte[] close4(){
        return closeWork("04",4);
    }

    public static byte[] closeWork(String num,int type){
        String t = "";
        switch (type){
            case 1:
                t = "01";
                break;
            case 2:
                t = "02";
                break;
            case 3:
                t = "03";
                break;
            case 4:
                t = "04";
                break;
        }
        return openWork(num,0,0,t);
    }


    public static byte[] setIntensity(String num,int qiangdu){
        String intensity = Integer.toHexString(qiangdu);
        if(intensity.length() >2){
            intensity = intensity.substring(intensity.length()-2, intensity.length());
        }else if(intensity.length() == 1){
            intensity = "0" + intensity;
        }
        return getBytesWorkType(num,"00",intensity,"00" ,"00");
    }


    public static byte[] openWork(String num,int qiangdu,int time,String type){
        String intensity = Integer.toHexString(qiangdu);
        if(intensity.length() >2){
            intensity = intensity.substring(intensity.length()-2, intensity.length());
        }else if(intensity.length() == 1){
            intensity = "0" + intensity;
        }

        int highTime = time / 256;
        int lowTime = time % 256;

        String time1 = Integer.toHexString(highTime);
        String time2 = Integer.toHexString(lowTime);
        if(time1.length() >2){
            time1 = time1.substring(time1.length()-2, time1.length());
        }else if(time1.length() == 1){
            time1 = "0" + time1;
        } if(time2.length() >2){
            time2 = time2.substring(time2.length()-2, time2.length());
        }else if(time2.length() == 1){
            time2 = "0" + time2;
        }
        return getBytesWorkType(num,type,intensity,time1,time2);
    }


    /**
     * 获取命令
     * @param num       命令序号
     * @param workType  工作模式
     * @param qiangdu   工作强度
     * @param time1     计时1
     * @return
     */
    public static byte[] getBytesWorkType(
            String num,String workType,String qiangdu,String time1,String time2){
        long a = Long.parseLong("A5",16);
        long b = Long.parseLong("5A",16);
        long c = Long.parseLong("07",16);
        long d = Long.parseLong(num,16);
        long e = Long.parseLong("F1",16);
        long f = Long.parseLong(workType,16);
        long g = Long.parseLong(qiangdu,16);
        long h = Long.parseLong(time1,16);
        long i = Long.parseLong(time2,16);
        long k = a+b+c+d+e+f+g+h+i;
        String result = Long.toHexString(k);
        if(result.length() >2){
            result = result.substring(result.length()-2, result.length());
        }else if(result.length() == 1){
            result = "0" + result;
        }
        byte ba = (byte) Integer.parseInt("A5", 16);
        byte bb = (byte) Integer.parseInt("5A", 16);
        byte bc = (byte) Integer.parseInt("07", 16);
        byte bd = (byte) Integer.parseInt(num, 16);
        byte be = (byte) Integer.parseInt("F1", 16);
        byte bf = (byte) Integer.parseInt(workType, 16);
        byte bg = (byte) Integer.parseInt(qiangdu, 16);
        byte bh = (byte) Integer.parseInt(time1, 16);
        byte bi = (byte) Integer.parseInt(time2, 16);
        byte bk = (byte) Integer.parseInt(result, 16);
        byte[] bs = new byte[]{ba,bb,bc,bd,be,bf,bg,bh,bi,bk};
        Logger.e("当前执行命令===[" + "A5" + " 5A" + " 07" + " " + num + " F1"
                + " " + workType + " " + qiangdu + " " + time1 + " " + time2 + " " + result + "]");
        return bs;
    }



    public static byte[] getXinTiao(){
        long a = Long.parseLong("A5",16);
        long b = Long.parseLong("5A",16);
        long c = Long.parseLong("03",16);
        long d = Long.parseLong("01",16);
        long e = Long.parseLong("f2",16);
        long f = a+b+c+d+e;
        String result = Long.toHexString(f);
        if(result.length() >2){
            result = result.substring(result.length()-2, result.length());
        }
        System.out.println(f);
        System.out.println(result);
        byte ba = (byte) Integer.parseInt("A5", 16);
        byte bb = (byte) Integer.parseInt("5A", 16);
        byte bc = (byte) Integer.parseInt("03", 16);
        byte bd = (byte) Integer.parseInt("01", 16);
        byte be = (byte) Integer.parseInt("f2", 16);
        byte bf = (byte) Integer.parseInt(result, 16);
        byte[] bs = new byte[]{ba,bb,bc,bd,be,bf};
        return bs;
    }


}

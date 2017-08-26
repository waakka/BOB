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

    public static byte[] getBytes(String strC,String strD,String strE){
        long a = Long.parseLong("A5",16);
        long b = Long.parseLong("5A",16);
        long c = Long.parseLong(strC,16);
        long d = Long.parseLong(strD,16);
        long e = Long.parseLong(strE,16);
        long f = a+b+c+d+e;
        String result = Long.toHexString(f);
        if(result.length() >2){
            result = result.substring(result.length()-2, result.length());
        }
        System.out.println(f);
        System.out.println(result);
        byte ba = (byte) Integer.parseInt("A5", 16);
        byte bb = (byte) Integer.parseInt("5A", 16);
        byte bc = (byte) Integer.parseInt(strC, 16);
        byte bd = (byte) Integer.parseInt(strD, 16);
        byte be = (byte) Integer.parseInt(strE, 16);
        byte bf = (byte) Integer.parseInt(result, 16);
        byte[] bs = new byte[]{ba,bb,bc,bd,be,bf};
        Logger.e("当前执行命令===[" + "A5" + " 5A" + " " + strC + " " + strD + " " + strE + "]");
        return bs;
    }


    public static byte[] getId(){
        return getBytes("04","F4","01");
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
                t = "F5";
                break;
        }
        return openWork(num,qiangdu,time,t);
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
                t = "05";
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
        return getBytesWorkType(num,"00",intensity,"0000");
    }


    public static byte[] openWork(String num,int qiangdu,int time,String type){
        String intensity = Integer.toHexString(qiangdu);
        if(intensity.length() >2){
            intensity = intensity.substring(intensity.length()-2, intensity.length());
        }else if(intensity.length() == 1){
            intensity = "0" + intensity;
        }

        Logger.e("转换前时间===" + time);
        String times = Integer.toHexString(time);
        Logger.e("转换后时间===" + times);
        if(times.length() >4){
            times = times.substring(times.length()-4, times.length());
        }else if(times.length() == 3){
            times = "0" + times;
        }else if(times.length() == 2){
            times = "00" + times;
        }else if(times.length() == 1){
            times = "000" + times;
        }else if(times.length() == 0){
            times = "0000";
        }
        Logger.e("转换后时间===" + times);


        return getBytesWorkType(num,type,intensity,times);
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
            String num,String workType,String qiangdu,String time1){
        long a = Long.parseLong("A5",16);
        long b = Long.parseLong("5A",16);
        long c = Long.parseLong("07",16);
        long d = Long.parseLong(num,16);
        long e = Long.parseLong("F1",16);
        long f = Long.parseLong(workType,16);
        long g = Long.parseLong(qiangdu,16);
        long h = Long.parseLong(time1,16);
        long k = a+b+c+d+e+f+g+h;
        String result = Long.toHexString(f);
        if(result.length() >2){
            result = result.substring(result.length()-2, result.length());
        }else if(result.length() == 1){
            result = "0" + result;
        }
        System.out.println(f);
        System.out.println(result);
        byte ba = (byte) Integer.parseInt("A5", 16);
        byte bb = (byte) Integer.parseInt("5A", 16);
        byte bc = (byte) Integer.parseInt("07", 16);
        byte bd = (byte) Integer.parseInt(num, 16);
        byte be = (byte) Integer.parseInt("F1", 16);
        byte bf = (byte) Integer.parseInt(workType, 16);
        byte bg = (byte) Integer.parseInt(qiangdu, 16);
        byte bh = (byte) Integer.parseInt(time1, 16);
        byte bk = (byte) Integer.parseInt(result, 16);
        byte[] bs = new byte[]{ba,bb,bc,bd,be,bf,bg,bh,bk};
        Logger.e("当前执行命令===[" + "A5" + " 5A" + " 07" + " " + num + " F1"
                + " " + workType + " " + qiangdu+ " " + time1 + " " + result + "]");
        return bs;
    }


}

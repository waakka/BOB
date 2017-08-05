package com.zhongzhiyijian.eyan.util;

/**
 * Created by Administrator on 2016/9/1.
 */
public class MsgUtil {

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
        return bs;
    }

    public static byte[] closeMode(){
        return getBytes("03","F1","00");
    }

    public static byte[] openMode(){
        return getBytes("03","F1","F0");
    }

    public static byte[] zhenjiuMode(){
        return getBytes("03","F1","01");
    }

    public static byte[] anmoMode(){
        return getBytes("03","F1","02");
    }

    public static byte[] liliaoMode(){
        return getBytes("03","F1","03");
    }

    public static byte[] yueliaoMode(){
        return getBytes("03","F1","04");
    }

    public static byte[] setIntensity(int i){
        String intensity = Integer.toHexString(i);
        if(intensity.length() >2){
            intensity = intensity.substring(intensity.length()-2, intensity.length());
        }else if(intensity.length() == 1){
            intensity = "0" + intensity;
        }
        return getBytes("03","F3",intensity);
    }
    public static byte[] setFrequency(int i){
        i /= 10;
        String frequency = Integer.toHexString(i);
        if(frequency.length() >2){
            frequency = frequency.substring(frequency.length()-2, frequency.length());
        }else if(frequency.length() == 1){
            frequency = "0" + frequency;
        }
        return getBytes("03","F4",frequency);
    }

    public static byte[] getId(){
        return getBytes("03","F5","01");
    }
}

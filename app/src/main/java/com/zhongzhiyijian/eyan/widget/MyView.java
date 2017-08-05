package com.zhongzhiyijian.eyan.widget;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zhongzhiyijian.eyan.R;

/**
 * Created by Administrator on 2017/3/15.
 */

public class MyView implements View.OnClickListener{

    View rootView;
    Context mContext;
    LayoutInflater inflater;

    ImageView iv1;
    ImageView iv2;
    ImageView iv3;
    ImageView iv4;
    ImageView iv5;
    ImageView iv6;
    ImageView iv7;
    ImageView iv8;
    ImageView iv9;
    ImageView iv10;
    ImageView iv11;
    LinearLayout ll1;
    LinearLayout ll2;
    LinearLayout ll3;
    LinearLayout ll4;
    LinearLayout ll5;
    LinearLayout ll6;
    LinearLayout ll7;
    LinearLayout ll8;
    LinearLayout ll9;
    LinearLayout ll10;
    LinearLayout ll11;

    ImageButton btnSubtract,btnPlus;

    private static MyView myView;

    private int position;
    private boolean isOpen;

    OnItemCheckedChangedListener listener;


    private MyView(View view) {
        rootView = view;
        btnSubtract = (ImageButton) rootView.findViewById(R.id.ib_subtract);
        btnPlus = (ImageButton) rootView.findViewById(R.id.ib_plus);
        iv1 = (ImageView) rootView.findViewById(R.id.ib_s1);
        iv2 = (ImageView) rootView.findViewById(R.id.ib_s2);
        iv3 = (ImageView) rootView.findViewById(R.id.ib_s3);
        iv4 = (ImageView) rootView.findViewById(R.id.ib_s4);
        iv5 = (ImageView) rootView.findViewById(R.id.ib_s5);
        iv6 = (ImageView) rootView.findViewById(R.id.ib_s6);
        iv7 = (ImageView) rootView.findViewById(R.id.ib_s7);
        iv8 = (ImageView) rootView.findViewById(R.id.ib_s8);
        iv9 = (ImageView) rootView.findViewById(R.id.ib_s9);
        iv10 = (ImageView) rootView.findViewById(R.id.ib_s10);
        iv11 = (ImageView) rootView.findViewById(R.id.ib_s11);
        ll1 = (LinearLayout) rootView.findViewById(R.id.ll_s1);
        ll2 = (LinearLayout) rootView.findViewById(R.id.ll_s2);
        ll3 = (LinearLayout) rootView.findViewById(R.id.ll_s3);
        ll4 = (LinearLayout) rootView.findViewById(R.id.ll_s4);
        ll5 = (LinearLayout) rootView.findViewById(R.id.ll_s5);
        ll6 = (LinearLayout) rootView.findViewById(R.id.ll_s6);
        ll7 = (LinearLayout) rootView.findViewById(R.id.ll_s7);
        ll8 = (LinearLayout) rootView.findViewById(R.id.ll_s8);
        ll9 = (LinearLayout) rootView.findViewById(R.id.ll_s9);
        ll10 = (LinearLayout) rootView.findViewById(R.id.ll_s10);
        ll11 = (LinearLayout) rootView.findViewById(R.id.ll_s11);

        ll1.setOnClickListener(this);
        ll2.setOnClickListener(this);
        ll3.setOnClickListener(this);
        ll4.setOnClickListener(this);
        ll5.setOnClickListener(this);
        ll6.setOnClickListener(this);
        ll7.setOnClickListener(this);
        ll8.setOnClickListener(this);
        ll9.setOnClickListener(this);
        ll10.setOnClickListener(this);
        ll11.setOnClickListener(this);

        btnPlus.setOnClickListener(this);
        btnSubtract.setOnClickListener(this);

        position = 0;
    }

    public static MyView getInstance(View v){
        if (myView == null){
            synchronized (MyView.class){
                myView = new MyView(v);
            }
        }
        return myView;
    }

    public interface OnItemCheckedChangedListener{
        void itemCheckedChangedListener(int position);
    }

    public void setOnItemCheckedChangedListener(OnItemCheckedChangedListener listener){
        this.listener = listener;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
        if (isOpen){
            Log.e("TAG","open");
            btnPlus.setBackgroundResource(R.mipmap.btn_jia_checked);
            btnSubtract.setBackgroundResource(R.mipmap.btn_jian_checked);
            setPosition(position);
        }else{
            Log.e("TAG","closed");
            btnPlus.setBackgroundResource(R.mipmap.btn_jia_nomal);
            btnSubtract.setBackgroundResource(R.mipmap.btn_jian_nomal);
            clearBg();
        }
    }

    public void setPosition(int position){
        this.position = position;
        clearBg();
        Log.e("TAG","position = " + position);
        switch (position){
            case 0:
                if (isOpen){
                    iv1.setBackgroundResource(R.mipmap.y1);
                }else{
                    iv1.setBackgroundResource(R.mipmap.l1);
                }
                break;
            case 1:
                if (isOpen){
                    iv2.setBackgroundResource(R.mipmap.y2);
                }else{
                    iv2.setBackgroundResource(R.mipmap.l2);
                }
                break;
            case 2:
                if (isOpen){
                    iv3.setBackgroundResource(R.mipmap.y3);
                }else{
                    iv3.setBackgroundResource(R.mipmap.l3);
                }
                break;
            case 3:
                if (isOpen){
                    iv4.setBackgroundResource(R.mipmap.y4);
                }else{
                    iv4.setBackgroundResource(R.mipmap.l4);
                }
                break;
            case 4:
                if (isOpen){
                    iv5.setBackgroundResource(R.mipmap.y5);
                }else{
                    iv5.setBackgroundResource(R.mipmap.l5);
                }
                break;
            case 5:
                if (isOpen){
                    iv6.setBackgroundResource(R.mipmap.y6);
                }else{
                    iv6.setBackgroundResource(R.mipmap.l6);
                }
                break;
            case 6:
                if (isOpen){
                    iv7.setBackgroundResource(R.mipmap.y7);
                }else{
                    iv7.setBackgroundResource(R.mipmap.l7);
                }
                break;
            case 7:
                if (isOpen){
                    iv8.setBackgroundResource(R.mipmap.y8);
                }else{
                    iv8.setBackgroundResource(R.mipmap.l8);
                }
                break;
            case 8:
                if (isOpen){
                    iv9.setBackgroundResource(R.mipmap.y9);
                }else{
                    iv9.setBackgroundResource(R.mipmap.l9);
                }
                break;
            case 9:
                if (isOpen){
                    iv10.setBackgroundResource(R.mipmap.y10);
                }else{
                    iv10.setBackgroundResource(R.mipmap.l10);
                }
                break;
            case 10:
                if (isOpen){
                    iv11.setBackgroundResource(R.mipmap.y11);
                }else{
                    iv11.setBackgroundResource(R.mipmap.l11);
                }
                break;
        }
        if (listener != null){
            listener.itemCheckedChangedListener(position);
        }
    }

    public int getPosition() {
        return position;
    }

    private void clearBg(){
        iv1.setBackgroundResource(R.mipmap.g1);
        iv2.setBackgroundResource(R.mipmap.g2);
        iv3.setBackgroundResource(R.mipmap.g3);
        iv4.setBackgroundResource(R.mipmap.g4);
        iv5.setBackgroundResource(R.mipmap.g5);
        iv6.setBackgroundResource(R.mipmap.g6);
        iv7.setBackgroundResource(R.mipmap.g7);
        iv8.setBackgroundResource(R.mipmap.g8);
        iv9.setBackgroundResource(R.mipmap.g9);
        iv10.setBackgroundResource(R.mipmap.g10);
        iv11.setBackgroundResource(R.mipmap.g11);
    }

    @Override
    public void onClick(View v) {
        if (!isOpen){
            Log.e("TAG","isOpen = false");
            return;
        }
        switch (v.getId()){
            case R.id.ll_s1:
                setPosition(0);
                break;
            case R.id.ll_s2:
                setPosition(1);
                break;
            case R.id.ll_s3:
                setPosition(2);
                break;
            case R.id.ll_s4:
                setPosition(3);
                break;
            case R.id.ll_s5:
                setPosition(4);
                break;
            case R.id.ll_s6:
                setPosition(5);
                break;
            case R.id.ll_s7:
                setPosition(6);
                break;
            case R.id.ll_s8:
                setPosition(7);
                break;
            case R.id.ll_s9:
                setPosition(8);
                break;
            case R.id.ll_s10:
                setPosition(9);
                break;
            case R.id.ll_s11:
                setPosition(10);
                break;
            case R.id.ib_plus:
                if (position >= 10){
                    return;
                }
                position += 1;
                setPosition(position);
                break;
            case R.id.ib_subtract:
                if (position <= 0){
                    return;
                }
                position -= 1;
                setPosition(position);
                break;
        }
    }
}

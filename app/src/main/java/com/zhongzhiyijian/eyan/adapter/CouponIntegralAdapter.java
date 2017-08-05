package com.zhongzhiyijian.eyan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.zhongzhiyijian.eyan.R;
import com.zhongzhiyijian.eyan.entity.CouponIntegral;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Administrator on 2016/9/12.
 */
public class CouponIntegralAdapter extends BaseAdapter {

    private Context context;
    private List<CouponIntegral> data;
    private int integral;
    private OnDuiHUanClickLenstener lenstener;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public CouponIntegralAdapter(Context context, List<CouponIntegral> data,int integral,OnDuiHUanClickLenstener lenstener) {
        this.context = context;
        this.data = data;
        this.integral = integral;
        this.lenstener = lenstener;
    }

    public interface OnDuiHUanClickLenstener{
        abstract void getClicked(int position);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder{
        TextView tvValue1,tvValue2,tvTime,tvIntegral;
        Button btnGet;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_coupon_integral, null);
            holder = new ViewHolder();
            holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tvValue1 = (TextView) convertView.findViewById(R.id.tv_value1);
            holder.tvValue2 = (TextView) convertView.findViewById(R.id.tv_value2);
            holder.tvIntegral = (TextView) convertView.findViewById(R.id.tv_integral);
            holder.btnGet = (Button) convertView.findViewById(R.id.btn_get);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        CouponIntegral ci = data.get(position);
        Logger.e("优惠券===" + ci.toString());

        holder.tvValue1.setText(context.getString(R.string.duihuan4) + ci.getValue1() + context.getString(R.string.duihuan3));
        holder.tvValue2.setText(context.getString(R.string.duihuan2) + ci.getValue2()+context.getString(R.string.duihuan3));
        long days = ci.getTime() / 60 / 60 / 24;
        holder.tvTime.setText(context.getString(R.string.duihuan7) + days +context.getString(R.string.duihuan8));
        holder.tvIntegral.setText(ci.getIntegral() + context.getString(R.string.duihuan6));

//        holder.tvValue1.setText("使用条件：单笔订单达" + ci.getValue1() + "元");
//        holder.tvValue2.setText("优惠券：" + ci.getValue2() + "元");
//        long days = ci.getTime() / 60 / 60 / 24;
//        holder.tvTime.setText("有效期：自兑换之日起" + days + "天内有效");
//        holder.tvIntegral.setText(ci.getIntegral() + "积分");

        if (integral >= ci.getIntegral()){
            holder.btnGet.setBackgroundResource(R.drawable.bg_btn_duihuan);
            holder.btnGet.setClickable(true);
        }else{
            holder.btnGet.setBackgroundResource(R.drawable.bg_btn_duihuan_dark);
            holder.btnGet.setClickable(false);
        }

        holder.btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lenstener.getClicked(position);
            }
        });

        return convertView;
    }
}

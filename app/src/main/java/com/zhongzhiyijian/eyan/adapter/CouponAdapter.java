package com.zhongzhiyijian.eyan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongzhiyijian.eyan.R;
import com.zhongzhiyijian.eyan.entity.Coupon;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/9/12.
 */
public class CouponAdapter extends BaseAdapter {

    private Context context;
    private List<Coupon> data;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public CouponAdapter(Context context, List<Coupon> data) {
        this.context = context;
        this.data = data;
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
        TextView tvValue1,tvValue2,tvTime,tvSid,tvState;
        RelativeLayout left;
        LinearLayout right,bottom;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_coupon, null);
            holder = new ViewHolder();
            holder.tvSid = (TextView) convertView.findViewById(R.id.tv_sid);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tv_term_time);
            holder.tvState = (TextView) convertView.findViewById(R.id.tv_state);
            holder.tvValue1 = (TextView) convertView.findViewById(R.id.tv_value1);
            holder.tvValue2 = (TextView) convertView.findViewById(R.id.tv_value2);
            holder.left = (RelativeLayout) convertView.findViewById(R.id.rl_coupon_left);
            holder.right = (LinearLayout) convertView.findViewById(R.id.ll_coupon_right);
            holder.bottom = (LinearLayout) convertView.findViewById(R.id.ll_coupon_bottpom);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Coupon coupon = data.get(position);
        long time = System.currentTimeMillis();
        if (coupon.getState() == 1 ){
            //未使用
            if(coupon.getTerm_time()*1000 > time){
                holder.left.setBackgroundResource(R.drawable.bg_coupon_left1);
                holder.right.setBackgroundResource(R.drawable.bg_coupon_right1);
                holder.bottom.setBackgroundResource(R.drawable.bg_coupon_bottom1);
                holder.tvState.setText(R.string.notused);
                holder.tvState.setTextColor(0xff3ad168);
                holder.tvValue2.setTextColor(0xff3ad168);
            }else{
                holder.left.setBackgroundResource(R.drawable.bg_coupon_left2);
                holder.right.setBackgroundResource(R.drawable.bg_coupon_right2);
                holder.bottom.setBackgroundResource(R.drawable.bg_coupon_bottom2);
                holder.tvState.setText(R.string.guoqi);
                holder.tvState.setTextColor(0xff565656);
                holder.tvValue2.setTextColor(0xff565656);
            }
        }else{
            //已使用
            holder.left.setBackgroundResource(R.drawable.bg_coupon_left2);
            holder.right.setBackgroundResource(R.drawable.bg_coupon_right2);
            holder.bottom.setBackgroundResource(R.drawable.bg_coupon_bottom2);

            holder.tvState.setText(R.string.used);
            holder.tvState.setTextColor(0xff565656);
            holder.tvValue2.setTextColor(0xff565656);
        }
        holder.tvValue1.setText(context.getString(R.string.over) + coupon.getValue1() + context.getString(R.string.youhuiq1));
        holder.tvValue2.setText(coupon.getValue2()+context.getString(R.string.youhui3));
        holder.tvTime.setText(sdf.format(new Date((long)coupon.getTerm_time()*1000)));
        holder.tvSid.setText(context.getString(R.string.nomber) + coupon.getSid());

        return convertView;
    }
}

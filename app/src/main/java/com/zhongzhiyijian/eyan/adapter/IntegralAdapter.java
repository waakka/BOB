package com.zhongzhiyijian.eyan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhongzhiyijian.eyan.R;
import com.zhongzhiyijian.eyan.entity.Integral;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/9/12.
 */
public class IntegralAdapter extends BaseAdapter {

    private Context context;
    private List<Integral> data;

    private int year,month,curYear,curMonth;
    private Calendar calendar;

    private SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");

    public IntegralAdapter(Context context, List<Integral> data) {
        this.context = context;
        this.data = data;
        this.calendar = Calendar.getInstance();
        this.year = 0;
        this.month = 0;
    }

    public void setData(List<Integral> d){
        this.year = 0;
        this.month = 0;
        this.data = d;
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
        TextView tvValue,tvNum,tvTime,tvTitle;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_integral, null);
            holder = new ViewHolder();
            holder.tvTime = (TextView) convertView.findViewById(R.id.tv_integral_time);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_integral_title);
            holder.tvNum = (TextView) convertView.findViewById(R.id.tv_integral_num);
            holder.tvValue = (TextView) convertView.findViewById(R.id.tv_integral_value);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Integral integral = data.get(position);
        holder.tvValue.setText(integral.getValue());

        calendar.setTimeInMillis(integral.getTime());
        curYear = calendar.get(Calendar.YEAR);
        curMonth = calendar.get(Calendar.MONTH) + 1;
        if (year != curYear){
            holder.tvTitle.setVisibility(View.VISIBLE);
            holder.tvTitle.setText(curYear + "年" + curMonth + "月");
            year = curYear;
            month = curMonth;
        }else {
            if (curMonth != month){
                holder.tvTitle.setVisibility(View.VISIBLE);
                holder.tvTitle.setText(curMonth + "月");
                month = curMonth;
            }else{
                holder.tvTitle.setVisibility(View.GONE);
            }
        }

        holder.tvTime.setText(sdf.format(new Date(integral.getTime())));
        if (integral.getType() == 1){
            holder.tvNum.setTextColor(0xff3ad168);
            holder.tvNum.setText("+" + integral.getIntegral());
        }else {
            holder.tvNum.setTextColor(0xffe60012);
            holder.tvNum.setText("-" + integral.getIntegral());
        }

        return convertView;
    }
}

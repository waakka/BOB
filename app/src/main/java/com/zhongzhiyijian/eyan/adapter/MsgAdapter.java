package com.zhongzhiyijian.eyan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongzhiyijian.eyan.R;
import com.zhongzhiyijian.eyan.entity.Msg;
import com.zhongzhiyijian.eyan.util.TimeUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/9/12.
 */
public class MsgAdapter extends BaseAdapter {

    private Context context;
    private List<Msg> data;

    public MsgAdapter(Context context, List<Msg> data) {
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
        TextView tvValue,tvTitle,tvTime;
        ImageView ivRed;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_msg, null);
            holder = new ViewHolder();
            holder.tvTime = (TextView) convertView.findViewById(R.id.tv_msg_time);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_msg_title);
            holder.tvValue = (TextView) convertView.findViewById(R.id.tv_msg_value);
            holder.ivRed = (ImageView) convertView.findViewById(R.id.iv_red);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Msg msg = data.get(position);
        long time = msg.getTime()*1000;
        holder.tvTime.setText(TimeUtil.getMsgTime(time));
        holder.tvTitle.setText(msg.getName());
        holder.tvValue.setText(msg.getValue());
        if (msg.getType() == 1){
            holder.ivRed.setVisibility(View.GONE);
        }else{
            holder.ivRed.setVisibility(View.VISIBLE);
        }
        return convertView;
    }
}

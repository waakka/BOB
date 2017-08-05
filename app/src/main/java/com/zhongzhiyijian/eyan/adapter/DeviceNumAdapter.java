package com.zhongzhiyijian.eyan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongzhiyijian.eyan.R;
import com.zhongzhiyijian.eyan.entity.DeviceNum;

import java.util.List;

/**
 * Created by Administrator on 2016/9/12.
 */
public class DeviceNumAdapter extends BaseAdapter {

    private Context context;
    private List<DeviceNum> data;
    private CallBack callBack;

    public DeviceNumAdapter(Context context, List<DeviceNum> data,CallBack callBack) {
        this.context = context;
        this.data = data;
        this.callBack = callBack;
    }

    public interface CallBack {
        abstract void del(int position);
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
        TextView tv;
        ImageView del;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_device_num, null);
            holder = new ViewHolder();
            holder.tv = (TextView) convertView.findViewById(R.id.tv);
            holder.del = (ImageView) convertView.findViewById(R.id.iv_num_del);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        DeviceNum deviceNum = data.get(position);
        holder.tv.setText(deviceNum.getName());
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.del(position);
            }
        });
        return convertView;
    }
}

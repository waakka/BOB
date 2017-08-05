package com.zhongzhiyijian.eyan.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.zhongzhiyijian.eyan.R;
import com.zhongzhiyijian.eyan.entity.Music;

import java.util.List;

public class AddMusicAdapter extends BaseAdapter{

	private Context mContext;
	private List<Music> musics;


	public AddMusicAdapter(Context mContext, List<Music> musics) {
		this.mContext = mContext;
		this.musics = musics;
	}

	@Override
	public int getCount() {
		return musics.size();
	}

	@Override
	public Object getItem(int position) {
		return musics.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	class ViewHolder{
		CheckBox cb;
		TextView tvName;
		TextView tvStatus;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_addmusic, null);
			holder = new ViewHolder();
			holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tvStatus = (TextView) convertView.findViewById(R.id.tv_status);
			holder.cb = (CheckBox) convertView.findViewById(R.id.cb);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		final Music music = musics.get(position);

		if (music.isadded()){
			holder.cb.setVisibility(View.GONE);
			holder.tvStatus.setVisibility(View.VISIBLE);
		}else{
			holder.cb.setVisibility(View.VISIBLE);
			holder.tvStatus.setVisibility(View.GONE);
		}
		
		holder.tvName.setText(music.getTitle());
		holder.cb.setChecked(music.isChecked());
		holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				music.setChecked(isChecked);
			}
		});

		return convertView;
	}

}

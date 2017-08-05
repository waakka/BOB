package com.zhongzhiyijian.eyan.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongzhiyijian.eyan.R;
import com.zhongzhiyijian.eyan.entity.Music;
import com.zhongzhiyijian.eyan.util.LogUtil;
import com.zhongzhiyijian.eyan.util.TextFormatter;

import java.util.List;

public class MusicAdapter extends BaseAdapter{

	private Context mContext;
	private List<Music> musics;
	
	
	public MusicAdapter(Context mContext, List<Music> musics) {
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
		ImageView ivImg;
		TextView tvName;
		TextView tvSinger;
		TextView tvTime;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_music, null);
			holder = new ViewHolder();
			holder.tvName = (TextView) convertView.findViewById(R.id.tv_item_music_name);
			holder.tvSinger = (TextView) convertView.findViewById(R.id.tv_item_music_singer);
			holder.tvTime = (TextView) convertView.findViewById(R.id.tv_item_music_time);
			holder.ivImg = (ImageView) convertView.findViewById(R.id.iv_item_music_img);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		final Music music = musics.get(position);
		
		holder.tvName.setText(music.getTitle());
		holder.tvSinger.setText(music.getArtist());
		holder.tvTime.setText(TextFormatter.getMusicTime(music.getDuration()));

		String path = music.getImage();
		LogUtil.showLog(position + "-->" + path);
		Bitmap bitmap = null;
		if (path != null && !path.equals("")) {
			bitmap = BitmapFactory.decodeFile(path);
		} else {
			bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
		}
		holder.ivImg.setImageBitmap(bitmap);
		
		return convertView;
	}

}

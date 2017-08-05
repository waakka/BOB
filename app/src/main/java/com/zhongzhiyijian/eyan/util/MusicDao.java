package com.zhongzhiyijian.eyan.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore.Audio.Albums;
import android.provider.MediaStore.Audio.Media;

import com.zhongzhiyijian.eyan.R;
import com.zhongzhiyijian.eyan.entity.Music;

import java.util.ArrayList;

/**
 * 
 * 歌曲的数据源操作类
 * 
 *
 */
public class MusicDao {
	private Context context;

	public MusicDao(Context context) {
		this.context = context;
	}

	/**
	 * 获取歌曲列表
	 * 
	 * @return 歌曲的List集合
	 */
	public ArrayList<Music> getMusicList() {
		ArrayList<Music> musics = null;
		Music music;
		ContentResolver cr;
		String[] projection;
		Cursor cursor;
		Uri uri;

		uri = Media.EXTERNAL_CONTENT_URI;
		projection = new String[] { Media._ID, Media.DATA, Media.TITLE, Media.DURATION, Media.ARTIST, Media.ALBUM, Media.ALBUM_KEY };
		cr = context.getContentResolver();
		cursor = cr.query(uri, projection, null, null, null);

		if (cursor != null && cursor.getCount() > 0) {
			musics = new ArrayList<Music>();
			while (cursor.moveToNext()) {
				music = new Music();
				music.setId(cursor.getLong(cursor.getColumnIndex(Media._ID)));
				music.setDuration(cursor.getLong(cursor.getColumnIndex(Media.DURATION)));
				music.setData(cursor.getString(cursor.getColumnIndex(Media.DATA)));
				music.setTitle(cursor.getString(cursor.getColumnIndex(Media.TITLE)));
				music.setArtist(cursor.getString(cursor.getColumnIndex(Media.ARTIST)));
				music.setAlbum(cursor.getString(cursor.getColumnIndex(Media.ALBUM)));
				music.setImage(getImageByAlbumKey(cursor.getString(cursor.getColumnIndex(Media.ALBUM_KEY))));
//				LogUtil.showLog("search music -->" + music.toString());
				if(music.getDuration() > 1000*60){
					musics.add(music);
				}
				music = null;
			}
			cursor.close();
		}

		return musics;
	}

	/**
	 * 
	 * 根据AlbumKey字段获取歌曲图片
	 * 
	 * @param albumKey
	 *            数据库中读取到的albumKey字段
	 * @return 歌曲图片
	 */
	private String getImageByAlbumKey(String albumKey) {
		String path = "";
		Bitmap bitmap = null;
		Uri uri;
		ContentResolver cr;
		Cursor cursor;
		String[] projection;
		String selection;
		String[] selectionArgs;

		uri = Albums.EXTERNAL_CONTENT_URI;
		cr = context.getContentResolver();
		projection = new String[] { Albums.ALBUM_ART };
		selection = Albums.ALBUM_KEY + "=?";
		selectionArgs = new String[] { albumKey };
		cursor = cr.query(uri, projection, selection, selectionArgs, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				path = cursor.getString(0);
				cursor.close();
			}
		}

		if (path != null) {
			bitmap = BitmapFactory.decodeFile(path);
		} else {
			bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
		}

		return path;
	}
}

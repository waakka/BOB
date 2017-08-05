package com.zhongzhiyijian.eyan.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 
 * 歌曲的实体类
 * 
 * @version 1
 *
 */
@Table(name = "music")
public class Music implements Comparable<Music> {
	@Column(name = "_id",isId = true,autoGen = true)
	private int _id;
	/**
	 * 排序方式
	 */
	@Column(name = "orderBy")
	private static int orderBy;
	/**
	 * 根据标题排序
	 */
	@Column(name = "ORDER_BY_TITLE")
	public static final int ORDER_BY_TITLE = 1;
	/**
	 * 根据时长排序
	 */
	@Column(name = "ORDER_BY_DURATION")
	public static final int ORDER_BY_DURATION = 2;
	/**
	 * ID
	 */
	@Column(name = "id")
	private long id;
	/**
	 * 路径
	 */
	@Column(name = "data")
	private String data;
	/**
	 * 标题
	 */
	@Column(name = "title")
	private String title;
	/**
	 * 时长
	 */
	@Column(name = "duration")
	private long duration;
	/**
	 * 作者
	 */
	@Column(name = "artist")
	private String artist;
	/**
	 * 专辑
	 */
	@Column(name = "album")
	private String album;
	/**
	 * 图片
	 */
	@Column(name = "image")
	private String image;

	@Column(name = "isChecked")
	private boolean isChecked;

	@Column(name = "isadded")
	private boolean isadded;

	public boolean isadded() {
		return isadded;
	}

	public void setIsadded(boolean isadded) {
		this.isadded = isadded;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean checked) {
		isChecked = checked;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	@Override
	public String toString() {
		return "Music [id=" + id + ", data=" + data + ", title=" + title + ", duration=" + duration + ", artist=" + artist + ", album=" + album + "]";
	}

	@Override
	public int compareTo(Music another) {
		if (orderBy == ORDER_BY_DURATION) {
			// 按duration排序
			return (int) (this.duration - another.getDuration());
		} else {
			// 按title排序
			return this.title.compareTo(another.getTitle());
		}
	}

	public static int getOrderBy() {
		return orderBy;
	}

	public static void setOrderBy(int by) {
		orderBy = by;
	}

}

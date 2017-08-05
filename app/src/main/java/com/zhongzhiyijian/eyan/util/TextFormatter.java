package com.zhongzhiyijian.eyan.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 
 * �ı���ʽ��
 * 
 * @author chengheng@tarena.com.cn
 *
 * @since 2014-08-30
 *
 * @version 1
 *
 */
public class TextFormatter {
	/**
	 * ��ȡ������ʱ�䣬��ʽΪ mm:ss
	 * 
	 * @param duration
	 *            �Ժ���Ϊ��λ��ʱ��
	 * @return ��ʽΪ mm:ss ���ַ���
	 */
	public static String getMusicTime(long duration) {
		return new SimpleDateFormat("mm:ss", Locale.CHINA).format(new Date(duration));
	}
}

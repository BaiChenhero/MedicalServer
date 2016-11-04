package com.example.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	// 将String转化成Date类型
	public static Date transferToDate(String strDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = sdf.parse(strDate);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;

	}

	// 获取当前时间
	public static Date getCurrentDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date currentDate = null;
		try {
			currentDate = sdf.parse(sdf.format(new Date()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return currentDate;
	}

	public static Date getCurrentDate1() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");
		Date currentDate = null;
		try {
			currentDate = sdf.parse(sdf.format(new Date()));
			return currentDate;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return currentDate;
	}

	

}

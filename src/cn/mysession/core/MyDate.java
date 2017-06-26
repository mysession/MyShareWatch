package cn.mysession.core;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyDate {

	private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	
	public static String getDate() {
		return sdf.format(new Date());
	}
}

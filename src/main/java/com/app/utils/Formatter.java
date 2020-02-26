package com.app.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Formatter {
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
	
	public static String getBroker(String value) {
		String broker = removeSpecialChars(value).split(",")[0];
		
		return broker;
	}
	
	private static String removeSpecialChars(String value) {
		String _return = value.replaceAll("(\\<|\\>|\"|\\{|\\})", "");
		return _return;
	}

	
	public static String dateFormat(Timestamp date) {
		return dateFormat.format(date);
	}
	
	public static Timestamp dateFormat(String date) {
		Timestamp formatDate = null;
		try {
			formatDate = new Timestamp(dateFormat.parse(date).getTime());	
		}catch(Exception e) {
			e.printStackTrace();
		}
		return formatDate;
	}
}

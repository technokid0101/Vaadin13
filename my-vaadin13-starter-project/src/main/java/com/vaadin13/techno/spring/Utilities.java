package com.vaadin13.techno.spring;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author sushi
 *
 */
public class Utilities {

	private static final Calendar calendar = Calendar.getInstance();
	private static final Map<Integer, String> months;
	private static final Map<Integer, String> daysOfWeek;
	private static Timestamp timestamp;
	private static final Date date;
	private static SimpleDateFormat dateFormat;

	static {
		months = new HashMap<>();
		daysOfWeek = new HashMap<>();
		long mills = System.currentTimeMillis();
		date = new Date(mills);
		timestamp = new Timestamp(calendar.getTime().getTime());
		months.put(0, "January");
		months.put(1, "February");
		months.put(2, "March");
		months.put(3, "April");
		months.put(4, "May");
		months.put(5, "Jun");
		months.put(6, "July");
		months.put(7, "August");
		months.put(8, "September");
		months.put(9, "October");
		months.put(10, "November");
		months.put(11, "December");

		daysOfWeek.put(1, "Sunday");
		daysOfWeek.put(2, "Monday");
		daysOfWeek.put(3, "Tuesday");
		daysOfWeek.put(4, "Wednsday");
		daysOfWeek.put(5, "Thursday");
		daysOfWeek.put(6, "Friday");
		daysOfWeek.put(7, "Saturday");

	}

	// This method loads properties and returns instance of the properties class
	/**
	 * @param filePath
	 * @return properties instanc
	 */
	public static Properties loadProperties(File file) {
		Properties properties = null;
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			properties = new Properties();
			properties.load(fileInputStream);
		} catch (IOException e) {
			System.out.println("Caught an exception " + e.getMessage());
			e.printStackTrace();
		}
		return properties;
	}

	/**
	 * @return current day
	 */
	public static String getCurrentDay() {
		return daysOfWeek.get(calendar.get(Calendar.DAY_OF_WEEK));
	}

	/**
	 * @return current month name
	 */
	public static String getCurrentMonthByName() {
		return months.get(calendar.get(Calendar.MONTH));
	}

	/**
	 * @return current month number in string format
	 */
	public static String getCurrentMonth() {
		return String.valueOf(calendar.get(Calendar.MONTH) + 1);
	}

	/**
	 * @return current year
	 */
	public static String getCurrentYear() {
		return String.valueOf(calendar.get(Calendar.YEAR));
	}

	/**
	 * @return current date(today's)
	 */
	public static Date getCurrentDate() {
		return date;
	}

	/**
	 * @return current time
	 */
	public static Timestamp getTimeNow() {
		return timestamp;
	}

	/**
	 * @param date
	 * @return formatted date string
	 */
	public static String formate(Date date) {
		dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		return dateFormat.format(date);
	}

	/**
	 * @param timestamp
	 * @return formatted time string
	 */
	public static String formate(Timestamp timestamp) {
		dateFormat = new SimpleDateFormat("hh:mm:ss a");
		return dateFormat.format(timestamp);
	}

	/**
	 * @param month
	 * @return month number from given month name
	 */
	public static int getMonth(String month) {
		int intMonth = 0;
		for (Map.Entry<Integer, String> iter : months.entrySet()) {
			if (iter.getValue().equalsIgnoreCase(month)) {
				intMonth = iter.getKey();
				break;
			}
		}
		return intMonth + 1;
	}

	/**
	 * @return
	 */
	public static String getToday() {
		String strDate = "";
		try {
			strDate = String.valueOf(calendar.get(Calendar.DATE));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strDate;
	}
}

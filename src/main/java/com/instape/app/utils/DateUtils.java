package com.instape.app.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 18-Dec-2023
 * @ModifyDate - 18-Dec-2023
 * @Desc -
 */
public class DateUtils {

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 18-Dec-2023
	 * @ModifyDate - 18-Dec-2023
	 * @Desc -
	 */
	public static String dateInYYYYMMDDHHMMSS(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("IST"));
		return formatter.format(date);
	}

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 15-Jan-2024
	 * @ModifyDate - 15-Jan-2024
	 * @Desc -
	 */
	public static String differenceInMonths(Timestamp startDate, Timestamp endDate) {
		LocalDate start = LocalDate.parse(startDate.toString().substring(0, 10));
		LocalDate end = LocalDate.parse(endDate.toString().substring(0, 10));
		Period period = Period.between(start, end);
		int monthsDifference = period.getYears() * 12 + period.getMonths();
		String months = monthsDifference + " Months";
		return months;
	}

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 01-Feb-2024
	 * @ModifyDate - 01-Feb-2024
	 * @Desc -
	 */
	public static String getPreviousDaysDateByDayCount(int numberOfdays) {
		LocalDate currentDate = LocalDate.now();
		LocalDate numberOfDaysAgo = currentDate.minusDays(numberOfdays);
		String returnDate = numberOfDaysAgo + " 00:00:00";
		return returnDate;
	}

	public static Date getDate(String dateString) throws ParseException {
		// Define the date format that matches the date string
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		// Parse the date string to a Date object
		Date returnDate = dateFormat.parse(dateString);

		return returnDate;
	}

	public static int AgeCalculator(String dobString) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate dob = LocalDate.parse(dobString, formatter);

		LocalDate currentDate = LocalDate.now();
		int age = Period.between(dob, currentDate).getYears();

		return age;
	}

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 03-Sep-2024
	 * @ModifyDate - 03-Sep-2024
	 * @Desc -
	 */
	public static Timestamp getCurrentTimestamp() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("IST"));
		String formattedDate = formatter.format(new Date());
		return Timestamp.valueOf(formattedDate);
	}
}

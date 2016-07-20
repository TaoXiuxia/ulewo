package com.ulewo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.ulewo.po.enums.DateTimePatternEnum;

public class DateUtil {

	/** 锁对象 **/
	private static final Object lockObj = new Object();

	/** 存放不同的日期模版格式的sdf的Map **/
	private static Map<String, ThreadLocal<SimpleDateFormat>> sdfMap = new HashMap<String, ThreadLocal<SimpleDateFormat>>();

	private static final String[] WEEKCN = { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };

	/**
	 * 返回一个ThreadLocal的sdf，每个线程只会new一个sdf
	 */
	private static SimpleDateFormat getSdf(final String pattern) {
		ThreadLocal<SimpleDateFormat> tl = sdfMap.get(pattern);

		// 此处的双重判断和同步是为了防止sdfMap这个单例被多次put重复的sdf
		if (tl == null) {
			synchronized (lockObj) {
				tl = sdfMap.get(pattern);
				if (tl == null) {
					// 只有Map中还没有pattern的sdf才会生成新的sdf并放入map
					System.out.println("put new sdf of pattern " + pattern + " to map");

					// 这里是关键，使用ThreadLocal<SimpleDateFormat> 替代原来直接new
					// SimpleDateFormat
					tl = new ThreadLocal<SimpleDateFormat>() {
						@Override
						protected SimpleDateFormat initialValue() {
							System.out.println("thread: " + Thread.currentThread() + " init pattern: " + pattern);
							return new SimpleDateFormat(pattern);
						}
					};
					sdfMap.put(pattern, tl);
				}
			}
		}
		return tl.get();
	}

	/**
	 * 使用ThreadLocal
	 * <SimpleDateFormat>类获取SimpleDateFormat，这样两个线程只会有一个SimpleDateFormat
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String format(Date date, String pattern) {
		return getSdf(pattern).format(date);
	}

	public static Date parse(String dateStr, String pattern) throws ParseException {
		return getSdf(pattern).parse(dateStr);
	}

	/**
	 * 获取某年某月第几天是星期几, 这个月有多少天
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static Map<String, Integer> getTotalDayAndFirstWeekDay4Month(int year, int month, int day) {
		Map<String, Integer> result = new HashMap<String, Integer>();
		// 获取一个月有多少天
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONDAY, month - 1);
		int dateOfMonth = cal.getActualMaximum(Calendar.DATE);

		// 获取当前日期是星期几
		cal.set(Calendar.DATE, day);
		int week = cal.get(Calendar.DAY_OF_WEEK);

		result.put("todayDay", dateOfMonth);
		result.put("firstWeekDay", week);
		return result;
	}

	public static boolean beforeNowDate(String date) {
		try {
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DAY_OF_MONTH, 1);
			Date curDate = c.getTime();
			Date d = parse(date, DateTimePatternEnum.YYYY_MM_DD.getPattern());
			boolean flag = d.before(curDate);
			return flag;
		} catch (Exception e) {
			return false;
		}
	}

	public static int getWeek(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int w = c.get(Calendar.DAY_OF_WEEK) - 1;
		return w;
	}

	public static String getWeekCN(Date date) {
		int w = getWeek(date);
		return WEEKCN[w];
	}
}

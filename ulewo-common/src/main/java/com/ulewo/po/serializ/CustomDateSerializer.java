package com.ulewo.po.serializ;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.ulewo.po.enums.DateTimePatternEnum;
import com.ulewo.utils.DateUtil;

public class CustomDateSerializer extends JsonSerializer<Date> {

	private static final long ONE_MINUTE = 60000L;
	private static final long ONE_HOUR = 3600000L;
	private static final long ONE_DAY = 86400000L;

	private static final int AGO_DAY_0 = 0, AGO_DAY_1 = 1, AGO_DAY_2 = 2, AGO_DAY_7 = 7;

	private static final String JUST_NOW = "刚刚";
	private static final String ONE_MINUTE_AGO = "分钟前";
	private static final String ONE_HOUR_AGO = "小时前";
	private static final String ONE_DAY_AGO = "天前";
	private static final String TWO_DAY_AGO = "昨天";
	private static final String THREE_DAY_AGO = "前天";

	@Override
	public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider)
			throws IOException, JsonProcessingException {
		jgen.writeString(firendly_time(value));
	}
	
	public String firendly_time(Date sourceDate) {
		Date curDate = new Date();
		Date sourceDateYMD = getDayYYYYMMDD(sourceDate);
		Date curDateYMD = getDayYYYYMMDD(curDate);

		// 几天前
		long daysAgo = (curDateYMD.getTime() - sourceDateYMD.getTime()) / ONE_DAY;

		// 当天
		if (daysAgo == AGO_DAY_0) {
			return getCurDayInfo(sourceDate, curDate);
		} else if (daysAgo == AGO_DAY_1) { // 昨天
			return TWO_DAY_AGO + " " + getMinAndSec(sourceDate);
		} else if (daysAgo == AGO_DAY_2) { // 前天
			return THREE_DAY_AGO + " " + getMinAndSec(sourceDate);
		} else if (daysAgo > AGO_DAY_2 && daysAgo <= AGO_DAY_7) {
			return (daysAgo - 1) + ONE_DAY_AGO;
		} else {
			return DateUtil.format(sourceDate, DateTimePatternEnum.YYYY_MM_DD.getPattern());
		}
	}

	private Date getDayYYYYMMDD(Date date) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date);
		Calendar c = Calendar.getInstance();
		c.set(c1.get(Calendar.YEAR), c1.get(Calendar.MONTH), c1.get(Calendar.DAY_OF_MONTH));
		return c.getTime();
	}

	private String getMinAndSec(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.MINUTE) + ":" + String.format("%02d", c.get(Calendar.SECOND));
	}

	private String getCurDayInfo(Date sourceDate, Date curDate) {
		long secondsAgo = curDate.getTime() - sourceDate.getTime();
		if (secondsAgo / ONE_MINUTE <= 0) {
			return JUST_NOW;
		} else if (secondsAgo / ONE_MINUTE > 0 && secondsAgo / ONE_HOUR == 0) {
			return secondsAgo / ONE_MINUTE + ONE_MINUTE_AGO;
		} else {
			return secondsAgo / ONE_HOUR + ONE_HOUR_AGO;
		}
	}
}

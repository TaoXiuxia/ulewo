package com.ulewo.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.tools.ant.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ulewo.controller.SignInController;
import com.ulewo.exception.BusinessException;
import com.ulewo.mapper.SignInMapper;
import com.ulewo.po.enums.DateTimePatternEnum;
import com.ulewo.po.enums.MarkEnum;
import com.ulewo.po.enums.PageSize;
import com.ulewo.po.model.Calendar4SignIn;
import com.ulewo.po.model.DaySignInfo;
import com.ulewo.po.model.SessionUser;
import com.ulewo.po.model.SignIn;
import com.ulewo.po.model.SignInInfo;
import com.ulewo.po.model.SimplePage;
import com.ulewo.po.query.SignInQuery;
import com.ulewo.service.SignInService;
import com.ulewo.service.UserService;
import com.ulewo.utils.Constants;
import com.ulewo.utils.DateUtil;
import com.ulewo.utils.PaginationResult;

@Service("signInService")
public class SignInServiceImpl implements SignInService {

	private Logger logger = LoggerFactory.getLogger(SignInServiceImpl.class);

	@Resource
	private SignInMapper<SignIn, SignInQuery> signInMapper;

	@Resource
	private UserService userService;

	/**
	 * 获取用户当天签到信息
	 * 
	 * @param userId
	 * @return
	 */
	public SignInInfo findSignInInfoByUserId(Integer userId) {
		SignInInfo signInInfo = new SignInInfo();
		SignInQuery query = new SignInQuery();

		Date curDate = new Date();

		// 查询当日所有签到的数量
		query.setCurDate(curDate);
		int todaySignInCount = this.signInMapper.selectCount(query);
		signInInfo.setTodaySignInCount(todaySignInCount);

		// 用户没有登录，显示未签到，已经签到数量为0
		if (null == userId) {
			signInInfo.setHaveSignInToday(false);
			signInInfo.setUserSignInCount(0);
		} else {
			// 查询用户所有签到数量
			query = new SignInQuery();
			query.setUserId(userId);
			int userSignInCount = this.signInMapper.selectCount(query);
			signInInfo.setUserSignInCount(userSignInCount);

			// 查询用户当日签到数量 => 1或者0
			query.setCurDate(curDate);
			int todaySignInCount2 = this.signInMapper.selectCount(query);
			if (todaySignInCount2 == 0) {
				signInInfo.setHaveSignInToday(false);
			} else {
				signInInfo.setHaveSignInToday(true);
			}
		}
		signInInfo.setCurDay(DateUtil.format(new Date(), DateTimePatternEnum.MM_POINT_DD.getPattern()));
		signInInfo.setWeek(DateUtil.getWeekCN(curDate));
		return signInInfo;
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = BusinessException.class)
	public SignIn doSignIn(SessionUser sessionUser) throws BusinessException {
		// 判断今天是否已经签到
		Date curDate = new Date();
		SignInQuery query = new SignInQuery();
		query.setUserId(sessionUser.getUserId());
		query.setCurDate(curDate);
		int todaySignInCount = this.signInMapper.selectCount(query);
		if (todaySignInCount > 0) {
			throw new BusinessException("今日已经签到");
		}

		// 签到
		SignIn signIn = new SignIn();
		signIn.setUserIcon(sessionUser.getUserIcon());
		signIn.setUserId(sessionUser.getUserId());
		signIn.setUserName(sessionUser.getUserName());
		signIn.setSignDate(curDate);
		signIn.setSignTime(curDate);
		this.signInMapper.insert(signIn);

		// 判断是否是连续7天签到
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, -6);
		Date startDate = c.getTime();
		query.setCurDate(null);
		query.setStartDate(startDate);
		query.setEndDate(curDate);
		int count = this.signInMapper.selectCount(query);

		// 写入积分
		int mark = MarkEnum.MARK_SIGNIN.getMark();
		if (count >= Constants.CONTINUESIGNINCOUNT) {
			mark = MarkEnum.MARK_SIGNIN_CONTINUE.getMark();
			signIn.setContinueSignIn(true);
		} else {
			signIn.setContinueSignIn(false);
		}
		userService.addMark(mark, sessionUser.getUserId());
		return signIn;
	}

	public List<Calendar4SignIn> findUserSignInsByYear(Integer userId, Integer year) {
		Calendar c = Calendar.getInstance();
		if (null == year) {
			year = c.get(Calendar.YEAR);
		}
		// 一年的月份
		final int YEARMONTH = 12;
		// 当月月份
		int curMonth = c.get(Calendar.MONTH) + 1;
		int curYear = c.get(Calendar.YEAR);

		SignInQuery query = new SignInQuery();
		query.setYear(year);
		query.setUserId(userId);
		List<SignIn> list = this.signInMapper.selectList(query);

		Map<String, String> signInMap = new HashMap<String, String>();
		// 签到情况放到map中
		for (SignIn sign : list) {
			String dateStr = DateUtil.format(sign.getSignDate(), DateTimePatternEnum.YYYY_MM_DD.getPattern());
			signInMap.put(dateStr, dateStr);
		}
		// 获取日历信息
		List<Calendar4SignIn> resultList = new ArrayList<Calendar4SignIn>();
		boolean beforeNowDate = Boolean.TRUE;
		for (int i = 1; i <= YEARMONTH; i++) {
			Calendar4SignIn signIn = new Calendar4SignIn();
			resultList.add(signIn);
			Map<String, Integer> dayMonth = DateUtil.getTotalDayAndFirstWeekDay4Month(year, i, 1);
			int thisMonthDays = dayMonth.get("todayDay");
			int firstDay = dayMonth.get("firstWeekDay");
			// 0代表前面补充0， 4代表长度为4， d代表参数为整数型
			signIn.setMonth(String.format("%02d", i));
			signIn.setFirstDay(firstDay);
			signIn.setMonthDays(thisMonthDays);
			List<DaySignInfo> dayList = new ArrayList<DaySignInfo>();
			signIn.setDayInfos(dayList);

			// 遍历日期天数
			for (int j = 1; j <= thisMonthDays; j++) {
				DaySignInfo info = new DaySignInfo();
				dayList.add(info);
				String day = String.format("%02d", j);
				String curDay = year + "-" + signIn.getMonth() + "-" + day;

				// 判断是否当天
				if (curDay.equals(DateUtil.format(new Date(), DateTimePatternEnum.YYYY_MM_DD.getPattern()))) {
					info.setCurDay(Boolean.TRUE);
				} else {
					info.setCurDay(Boolean.FALSE);
				}
				info.setDay(day);

				// 判断是否已经签到
				if (signInMap.get(curDay) != null) {
					info.setSignInType(true);
				} else {
					info.setSignInType(false);
				}
				
				// 判断日期是否是当天之前
				// 月份小于当月，或者标识符是true，那么表示是在当天之前
				if(year < curYear){
					info.setBeforeNowDate(true);
				}else if(year == curYear){
					if (i < curMonth) {
						info.setBeforeNowDate(true);
					} else if (i == curMonth) {
						if (DateUtil.beforeNowDate(curDay)) {
							info.setBeforeNowDate(true);
						} else {
							info.setBeforeNowDate(false);
						}
					} else {
						info.setBeforeNowDate(false);
					}
				}else{
					info.setBeforeNowDate(false);
				}
			}
		}
		return resultList;
	}

	public PaginationResult<SignIn> findCurDaySignIn(SignInQuery query) {

		query.setCurDate(new Date());
		int count = this.signInMapper.selectCount(query);
		int pageSize = PageSize.SIZE20.getSize();
		int pageNo = 0;
		if (null != query.getPageNo()) {
			pageNo = query.getPageNo();
		}
		SimplePage page = new SimplePage(pageNo, count, pageSize);
		query.setPage(page);
		List<SignIn> list = this.signInMapper.selectList(query);
		PaginationResult<SignIn> result = new PaginationResult<SignIn>(page, list);
		return result;
	}
}

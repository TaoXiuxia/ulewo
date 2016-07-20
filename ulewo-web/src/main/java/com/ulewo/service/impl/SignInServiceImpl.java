package com.ulewo.service.impl;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ulewo.exception.BusinessException;
import com.ulewo.mapper.SignInMapper;
import com.ulewo.po.enums.DateTimePatternEnum;
import com.ulewo.po.enums.MarkEnum;
import com.ulewo.po.model.SessionUser;
import com.ulewo.po.model.SignIn;
import com.ulewo.po.model.SignInInfo;
import com.ulewo.po.query.SignInQuery;
import com.ulewo.service.SignInService;
import com.ulewo.service.UserService;
import com.ulewo.utils.Constants;
import com.ulewo.utils.DateUtil;
import com.ulewo.utils.PaginationResult;

@Service("signInService")
public class SignInServiceImpl implements SignInService{

	@Resource 
	private SignInMapper <SignIn, SignInQuery>signInMapper;
	
	@Resource
	private UserService userService;

	/**
	 * 获取用户当天签到信息
	 * @param userId
	 * @return
	 */
	public SignInInfo findSignInInfoByUserId(Integer userId){
		SignInInfo signInInfo =new SignInInfo();
		SignInQuery query =new SignInQuery();
		
		Date curDate = new  Date();
		
		//查询当日所有签到的数量
		query.setCurDate(curDate);
		int todaySignInCount = this.signInMapper.selectCount(query);
		signInInfo.setTodaySignInCount(todaySignInCount);
		
		//用户没有登录，显示未签到，已经签到数量为0
		if(null==userId){
			signInInfo.setHaveSignInToday(false);
			signInInfo.setUserSignInCount(0);
		}else{
			//查询用户所有签到数量
			query=new SignInQuery();
			query.setUserId(userId);
			int userSignInCount = this.signInMapper.selectCount(query);
			signInInfo.setUserSignInCount(userSignInCount);
			
			//查询用户当日签到数量 => 1或者0
			query.setCurDate(curDate);
			int todaySignInCount2 = this.signInMapper.selectCount(query);
			if(todaySignInCount2 == 0){
				signInInfo.setHaveSignInToday(false);
			}else{
				signInInfo.setHaveSignInToday(true);
			}
		}
		signInInfo.setCurDay(DateUtil.format(new Date(), DateTimePatternEnum.MM_POINT_DD.getPattern()));
		signInInfo.setWeek(DateUtil.getWeekCN(curDate));
		return signInInfo;
	}

	@Transactional(propagation = Propagation.REQUIRED,rollbackFor = BusinessException.class)
	public SignIn doSignIn(SessionUser sessionUser) throws BusinessException {
		//判断今天是否已经签到
		Date curDate = new Date();
		SignInQuery query = new SignInQuery();
		query.setUserId(sessionUser.getUserId());
		query.setCurDate(curDate);
		int todaySignInCount = this.signInMapper.selectCount(query);
		if(todaySignInCount>0){
			throw new BusinessException("今日已经签到");
		}
		
		//签到
		SignIn signIn = new SignIn();
		signIn.setUserIcon(sessionUser.getUserIcon());
		signIn.setUserId(sessionUser.getUserId());
		signIn.setUserName(sessionUser.getUserName());
		signIn.setSignDate(curDate);
		signIn.setSignTime(curDate);
		this.signInMapper.insert(signIn);
		
		//判断是否是连续7天签到
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, -6);
		Date startDate  =c.getTime();
		query.setCurDate(null);
		query.setStartDate(startDate);
		query.setEndDate(curDate);
		int count =this.signInMapper.selectCount(query);
		
		//写入积分
		int mark=MarkEnum.MARK_SIGNIN.getMark();
		if(count>=Constants.CONTINUESIGNINCOUNT){
			mark= MarkEnum.MARK_SIGNIN_CONTINUE.getMark();
			signIn.setContinueSignIn(true);
		}else{
			signIn.setContinueSignIn(false);
		}
		userService.addMark(mark, sessionUser.getUserId());
		return signIn;
	}

	public PaginationResult<SignIn> findCurDaySignIn(SignInQuery query) {
		// TODO Auto-generated method stub
		return null;
	}

	
}















package com.ulewo.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ulewo.mapper.SignInMapper;
import com.ulewo.po.enums.DateTimePatternEnum;
import com.ulewo.po.model.SignIn;
import com.ulewo.po.model.SignInInfo;
import com.ulewo.po.query.SignInQuery;
import com.ulewo.service.SignInService;
import com.ulewo.service.UserService;
import com.ulewo.utils.DateUtil;

@Service("signInService")
public class SignInServiceImpl implements SignInService{

	@Resource 
	private SignInMapper <SignIn, SignInQuery>signInMapper;
	
	@Resource
	private UserService userService;

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
}















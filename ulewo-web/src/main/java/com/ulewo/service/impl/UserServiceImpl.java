package com.ulewo.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ulewo.controller.UserController;
import com.ulewo.exception.BusinessException;
import com.ulewo.mapper.UserMapper;
import com.ulewo.po.config.ConfigInfo;
import com.ulewo.po.model.User;
import com.ulewo.po.query.UserQuery;
import com.ulewo.service.UserService;
import com.ulewo.utils.Constants;
import com.ulewo.utils.SendMailUtils;
import com.ulewo.utils.StringTools;

@Service("userService")
public class UserServiceImpl implements UserService {

	private Logger logger=LoggerFactory.getLogger(UserServiceImpl.class);

	@Resource
	private UserMapper<User, UserQuery> userMapper;
	
	@Resource 
	private ConfigInfo configInfo;

	public void register(User user) throws BusinessException {
		
		logger.info("============1.1===============");
		
		// 校验Email，用户名，密码是否合法
		String username = user.getUserName();
		String email = user.getEmail();
		String password = user.getPassword();
		
		logger.info("============1.2===============");
		
		if (StringTools.isEmpty(username) || StringTools.isEmpty(email) || StringTools.isEmpty(password)
				|| username.length() > Constants.LENGTH_20 || password.length() > Constants.LENGTH_16
				|| password.length() < Constants.LENGTH_6 || !StringTools.checkEmail(email)
				|| !StringTools.checkUsername(username) || !StringTools.checkPassword(password)) {
			throw new BusinessException("输入参数不合法");
		}
		
		logger.info("============1.3===============");
		
		// 校验用户是否已经存在
		if (this.findUserByUserName(user.getUserName()) != null) {
			throw new BusinessException("用户已经存在");
		}

		logger.info("============1.4===============");
		
		// 校验Email是否已经存在
		if (this.findUserByEmail(user.getEmail()) != null) {
			throw new BusinessException("邮箱已存在");
		}
		
		logger.info("============1.5===============");
		
		user.setPassword(StringTools.encodeByMD5(password));
		user.setUserIcon(Constants.user_img_path_user_icon + ((int) (Math.random() * 10) + 1) + ".png");
		user.setUserBg(Constants.user_img_path_user_bg + ((int) (Math.random() * 10) + 1) + ".png");
		Date curDate = new Date();
		user.setRegisterTime(curDate);
		user.setLastLoginTime(curDate);
		
		
		logger.info("============222===============");
		
		
		this.userMapper.insert(user);
	}

	public User findUserByUserName(String userName) {
		UserQuery query = new UserQuery();
		query.setUserName(userName);
		List<User> userList = userMapper.selectList(query);
		if (userList.size() == 1) {
			return userList.get(0);
		}
		return null;
	}

	public User findUserByEmail(String email) {
		UserQuery query = new UserQuery();
		query.setEmail(email);
		List<User> userList = userMapper.selectList(query);
		if (userList.size() == 1) {
			return userList.get(0);
		}
		return null;
	}

	public User login(String account, String password) throws BusinessException {
		if(StringTools.isEmpty(account)||StringTools.isEmpty(password)){
			throw new BusinessException("输入参数不合法");
		}
		
		User user =null;
		
		//邮箱登录
		if(account.contains("@")){
			user=this.findUserByEmail(account);
		}else{  //用户名登录
			user=this.findUserByUserName(account);
		}
		if(null==user){
			throw new BusinessException("用户不存在");
		}
		if(!user.getPassword().equals(StringTools.encodeByMD5(password))){
			throw new BusinessException("密码错误");
		}
		
		return user;
	}

	public void sendCheckCode(String email) throws BusinessException {
		if(StringTools.isEmpty(email)){
			throw new BusinessException("请求参数错误");
		}
		
		User user =this.findUserByEmail(email);
		if(null==user){
			throw new BusinessException("输入的邮箱不存在");
		}
		
		String checkCode=createCheckCode();
		String title="ulewo邮箱找回密码邮箱";
		StringBuilder content = new StringBuilder("亲爱的 "+email+"<br><br>");
		content.append("欢迎使用ulewo找回密码功能。(http://ulewo.com)!<br><br>");
		content.append("您的验证码是：<span style='color:red;'>"+checkCode+"</span>,如不是本人操作，请忽略此邮件<br><br>");
		content.append("您的注册邮箱是:" + email + "<br><br>");
		content.append("希望你在有乐窝社区的体验有益和愉快！<br><br>");
		content.append("- 有乐窝社区(http://ulewo.com)");
		
		try {
			SendMailUtils.sendEmail(configInfo.getFindemail(), configInfo.getFindpwd(), title, content.toString(), new String[]{email});
		} catch (Exception e) {
			throw new BusinessException("发送邮件失败，请稍候再试");
		}
		//更新数据库
		user.setActivationCode(checkCode);
		this.userMapper.update(user);
	}
	
	/**
	 * 重置密码
	 */
	public void resetPwd(String email, String password, String checkCode) throws BusinessException {
		if (StringTools.isEmpty(email) || StringTools.isEmpty(password) || password.length() > Constants.LENGTH_16
				|| password.length() < Constants.LENGTH_6 || !StringTools.checkPassword(password)) {
			throw new BusinessException("输入参数不合法");
		}
		User user = this.findUserByEmail(email);
		if (null == user) {
			throw new BusinessException("邮箱不存在");
		}
		if (!user.getActivationCode().equals(checkCode)) {
			throw new BusinessException("验证码错误");
		}
		
		user.setPassword(StringTools.encodeByMD5(password));
		this.userMapper.update(user);
	}

	/**
	 * 加积分
	 */
	public void addMark(Integer userId, Integer mark) throws BusinessException {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 生成验证码，用户找回邮箱
	 * @return
	 */
	private String createCheckCode(){
		char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R',
				'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '2', '3', '4', '5', '6', '7', '8', '9' };
		int codeCount =6;
		Random random =new Random();
		StringBuilder randomCode =new StringBuilder();
		int codeLength=codeSequence.length;
		for (int i=0;i<codeCount;i++){
			String strRand =String.valueOf(codeSequence[random.nextInt(codeLength)]);
			randomCode.append(strRand);
		}
		return randomCode.toString();
	}


}

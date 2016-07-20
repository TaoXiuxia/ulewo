package com.ulewo.service;

import com.ulewo.exception.BusinessException;
import com.ulewo.po.model.User;

public interface UserService {

	public void register(User user) throws BusinessException;
	
	public User findUserByUserName(String userName);
	
	public User findUserByEmail(String email);
	
	/**
	 * @param account 帐号可以是用户名或者邮箱
	 * @param password
	 * @return TODO
	 * @throws BusinessException
	 */
	public  User login(String account, String password, Boolean encodePwd ) throws BusinessException;
	
	/**
	 * 发送验证码
	 * @param email
	 * @throws BusinessException
	 */
	public void sendCheckCode(String email) throws BusinessException;
	
	/**
	 * 重置密码
	 * @param email
	 * @param password
	 * @param checkCode
	 * @throws BusinessException
	 */
	public void resetPwd(String email, String password, String checkCode) throws BusinessException;
	
	/**
	 * 加积分
	 * @param userId
	 * @param mark
	 * @throws BusinessException
	 */
	public void addMark(Integer userId, Integer mark)throws BusinessException;

	public void update(User user);
}

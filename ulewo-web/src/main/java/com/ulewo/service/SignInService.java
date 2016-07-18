package com.ulewo.service;

import com.ulewo.po.model.SignInInfo;

public interface SignInService {

	public SignInInfo findSignInInfoByUserId(Integer userId);
	
}

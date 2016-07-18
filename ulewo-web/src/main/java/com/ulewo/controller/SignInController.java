package com.ulewo.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ulewo.po.enums.ResponseCode;
import com.ulewo.po.model.SignInInfo;
import com.ulewo.po.vo.AjaxResponse;
import com.ulewo.service.SignInService;

@Controller
public class SignInController extends BaseController{

	private Logger logger = LoggerFactory.getLogger(SignInController.class);

	@Resource
	private SignInService signInService;

	/**
	 * register页面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "signIn")
	public ModelAndView spitSlot(HttpSession session) {
		ModelAndView view = new ModelAndView("/page/sign_in");
		SignInInfo signInInfo = signInService.findSignInInfoByUserId(this.getUserId(session));
		view.addObject("signInInfo",signInInfo);
		return view;
	}

	/**
	 * 获取签到信息
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "loginSignInfo")
	public AjaxResponse<SignInInfo> loadSignInfo(HttpSession session) {
		AjaxResponse<SignInInfo> result = new AjaxResponse<SignInInfo>();
		try {
			SignInInfo signInInfo = signInService.findSignInInfoByUserId(this.getUserId(session));
			result.setData(signInInfo);
			result.setResponseCode(ResponseCode.SUCCESS);
		} catch (Exception e) {
			logger.error("获取签到信息异常", e);
			result.setResponseCode(ResponseCode.SERVERERROR);
			result.setErrorMsg("获取签到信息异常");
		}
		return result;
	}

	
}























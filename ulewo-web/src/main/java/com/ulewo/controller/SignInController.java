package com.ulewo.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ulewo.exception.BusinessException;
import com.ulewo.po.enums.ResponseCode;
import com.ulewo.po.model.SignIn;
import com.ulewo.po.model.SignInInfo;
import com.ulewo.po.query.SignInQuery;
import com.ulewo.po.vo.AjaxResponse;
import com.ulewo.service.SignInService;
import com.ulewo.utils.PaginationResult;

@Controller
public class SignInController extends BaseController {

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
		view.addObject("signInInfo", signInInfo);
		return view;
	}

	/**
	 * 签到
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "doSignIn.action")
	public AjaxResponse<SignIn> doSignIn(HttpSession session) {
		AjaxResponse<SignIn> result = new AjaxResponse<SignIn>();
		try {
			SignIn signIn = signInService.doSignIn(this.getSessionUser(session));
			result.setData(signIn);
			result.setResponseCode(ResponseCode.SUCCESS);
		} catch (BusinessException e) {
			logger.error("签到异常", e);
			result.setResponseCode(ResponseCode.BUSINESSERROR);
			result.setErrorMsg(e.getMessage());
		} catch (Exception e) {
			logger.error("签到异常", e);
			result.setResponseCode(ResponseCode.SERVERERROR);
			result.setErrorMsg("签到异常");
		}
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "loadCurDaySignIn")
	public AjaxResponse<PaginationResult<SignIn>> loadCurDaySignIn(HttpSession session, SignInQuery query) {
		AjaxResponse<PaginationResult<SignIn>> result = new AjaxResponse<PaginationResult<SignIn>>();
		try {
			PaginationResult<SignIn> data = signInService.findCurDaySignIn(query);
			result.setData(data);
			result.setResponseCode(ResponseCode.SUCCESS);
		} catch (Exception e) {
			logger.error("获取今日签到异常", e);
			result.setResponseCode(ResponseCode.SERVERERROR);
			result.setErrorMsg("获取今日签到异常");
		}
		return result;
	}
	
	
}

package com.ulewo.controller;

import java.awt.Color;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ulewo.checkcode.patchca.color.SingleColorFactory;
import com.ulewo.checkcode.patchca.filter.predefined.CurvesRippleFilterFactory;
import com.ulewo.checkcode.patchca.service.ConfigurableCaptchaService;
import com.ulewo.checkcode.patchca.utils.encoder.EncoderHelper;
import com.ulewo.exception.BusinessException;
import com.ulewo.po.enums.ResponseCode;
import com.ulewo.po.model.SessionUser;
import com.ulewo.po.model.User;
import com.ulewo.po.vo.AjaxResponse;
import com.ulewo.service.UserService;
import com.ulewo.utils.Constants;
import com.ulewo.utils.StringTools;

@Controller
public class UserController {

	private Logger logger=LoggerFactory.getLogger(UserController.class);
	
	@Resource 
	private UserService userService;
	
	/**
	 * register页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="register")
	public ModelAndView register(HttpServletRequest request){
		ModelAndView view=new ModelAndView("/page/register");
		return view;
	}
	
	/**
	 * 注册
	 * @param session
	 * @param user
	 * @param checkCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "register.do")
	public AjaxResponse<Object> registerDo(HttpSession session, User user, String checkCode) {
		AjaxResponse<Object> result = new AjaxResponse<Object>();
		result.setResponseCode(ResponseCode.SUCCESS);
		try {
			String sessionCheckCode = session.getAttribute(Constants.check_code_key).toString();
			
			logger.info("sessionCheckCode --> {}",sessionCheckCode);
			
			if (!sessionCheckCode.equalsIgnoreCase(checkCode)) {
				result.setErrorMsg("验证码错误");
				result.setResponseCode(ResponseCode.CODEERROR);
			} else {
				userService.register(user);
				
				
				logger.info("===============1111==============");
				logger.info("user.getUserIcon()--> {}",user.getUserIcon());
				logger.info("user.getUserName()--> {}",user.getUserName());
				logger.info("user.getUserId()--> {}",user.getUserId());
			
				
				SessionUser sessionUser =new SessionUser();
				sessionUser.setUserId(user.getUserId());
				sessionUser.setUserIcon(user.getUserIcon());
				sessionUser.setUserName(user.getUserName());
				session.setAttribute(Constants.SESSION_USER_KEY, sessionUser);
			}
		} catch (BusinessException e) {
			result.setErrorMsg(e.getMessage());
			result.setResponseCode(ResponseCode.BUSINESSERROR);
			logger.error("注册用户失败，用户名{}，邮箱{}", user.getUserName(), user.getEmail());
		} catch (Exception e) {
			result.setErrorMsg(ResponseCode.SERVERERROR.getDesc());
			result.setResponseCode(ResponseCode.SERVERERROR);
			logger.error("注册用户失败，用户名{}，邮箱{}", user.getUserName(), user.getEmail());
		}
		return result;
	}
	
	/**
	 * 登录页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="login")
	public ModelAndView login(HttpServletRequest request, HttpSession session){
		ModelAndView view=new ModelAndView("/page/login");
		if ((null!=session.getAttribute(Constants.SESSION_ERROR_LOGIN_COUNT)
				&& (Integer)session.getAttribute(Constants.SESSION_ERROR_LOGIN_COUNT) >= Constants.MAX_LOGIN_ERROR_COUNT)) {
			view.addObject("checkCode", "checkCode");
		}
		return view;
	}
	
	/**
	 * 登录
	 * @param session
	 * @param user
	 * @param checkCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "login.do")
	public AjaxResponse<Object> loginDo(HttpSession session, HttpServletResponse response, String account, String password, String rememberMe, String checkCode) {
		 
		final String REMEMBERME ="1";
		String jsessionid=session.getId();
		
		AjaxResponse<Object> result = new AjaxResponse<Object>();
		result.setResponseCode(ResponseCode.SUCCESS);
		try {
			String sessionCheckCode = String.valueOf(session.getAttribute(Constants.check_code_key));
			if (!StringTools.isEmpty(sessionCheckCode) && !sessionCheckCode.equalsIgnoreCase(checkCode)
					&& null!=session.getAttribute(Constants.SESSION_ERROR_LOGIN_COUNT)
					&& (Integer)session.getAttribute(Constants.SESSION_ERROR_LOGIN_COUNT) >= Constants.MAX_LOGIN_ERROR_COUNT) {
				result.setErrorMsg("验证码错误");
				result.setResponseCode(ResponseCode.CODEERROR);
				return result;
			}
			
			User user = userService.login(account, password);
			SessionUser sessionUser = new SessionUser();
			sessionUser.setUserId(user.getUserId());
			sessionUser.setUserIcon(user.getUserIcon());
			sessionUser.setUserName(user.getUserName());
			session.setAttribute(Constants.SESSION_USER_KEY, session);
			session.setAttribute(Constants.SESSION_ERROR_LOGIN_COUNT, 0);
			session.removeAttribute(Constants.check_code_key);
			session.invalidate();
			//记住登录状态
			if(REMEMBERME.equals(rememberMe)){
				//自动登录，保存用户名密码到Cookie
				String infor=URLEncoder.encode(account.toString(), "utf-8")+"===|"+StringTools.encodeByMD5(user.getPassword());
				//清除之前的Cookie信息
				Cookie cookie=new Cookie("cookieInfo",null);
				cookie.setPath("/");
				cookie.setMaxAge(0);
				//将用户信息保存到Cookie中
				Cookie cookieInfo = new Cookie("cookieInfo",infor);
				cookieInfo.setPath("/");
				//设置最大生命周期为1年
				cookieInfo.setMaxAge(31536000);
				response.addCookie(cookieInfo);
			}else{
				Cookie cookie=new Cookie("cookieInfo",null);
				cookie.setPath("/");
				cookie.setMaxAge(0);
			}
		} catch (BusinessException e) {
			if(null==session.getAttribute(Constants.SESSION_ERROR_LOGIN_COUNT)){
				session.setAttribute(Constants.SESSION_ERROR_LOGIN_COUNT, 1);
			}else{
				session.setAttribute(Constants.SESSION_ERROR_LOGIN_COUNT, (Integer)session.getAttribute(Constants.SESSION_ERROR_LOGIN_COUNT)+1);
			}
			
			if((Integer)session.getAttribute(Constants.SESSION_ERROR_LOGIN_COUNT) >= Constants.MAX_LOGIN_ERROR_COUNT){
				result.setResponseCode(ResponseCode.MOREMAXLOGINCOUNT);
			}else{
				result.setResponseCode(ResponseCode.BUSINESSERROR);
			}
			result.setErrorMsg(e.getMessage());
			logger.error("登录失败，帐号{}", account, e);
		} catch (Exception e) {
			result.setErrorMsg(ResponseCode.SERVERERROR.getDesc());
			result.setResponseCode(ResponseCode.SERVERERROR);
			logger.error("登录失败，帐号{}", account, e);
		}
		return result;
	}
	
	/**
	 * 找回密码页面
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value="findpwd")
	public ModelAndView findpwd(HttpServletRequest request, HttpSession session){
		ModelAndView view =new  ModelAndView("/page/findpwd");
		return view;
	}
	
	/**
	 * 发送验证码
	 * @param session
	 * @param response
	 * @param email
	 * @param checkCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="sendCheckCode.do")
	public AjaxResponse<Object> sendCheckCode(HttpSession session, HttpServletResponse response, String email, String checkCode){
		AjaxResponse<Object> result = new AjaxResponse<Object>();
		result.setResponseCode(ResponseCode.SUCCESS);
		try {
			String sessionCheckCode = session.getAttribute(Constants.check_code_key).toString();
			if(!sessionCheckCode.equalsIgnoreCase(checkCode)){
				result.setErrorMsg("验证码错误");
				result.setResponseCode(ResponseCode.CODEERROR);
				return result;
			}else{
				this.userService.sendCheckCode(email);
			}
		} catch(BusinessException e){
			result.setResponseCode(ResponseCode.BUSINESSERROR);
			result.setErrorMsg(e.getMessage());
			logger.error("发送验证码失败，邮箱：{}",email,e);
		}catch (Exception e) {
			result.setResponseCode(ResponseCode.SERVERERROR);
			result.setErrorMsg(ResponseCode.SERVERERROR.getDesc());
			logger.error("发送验证码失败，邮箱：{}",email,e);
		}
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "resetpwd.do")
	public AjaxResponse<Object> resetpwdDo(String email, String password, String checkCode){
		AjaxResponse<Object> result = new AjaxResponse<Object>();
		result.setResponseCode(ResponseCode.SUCCESS);
		try {
			logger.info("=================");
			logger.info("email --> {}", email);
			logger.info("password --> {}",password);
			logger.info("checkCode --> {}",checkCode);
			logger.info("===================");
			
			this.userService.resetPwd(email, password, checkCode);
			
		} catch(BusinessException e){
			result.setResponseCode(ResponseCode.BUSINESSERROR);
			result.setErrorMsg(e.getMessage());
			logger.error("修改密码失败，邮箱：{}",email,e);
		}catch (Exception e) {
			result.setResponseCode(ResponseCode.SERVERERROR);
			result.setErrorMsg(ResponseCode.SERVERERROR.getDesc());
			logger.error("修改密码失败，邮箱：{}",email,e);
		}
		return result;
	}
	
	/**
	 * 生成验证码
	 * @param request
	 * @param response
	 * @param session
	 * @throws IOException
	 */
	@RequestMapping(value = "checkCode")
	public void checkCode(HttpServletRequest request,HttpServletResponse response , HttpSession session) throws IOException{
		ConfigurableCaptchaService cs = new ConfigurableCaptchaService();
        cs.setColorFactory(new SingleColorFactory(new Color(25, 60, 170)));
        cs.setFilterFactory(new CurvesRippleFilterFactory(cs.getColorFactory()));

        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        
        String code = EncoderHelper.getChallangeAndWriteImage(cs, "png", response.getOutputStream());
        session.setAttribute(Constants.check_code_key, code);
	}
}

package com.ulewo.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ulewo.po.enums.ResponseCode;
import com.ulewo.po.model.SessionUser;
import com.ulewo.po.model.User;
import com.ulewo.po.vo.AjaxResponse;
import com.ulewo.service.UserService;
import com.ulewo.service.impl.UserServiceImpl;
import com.ulewo.utils.Constants;
import com.ulewo.utils.SpringContextUtil;

public class AuthorityFilter implements Filter {
	private final static String[] static_ext = { "js", "css", "jpg", "png", "gif", "html", "ico", "vm", "swf" };
	private final static String action_ext = "action";
	private static String absolutePath = null;
	
	private Logger logger=LoggerFactory.getLogger(AuthorityFilter.class);

	private final SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		ServletContext application = request.getSession().getServletContext();
		String req_uri = request.getRequestURI();
		HttpSession session = request.getSession();
		String type = req_uri.substring(req_uri.lastIndexOf('.') + 1);
		
		// 静态资源忽略
		if (ArrayUtils.contains(static_ext, type)) {
			chain.doFilter(req, resp);
			return;
		}
		
		if(absolutePath == null){
			absolutePath = getRealPath(request);
			logger.info("absolutePath ==> {}",absolutePath);
		}
		
		if(application.getAttribute(Constants.ABSOLUTEPATH) == null){
			application.setAttribute(Constants.ABSOLUTEPATH,absolutePath);
		}
		
		Object sessionUserObj = session.getAttribute(Constants.SESSION_USER_KEY);
		// 自动登录
		if(null == sessionUserObj){
			autoLogin(request);
		}
		// 过滤.action后缀的请求
		if(action_ext.equals(type)){
			if(null==sessionUserObj){
				if (request.getHeader("x-requested-with") != null
						&& request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {  //ajax请求
					response.setHeader("Content-Type", "text/html;charset=UTF-8");
					PrintWriter writer = response.getWriter();
					AjaxResponse<?> ajaxResponse = new AjaxResponse<Object>();
					ajaxResponse.setResponseCode(ResponseCode.LOGINTIMEOUT);
					ajaxResponse.setErrorMsg("登录超时");
					//writer.print(JacksonMapper.toJson(ajaxResponse));
				}else{
					response.sendRedirect("/login");
				}
				return;
			}
		}
		chain.doFilter(request, resp);
		return;
	}

	private String getRealPath(HttpServletRequest request) {
		String port = request.getServerPort() == 80 ? "" : (":" + request.getServerPort());
		String realPath = "http://" + request.getServerName() + port + request.getContextPath();
		return realPath;
	}
	
	private void autoLogin(HttpServletRequest req) {
		try {
			Cookie cookieInfo = getCookieByName(req, Constants.COOKIE_USER_INFO);
			if (cookieInfo != null) {
				String info = URLDecoder.decode(cookieInfo.getValue(), "utf-8");
				if (info != null && !"".equals(info)) {
					String infos[] = info.split("\\|");
					UserService userService = (UserServiceImpl)SpringContextUtil.getBean("userService");
					User user = userService.login(infos[0], infos[1], false);
					if (user != null) {
						SessionUser loginUser = new SessionUser();
						loginUser.setUserId(user.getUserId());
						loginUser.setUserName(user.getUserName());
						loginUser.setUserIcon(user.getUserIcon());
						req.getSession().setAttribute(Constants.SESSION_USER_KEY, loginUser);
						user.setLastLoginTime(new Date());
						userService.update(user);
					}
				}
			}
		} catch (Exception e) {
			logger.error("自动登录失败", e);
		}

	}

	public Cookie getCookieByName(HttpServletRequest request, String name) {
		Map<String, Cookie> cookieMap = ReadCookieMap(request);
		if (cookieMap.containsKey(name)) {
			Cookie cookie = (Cookie) cookieMap.get(name);
			return cookie;
		} else {
			return null;
		}
	}

	private Map<String, Cookie> ReadCookieMap(HttpServletRequest request) {

		Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
		Cookie[] cookies = request.getCookies();
		if (null != cookies) {
			for (Cookie cookie : cookies) {
				cookieMap.put(cookie.getName(), cookie);
			}
		}
		return cookieMap;
	}

	public void destroy() {

	}

	public void init(FilterConfig arg0) throws ServletException {

	}

}
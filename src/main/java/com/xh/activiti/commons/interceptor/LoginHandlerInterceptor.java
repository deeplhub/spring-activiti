package com.xh.activiti.commons.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * <p>Title: MVC拦截器</p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @date 2018年3月15日
 *
 */
public class LoginHandlerInterceptor extends HandlerInterceptorAdapter {

	private static final Logger LOGGER = LogManager.getLogger(LoginHandlerInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		// 非控制器请求直接跳出
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		// shiro管理的session
		Subject subject = SecurityUtils.getSubject();
		// HttpSession session = (HttpSession) subject.getSession();
		if (SecurityUtils.getSubject().isAuthenticated()) {
			LOGGER.info("已登陆");
			return true;
		}
		LOGGER.info("未登陆");
		response.sendRedirect(request.getContextPath() + "/login");
		return super.preHandle(request, response, handlerMethod);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		LOGGER.info("postHandle");
		super.postHandle(request, response, handler, modelAndView);
	}

}

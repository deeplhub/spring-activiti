package com.xh.activiti.commons.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.UserFilter;
import org.apache.shiro.web.util.WebUtils;

/**
 * <p>Title: ajax shiro session超时统一处理</p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @date 2018年3月6日
 *
 */
public class ShiroAjaxSessionFilter extends UserFilter {
	
	private static final Logger LOGGER = LogManager.getLogger(ShiroAjaxSessionFilter.class);

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		LOGGER.info("ajax shiro session超时统一处理");
		HttpServletRequest req = WebUtils.toHttp(request);
		String xmlHttpRequest = req.getHeader("X-Requested-With");
		
		
		Subject subject = SecurityUtils.getSubject();
		ShiroUser shiroUser = (ShiroUser) subject.getPrincipal();
		
		
		if (StringUtils.isNotBlank(xmlHttpRequest)) {
			if (xmlHttpRequest.equalsIgnoreCase("XMLHttpRequest")) {
				HttpServletResponse res = WebUtils.toHttp(response);
				// 采用res.sendError(401);在Easyui中会处理掉error，$.ajaxSetup中监听不到
				res.setHeader("oauthstatus", "401");
				return false;
			}
		}
		return super.onAccessDenied(request, response);
	}

}
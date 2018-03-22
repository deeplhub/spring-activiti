package com.xh.activiti.commons.scan;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.xh.activiti.commons.shiro.ShiroUser;

/**
 * <p>Title: AOP 日志</p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @date 2018年3月6日
 *
 */
@Aspect
@Component
@Order
public class SysLogAspect {
	private static final Logger LOGGER = LogManager.getLogger(SysLogAspect.class);

	@Pointcut("within(@org.springframework.stereotype.Controller *)")
	public void cutController() {
	}

	@Around("cutController()")
	public Object recordSysLog(ProceedingJoinPoint point) throws Throwable {
		String strMethodName = point.getSignature().getName();
		String strClassName = point.getTarget().getClass().getName();
		Object[] params = point.getArgs();
		StringBuffer bfParams = new StringBuffer();
		Enumeration<String> paraNames = null;
		HttpServletRequest request = null;
		if (params != null && params.length > 0) {
			request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			paraNames = request.getParameterNames();
			String key;
			String value;
			while (paraNames.hasMoreElements()) {
				key = paraNames.nextElement();
				value = request.getParameter(key);
				bfParams.append(key).append("=").append(value).append("&");
			}
			if (StringUtils.isBlank(bfParams)) {
				bfParams.append(request.getQueryString());
			}
		}

		String strMessage = String.format("[类名]:%s,[方法]:%s,[参数]:%s", strClassName, strMethodName, bfParams.toString());
		LOGGER.info(strMessage);
		if (isWriteLog(strMethodName)) {
			try {
				Subject subject = SecurityUtils.getSubject();
				
				ShiroUser shiroUser = (ShiroUser) subject.getPrincipal();
//				HttpSession session = (HttpSession) currentUser.getSession();
				
				PrincipalCollection collection = subject.getPrincipals();
				if (null != collection) {
					String loginName = collection.getPrimaryPrincipal().toString();
//					SysLog sysLog = new SysLog();
//					sysLog.setLoginName(loginName);
//					sysLog.setRoleName(loginName);
//					sysLog.setOptContent(strMessage);
//					sysLog.setCreateTime(new Date());
//					if (request != null) {
//						sysLog.setClientIp(request.getRemoteAddr());
//					}
//					LOGGER.info(sysLog.toString());
				}
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
		}

		return point.proceed();
	}

	private boolean isWriteLog(String method) {
		String[] pattern = { "login", "logout", "add", "edit", "delete", "grant" };
		for (String s : pattern) {
			if (method.indexOf(s) > -1) {
				return true;
			}
		}
		return false;
	}
}

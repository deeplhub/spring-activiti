package com.xh.activiti.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xh.activiti.commons.utils.Assert;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年3月14日
 */
@Controller
public class LoginController extends BaseController {

	/**
	* 首页
	*
	* @param model
	* @return
	*/
	@GetMapping("/index")
	public String index(Model model) {
		return "index";
	}

	/**
	 * GET 登录
	 * @return {String}
	 */
	@GetMapping("/login")
	public String login() {
		LOGGER.info("GET请求登录");
		if (SecurityUtils.getSubject().isAuthenticated()) {
			return "redirect:/index";
		}
		return "login";
	}

	/**
	 * POST 登录 shiro 写法
	 *
	 * @param username 用户名
	 * @param password 密码
	 * @return {Object}
	 */
	@PostMapping("/login")
	// @ResponseBody
	public Object loginPost(HttpServletRequest request, HttpServletResponse response, String username, String password,
			@RequestParam(value = "rememberMe", defaultValue = "0") Integer rememberMe) {
		LOGGER.info("POST请求登录");

		Assert.isBlank(password, "密码不能为空");

		// 改为全部抛出异常，避免ajax csrf token被刷新
		if (StringUtils.isBlank(username)) {
			throw new RuntimeException("用户名不能为空");
		}
		if (StringUtils.isBlank(password)) {
			throw new RuntimeException("密码不能为空");
		}
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		// 设置记住密码
		token.setRememberMe(1 == rememberMe);
		try {
			subject.login(token);
			return "redirect:index";
		} catch (UnknownAccountException e) {
			throw new RuntimeException("账号不存在！", e);
		} catch (DisabledAccountException e) {
			throw new RuntimeException("账号未启用！", e);
		} catch (IncorrectCredentialsException e) {
			throw new RuntimeException("密码错误！", e);
		} catch (Throwable e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 未授权
	 * @return {String}
	 */
	@GetMapping("/unauth")
	public String unauth() {
		if (SecurityUtils.getSubject().isAuthenticated() == false) {
			return "redirect:/login";
		}
		return "unauth";
	}

	/**
	 * 退出
	 * @return {Result}
	 */
	@PostMapping("/logout")
	@ResponseBody
	public Object logout() {
		LOGGER.info("登出");
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		return renderSuccess();
	}

}

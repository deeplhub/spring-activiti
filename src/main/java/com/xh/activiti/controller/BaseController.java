package com.xh.activiti.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.Charsets;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.baomidou.mybatisplus.plugins.Page;
import com.xh.activiti.commons.exception.ExceptionCode;
import com.xh.activiti.commons.result.PageInfo;
import com.xh.activiti.commons.result.Result;
import com.xh.activiti.commons.shiro.ShiroUser;
import com.xh.activiti.commons.utils.StringEscapeEditor;
import com.xh.activiti.commons.utils.URLUtils;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年3月14日
 */
public abstract class BaseController {

	// 控制器本来就是单例，这样似乎更加合理
	protected Logger LOGGER = LogManager.getLogger(getClass());

	@InitBinder
	public void initBinder(ServletRequestDataBinder binder) {
		/**
		 * 自动转换日期类型的字段格式
		 */
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));
		/**
		 * 防止XSS攻击
		 */
		binder.registerCustomEditor(String.class, new StringEscapeEditor());
	}

	/**
	 * 获取当前登录用户对象
	 * @return {ShiroUser}
	 */
	public ShiroUser getShiroUser() {
		return (ShiroUser) SecurityUtils.getSubject().getPrincipal();
	}

	/**
	 * 获取当前登录用户id
	 * @return {Long}
	 */
	public Long getUserId() {
		return this.getShiroUser().getId();
	}

	/**
	 * 获取当前登录用户名
	 * @return {String}
	 */
	public String getStaffName() {
		return this.getShiroUser().getName();
	}

	/**
	 * ajax失败
	 * @param msg 失败的消息
	 * @return {Object}
	 */
	public Object renderError(String msg) {
		// Result result = new Result();
		// result.setMsg(msg);
		return Result.exception(msg);
	}

	/**
	 * ajax失败
	 * @param code 消息类型
	 * @param msg 失败的消息
	 * @return {Object}
	 */
	public Object renderError(Integer code, String msg) {
		// Result result = new Result();
		// result.setCode(code);
		// result.setMsg(msg);
		return Result.exception(code, msg);
	}

	/**
	 * ajax成功
	 * @return {Object}
	 */
	public Object renderSuccess() {
		// Result result = new Result();
		// result.setCode(ExceptionCode.SUCCESS.code);
		// result.setMsg(ExceptionCode.SUCCESS.msg);
		return Result.exception(ExceptionCode.SUCCESS.code, ExceptionCode.SUCCESS.msg);
	}

	/**
	 * ajax成功
	 * @param msg 消息
	 * @return {Object}
	 */
	public Object renderSuccess(String msg) {
		// Result result = new Result();
		// result.setCode(ExceptionCode.SUCCESS.code);
		// result.setMsg(msg);
		return Result.exception(ExceptionCode.SUCCESS.code, msg);
	}

	/**
	 * ajax成功
	 * @param obj 成功时的对象
	 * @return {Object}
	 */
	public Object renderSuccess(Object obj) {
		// Result<Object> result = new Result<Object>();
		// result.setCode(ExceptionCode.SUCCESS.code);
		// result.setObj(obj);
		return Result.exception(ExceptionCode.SUCCESS.code, "", obj);
	}

	public <T> Page<T> getPage(int current, int size, String sort, String order) {
		Page<T> page = new Page<T>(current, size, sort);
		if ("desc".equals(order)) {
			page.setAsc(false);
		} else {
			page.setAsc(true);
		}
		return page;
	}

	public <T> PageInfo pageToPageInfo(Page<T> page) {
		PageInfo pageInfo = new PageInfo();
		pageInfo.setRows(page.getRecords());
		pageInfo.setTotal(page.getTotal());
		pageInfo.setOrder(page.getOrderByField());
		return pageInfo;
	}

	/**
	 * redirect跳转
	 * @param url 目标url
	 */
	protected String redirect(String url) {
		return new StringBuilder("redirect:").append(url).toString();
	}

	/**
	 * 下载文件
	 * @param file 文件
	 */
	protected ResponseEntity<Resource> download(File file) {
		String fileName = file.getName();
		return download(file, fileName);
	}

	/**
	 * 下载
	 * @param file 文件
	 * @param fileName 生成的文件名
	 * @return {ResponseEntity}
	 */
	protected ResponseEntity<Resource> download(File file, String fileName) {
		Resource resource = new FileSystemResource(file);

		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String header = request.getHeader("User-Agent");
		// 避免空指针
		header = header == null ? "" : header.toUpperCase();
		HttpStatus status;
		if (header.contains("MSIE") || header.contains("TRIDENT") || header.contains("EDGE")) {
			fileName = URLUtils.encodeURL(fileName, Charsets.UTF_8);
			status = HttpStatus.OK;
		} else {
			fileName = new String(fileName.getBytes(Charsets.UTF_8), Charsets.ISO_8859_1);
			status = HttpStatus.CREATED;
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", fileName);
		return new ResponseEntity<Resource>(resource, headers, status);
	}
}

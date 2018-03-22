package com.xh.activiti.commons.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xh.activiti.commons.result.Result;

/**
 * <p>Title: 异常处理器</p>
 * <p>Description: 异常增强，以JSON的形式返回给客服端</p>
 * 
 * @author H.Yang
 * @date 2018年3月8日
 *
 */
@ControllerAdvice
public class RestExceptionHandler {

	/**
	 * 自定义异常
	 */
	@ExceptionHandler(ResultException.class)
	@ResponseBody
	public Result handleRRException(ResultException e) {
		Result result = new Result();
		result.setCode(e.getCode());
		result.setMsg(e.getMessage());
		return result;
	}

}

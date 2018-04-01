package com.xh.activiti.commons.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;

import com.xh.activiti.commons.result.Result;
import com.xh.activiti.commons.utils.PageData;

/**
 * <p>Title: 异常处理器</p>
 * <p>Description: 异常增强，以JSON的形式返回给客服端<br><br>
 * <h3><h3>优点：</h3>将 Controller 层的异常和数据校验的异常进行统一处理，减少模板代码，减少编码量，提升扩展性和可维护性。<br>
 * <h3>缺点：</h3>只能处理 Controller 层未捕获（往外抛）的异常，对于 Interceptor（拦截器）层的异常，Spring 框架层的异常，就无能为力了。<br>
 * </p>
 * 
 * @author H.Yang
 * @date 2018年3月8日
 *
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * <p>Title: 返回结果异常</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年4月1日
	 * 
	 * @param e
	 * @return
	 */
	@ExceptionHandler(ExceptionResponse.class)
	@ResponseBody
	public Result handleResultException(RuntimeException e) {
		System.out.println("RuntimeException: " + e.getMessage());
		return Result.exception(500, e.getMessage());
	}

	/**
	 * <p>Title: 处理上传文件大小超过限制抛出的异常</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年4月1日
	 * 
	 * @param e
	 * @return
	 */
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ModelAndView handleResultException(MaxUploadSizeExceededException e) {
		System.out.println("MaxUploadSizeExceededException: " + e.getMessage());
		return new ModelAndView("", PageData.entityToMap(Result.exception(500, "上传文件过大")));
	}

	/**
	 * <p>Title: 父异常 - 此异常一定要写在所有异常的下面</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年4月1日
	 * 
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public Result handleResultException(Exception e) {
		System.out.println("Exception: " + e.getMessage());
		return Result.exception(500, e.getMessage());
	}

}

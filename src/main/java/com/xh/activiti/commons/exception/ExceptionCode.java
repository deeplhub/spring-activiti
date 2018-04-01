package com.xh.activiti.commons.exception;

/**
 * <p>Title: 异常码</p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年3月17日
 */
public enum ExceptionCode {

	/** 成功 (0000, "OK") */
	SUCCESS(0000, "OK"),
	/** 系统异常 (1010, "服务器繁忙，请稍后重试！") **/
	SERVER_ERROR(1010, "服务器繁忙，请稍后重试！"),
	/** 数据库异常 (2010, "数据库操作异常！数据异常！查询异常！查无记录！") **/
	DB_ERROR(2010, "数据库操作异常！数据异常！查询异常！查无记录！"),
	/** 参数异常 (3010, "无效参数！缺少参数！参数格式不匹配！参数非法！") **/
	PARAMETER_ERROR(3010, "无效参数！缺少参数！参数格式不匹配！参数非法！"),
	/** 服务异常 (4010, "非法请求！无操作权限！上传下载失败！") **/
	SERVICE_ERROR(4010, "非法请求！无操作权限！上传下载失败！"),
	/** 未登录 (4020, "未登录") */
	MEMBER_INVALID(4020, "未登录"),
	/** 账号被禁用 (4022, "账号被禁用") */
	MEMBER_DISABLED(4022, "账号被禁用");

	public final Integer code;
	public final String msg;

	ExceptionCode(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public static ExceptionCode getExceptionCode(final String code) {
		try {
			return ExceptionCode.valueOf(code);
		} catch (Exception e) {
		}

		return SERVER_ERROR;
	}
}

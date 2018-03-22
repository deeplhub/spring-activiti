package com.xh.activiti.commons.result;

/**
 * <p>Title: 操作结果集,返回数据</p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @date 2018年3月6日
 *
 */
public class Result {

	private Integer code = null;
	private String msg = "";
	private Object obj = null;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}
}

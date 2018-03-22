package com.xh.activiti.commons.utils;

import org.apache.commons.lang3.StringUtils;

import com.xh.activiti.commons.exception.ResultException;

/**
 * <p>Title: 数据校验</p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @date 2018年3月16日
 * 
 */
public abstract class Assert {

	public static void isBlank(String str, String message) {
		if (StringUtils.isBlank(str)) {
			throw new ResultException(message);
		}
	}

	public static void isNull(Object object, String message) {
		if (object == null) {
			throw new ResultException(message);
		}
	}
}

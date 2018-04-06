package com.xh.activiti.service;

import com.xh.activiti.commons.result.PageInfo;
import com.xh.activiti.commons.shiro.ShiroUser;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年4月6日
 */
public interface ITaskService {

	/**
	 * <p>Title: 待办流程任务列表</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年4月6日
	 */
	void selectPendingPage(PageInfo pageInfo, Long userId);

	/**
	 * <p>Title: 批准流程</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年4月6日
	 * 
	 * @param leaveId
	 * @param message
	 * @return
	 */
	boolean approvalProcess(Long leaveId, String taskId, String message, ShiroUser user);

	/**
	 * <p>Title: 已完成的任务</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年4月6日
	 * 
	 * @param userId
	 * @return
	 */
	void finishTaskProcess(PageInfo pageInfo, Long userId);
}

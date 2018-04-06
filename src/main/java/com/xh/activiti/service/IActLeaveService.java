package com.xh.activiti.service;

import java.util.List;

import org.activiti.engine.task.Comment;

import com.baomidou.mybatisplus.service.IService;
import com.xh.activiti.commons.result.PageInfo;
import com.xh.activiti.model.ActLeave;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年3月28日
 */
public interface IActLeaveService extends IService<ActLeave> {

	/**
	 * <p>Title: 请假列表</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年4月3日
	 * 
	 * @param pageInfo
	 * @param userId
	 */
	void selectPage(PageInfo pageInfo, Long userId);

	/**
	 * <p>Title: 提交请假申请</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年4月3日
	 * 
	 * @param leaveId
	 * @param userId
	 * @return
	 */
	boolean updateSubmitApplication(Long leaveId, Long userId);

	/**
	 * <p>Title: 查看历史批注</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年4月6日
	 * 
	 * @param processInstanceId
	 * @return
	 */
	List<Comment> queryHistoryComment(String processInstanceId);

}

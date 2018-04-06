package com.xh.activiti.service.impl;

import java.util.List;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xh.activiti.commons.exception.ResultException;
import com.xh.activiti.commons.result.PageInfo;
import com.xh.activiti.commons.utils.PageData;
import com.xh.activiti.dao.IActLeaveDao;
import com.xh.activiti.dao.IRoleDao;
import com.xh.activiti.model.ActLeave;
import com.xh.activiti.service.IActLeaveService;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年3月28日
 */
@Service
public class ActLeaveServiceImpl extends ServiceImpl<IActLeaveDao, ActLeave> implements IActLeaveService {

	@Autowired
	private IActLeaveDao leaveDao;
	@Autowired
	private IRoleDao roleDao;

	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private TaskService taskService;

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
	@Override
	public void selectPage(PageInfo pageInfo, Long userId) {
		Page<ActLeave> page = new Page<ActLeave>(pageInfo.getNowpage(), pageInfo.getSize());

		EntityWrapper<ActLeave> wrapper = new EntityWrapper<>();
		wrapper.addFilter("user_id = {0}", userId);
		wrapper.orderBy("create_date", false);
		super.selectPage(page, wrapper);

		pageInfo.setRows(page.getRecords());
		pageInfo.setTotal(page.getTotal());
	}

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
	@Override
	public boolean updateSubmitApplication(Long leaveId, Long userId) {
		ActLeave leave = super.selectById(leaveId);
		if (leave.getState() != ActLeave.LeaveState.UNSUBMITTED.getValue())
			throw new ResultException("申请已提交，不能重复提交！");

		String businessKey = leave.getId().toString();
		PageData variables = new PageData();
		variables.put("userId", userId);

		// 启动流程
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("leaveProcess", businessKey, variables);
		// 根据流程实例Id查询任务
		Task task = taskService.createTaskQuery()//
				.processInstanceId(processInstance.getProcessInstanceId())//
				.singleResult();

		// 完成 ,任务向下一步推送
		taskService.complete(task.getId());
		
		leave.setTaskId(task.getId());
		leave.setProcessInstanceId(processInstance.getProcessInstanceId());
		leave.setState(ActLeave.LeaveState.UNDER_REVIEW.getValue());
		leave.setApprovalInfo("待审批");
		return super.updateById(leave);
	}

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
	@Override
	public List<Comment> queryHistoryComment(String processInstanceId) {
		List<Comment> comments = taskService.getProcessInstanceComments(processInstanceId);
		return comments;
	}

}

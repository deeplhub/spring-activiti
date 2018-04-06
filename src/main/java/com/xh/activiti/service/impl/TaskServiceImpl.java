package com.xh.activiti.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xh.activiti.commons.result.PageInfo;
import com.xh.activiti.commons.shiro.ShiroUser;
import com.xh.activiti.commons.utils.PageData;
import com.xh.activiti.dao.IActLeaveDao;
import com.xh.activiti.dao.IRoleDao;
import com.xh.activiti.model.ActLeave;
import com.xh.activiti.service.ITaskService;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年4月6日
 */
@Service
public class TaskServiceImpl implements ITaskService {

	@Autowired
	private IRoleDao roleDao;
	@Autowired
	private IActLeaveDao leaveDao;

	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private HistoryService historyService;

	/**
	 * <p>Title: 待办流程任务列表</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年4月6日
	 * 
	 * @return
	 */
	@Override
	public void selectPendingPage(PageInfo pageInfo, Long userId) {
		// 查询总记录
		int total = (int) taskService.createTaskQuery()//
				.taskCandidateGroupIn(candidateGroup(userId))//
				.count();

		// 根据当前人的ID查询
		List<Task> tasks = taskService.createTaskQuery()//
				.taskCandidateGroupIn(candidateGroup(userId))//
				.listPage(pageInfo.getFrom(), pageInfo.getSize());

		// 查询请假信息
		List<String> list = new ArrayList<>();
		for (Task task : tasks) {
			list.add(task.getExecutionId());
		}
		List<ActLeave> leaves = list.isEmpty() ? null : leaveDao.selectListByProcessInstanceId(list);
		List<PageData> lists = buildByTask(tasks, leaves);
		pageInfo.setRows(lists);
		pageInfo.setTotal(total);
	}

	// 待办流程任务列表-递归
	private static List<PageData> buildByTask(List<Task> tasks, List<ActLeave> leaves) {
		List linkeds = new LinkedList<>();
		PageData pd = null;
		for (Task task : tasks) {
			pd = new PageData();
			pd.put("tid", task.getId());
			pd.put("name", task.getName());
			pd.put("assignee", task.getAssignee());
			pd.put("createTime", task.getCreateTime());

			pd.putAll(buildByLeave(task.getExecutionId(), leaves));

			linkeds.add(pd);
		}
		return linkeds;
	}

	// 待办流程任务列表-递归
	private static PageData buildByLeave(String executionId, List<ActLeave> leaves) {
		PageData pd = null;
		for (ActLeave leave : leaves) {
			if (executionId.equals(leave.getProcessInstanceId())) {
				pd = PageData.entityToMap(leave);
			}
		}
		return pd;
	}

	// 根据用户ID查询角色列表
	private List<String> candidateGroup(Long userId) {
		// 得到当前用户的所有角色名
		List<String> roleNames = roleDao.selectRoleListByUserId(userId);
		return roleNames;
	}

	/**
	 * <p>Title: 批准流程并往下推送</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年4月6日
	 * 
	 * @param leaveId
	 * @param message
	 * @return
	 */
	@Override
	public boolean approvalProcess(Long leaveId, String taskId, String message, ShiroUser user) {
		ActLeave leave = leaveDao.selectById(leaveId);
		if (leave == null) {
			return false;
		}

		// 有变量的情况下
		Map<String, Object> variables = new HashMap<>();
		variables.put("pass", "Yes");

		// 添加审批人
		// Authentication.setAuthenticatedUserId("28");
		Authentication.setAuthenticatedUserId(user.getName());

		// 添加变量信息
		// taskService.addCandidateUser(taskId, "27");

		// 添加批注信息
		taskService.addComment(taskId, leave.getProcessInstanceId(), message);
		taskService.setAssignee(taskId, user.getName());
		// 完成任务
		taskService.complete(taskId, variables);

		// 判断流程是否结束，查询正在执行的执行对象表
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()// 创建流程实例查询对象
				.processInstanceId(leave.getProcessInstanceId())//
				.singleResult();
		// 说明流程实例结束了
		if (processInstance == null) {
			leave.setState(ActLeave.LeaveState.APPROVAL.getValue());
			leave.setApprovalInfo(ActLeave.LeaveState.APPROVAL.getName());
		} else {
			leave.setState(ActLeave.LeaveState.NEXT_INFO.getValue());
			leave.setApprovalInfo(user.getName() + ActLeave.LeaveState.NEXT_INFO.getName());
		}
		return leaveDao.updateById(leave) > 0 ? true : false;
	}

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
	@Override
	public void finishTaskProcess(PageInfo pageInfo, Long userId) {
		int total = (int) historyService.createHistoricTaskInstanceQuery()//
				.taskCandidateGroupIn(candidateGroup(userId))// 根据角色名称查询
				.count();
		// 根据当前人的角色查询
		List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
				.taskCandidateGroupIn(candidateGroup(userId))// 根据角色名称查询
				.orderByTaskCreateTime()//
				.desc()//
				.listPage(pageInfo.getFrom(), pageInfo.getSize());

		// 查询请假信息
		List<String> list = new ArrayList<>();
		for (HistoricTaskInstance historicTaskInstance : historicTaskInstances) {
			list.add(historicTaskInstance.getExecutionId());
		}
		List<ActLeave> leaves = list.isEmpty() ? null : leaveDao.selectListByProcessInstanceId(list);

		List<PageData> lists = buildByHistoricTaskInstance(historicTaskInstances, leaves);
		pageInfo.setRows(lists);
		pageInfo.setTotal(total);
	}

	// 待办流程任务列表-递归
	private static List<PageData> buildByHistoricTaskInstance(List<HistoricTaskInstance> historicTaskInstances, List<ActLeave> leaves) {
		List linkeds = new LinkedList<>();
		PageData pd = null;
		for (HistoricTaskInstance historicTaskInstance : historicTaskInstances) {
			pd = new PageData();
			pd.put("hid", historicTaskInstance.getId());
			pd.put("name", historicTaskInstance.getName());
			pd.put("assignee", historicTaskInstance.getAssignee());
			pd.put("startTime", historicTaskInstance.getStartTime());
			pd.put("endTime", historicTaskInstance.getEndTime());
			pd.put("createTime", historicTaskInstance.getCreateTime());

			pd.putAll(buildByLeave(historicTaskInstance.getExecutionId(), leaves));

			linkeds.add(pd);
		}
		return linkeds;
	}
}

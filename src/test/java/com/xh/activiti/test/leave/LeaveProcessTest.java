package com.xh.activiti.test.leave;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.xh.activiti.model.ActLeave;
import com.xh.activiti.service.IActLeaveService;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年4月5日
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/spring-config.xml")
public class LeaveProcessTest {

	@Autowired
	private IActLeaveService leaveService;
	@Resource
	private RuntimeService runtimeService;
	@Resource
	private TaskService taskService;
	@Resource
	private HistoryService historyService;

	// 保存请假信息
	// @Test
	public void save() {
		ActLeave leave = new ActLeave();
		leave.setContent("请假信息.........");// 请假内容
		leave.setDays(1d);// 请假天数
		leave.setState(0);// 请假状态
		leave.setUserId(26l);
		leave.setStartDate(new Date());
		leave.setEndDate(new Date());
		leave.setCreateDate(new Date());

		boolean flag = leaveService.insert(leave);
		System.out.println("请假保存状态：" + flag);
	}

	// 提交请假信息
	// @Test
	public void submitApplication() {
		Long leaveId = (long) 1;
		ActLeave leave = leaveService.selectById(leaveId);

		String businessKey = leaveId.toString();
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("userId", 26);
		// 启动流程
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("leaveProcess", businessKey, variables);
		String processInstanceId = processInstance.getId();

		// 根据流程实例Id查询任务
		Task task = taskService.createTaskQuery()//
				.processInstanceId(processInstance.getProcessInstanceId())//
				.singleResult();

		// 完成 ,任务向下一步推送
		taskService.complete(task.getId());

		leave.setProcessInstanceId(processInstanceId);
		leaveService.updateById(leave);
	}

	// 查询我申请的流程
	// @Test
	public void findTaskProcess() {
		String assignee = "26";
		List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()//
				.taskAssignee(assignee)//
				.list();
		for (HistoricTaskInstance historicTaskInstance : historicTaskInstances) {
			System.out.println("任务ID:" + historicTaskInstance.getId());
			System.out.println("流程实例ID:" + historicTaskInstance.getProcessInstanceId());
			System.out.println("任务名称：" + historicTaskInstance.getName());
			System.out.println("办理人：" + historicTaskInstance.getAssignee());
			System.out.println("开始时间：" + historicTaskInstance.getStartTime());
			System.out.println("结束时间：" + historicTaskInstance.getEndTime());
			System.out.println("############################################");
		}
	}

	// 查询历史流程批注
	// @Test
	public void queryHistoryComment() {
		String processInstanceId = "242501";
		List<Comment> comments = taskService.getProcessInstanceComments(processInstanceId);
		System.out.println(JSON.toJSON(comments));
	}

	/********************************************************************************************************/
	/********************************************************************************************************/
	/********************************************************************************************************/

	// 待办流程
	// @Test
	public void pendingTaskProcess() {
		String candidateGroup = "技术部经理";
		// 根据当前人的ID查询
		List<Task> tasks = taskService.createTaskQuery()//
				.taskCandidateGroup(candidateGroup)//
				.list();
		for (Task task : tasks) {
			System.out.println("待办任务ID：" + task.getId());
			System.out.println("任务名称：" + task.getName());
			System.out.println("任务创建时间：" + task.getCreateTime());
			System.out.println("任务办理人：" + task.getAssignee());
			System.out.println("流程实例ID：" + task.getProcessInstanceId());
			System.out.println("执行对象ID：" + task.getExecutionId());
			System.out.println("流程定义ID" + task.getProcessDefinitionId());
			System.out.println("################################");
		}
	}

	// 批准流程
	// @Test
	public void approvalProcess() {
		String taskId = "242508";
		String message = "好，批准了！";
		// 首先根据ID查询任务
		Task task = taskService.createTaskQuery()//
				.taskId(taskId) // 根据任务id查询
				.singleResult();

		// 有变量的情况下
		Map<String, Object> variables = new HashMap<>();
		variables.put("pass", "Yes");

		// 获取流程实例id
		String processInstanceId = task.getProcessInstanceId();
		Authentication.setAuthenticatedUserId("27");// 添加审批人
		// 添加批注信息
		// taskService.addCandidateUser(taskId, "27");
		taskService.addComment(taskId, processInstanceId, message);
		// 完成任务
		taskService.complete(taskId, variables);
	}

	// 已完成的任务
	// @Test
	public void finishedTaskProcess() {
		String candidateGroup = "技术部经理";
		// 根据当前人的角色查询
		List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
				.taskCandidateGroup(candidateGroup)// 根据角色名称查询
				.list();
		System.out.println(JSON.toJSON(historicTaskInstances));
		for (HistoricTaskInstance historicTaskInstance : historicTaskInstances) {
			System.out.println("已办任务ID：" + historicTaskInstance.getId());
			System.out.println("任务名称：" + historicTaskInstance.getName());
			System.out.println("任务创建时间：" + historicTaskInstance.getCreateTime());
			System.out.println("任务办理人：" + historicTaskInstance.getAssignee());
			System.out.println("流程实例ID：" + historicTaskInstance.getProcessInstanceId());
			System.out.println("执行对象ID：" + historicTaskInstance.getExecutionId());
			System.out.println("流程定义ID" + historicTaskInstance.getProcessDefinitionId());
			System.out.println("################################");
		}
	}

	/********************************************************************************************************/
	/********************************************************************************************************/
	/********************************************************************************************************/

	// 待办流程
	// @Test
	public void pendingTaskProcess2() {
		String candidateGroup = "总经理";
		// 根据当前人的ID查询
		List<Task> tasks = taskService.createTaskQuery()//
				.taskCandidateGroup(candidateGroup)//
				.list();
		for (Task task : tasks) {
			System.out.println("待办任务ID：" + task.getId());
			System.out.println("任务名称：" + task.getName());
			System.out.println("任务创建时间：" + task.getCreateTime());
			System.out.println("任务办理人：" + task.getAssignee());
			System.out.println("流程实例ID：" + task.getProcessInstanceId());
			System.out.println("执行对象ID：" + task.getExecutionId());
			System.out.println("流程定义ID" + task.getProcessDefinitionId());
			System.out.println("################################");
		}
	}

	// 批准流程
	// @Test
	public void approvalProcess2() {
		String taskId = "245005";
		String message = "快去快回";
		// 首先根据ID查询任务
		Task task = taskService.createTaskQuery()//
				.taskId(taskId) // 根据任务id查询
				.singleResult();

		// 有变量的情况下
		Map<String, Object> variables = new HashMap<>();
		variables.put("pass", "Yes");

		// 获取流程实例id
		String processInstanceId = task.getProcessInstanceId();
		Authentication.setAuthenticatedUserId("28");// 添加审批人
		// 添加批注信息
		// taskService.addCandidateUser(taskId, "27");
		taskService.addComment(taskId, processInstanceId, message);
		// 完成任务
		taskService.complete(taskId, variables);
	}

}

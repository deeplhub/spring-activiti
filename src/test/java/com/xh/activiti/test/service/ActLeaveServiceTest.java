package com.xh.activiti.test.service;

import java.util.List;

import org.activiti.engine.task.Comment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.xh.activiti.commons.result.PageInfo;
import com.xh.activiti.commons.shiro.ShiroUser;
import com.xh.activiti.service.IActLeaveService;
import com.xh.activiti.service.ITaskService;
import com.xh.activiti.service.IUserService;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年4月3日
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/spring-config.xml")
public class ActLeaveServiceTest {

	@Autowired
	private IActLeaveService leaveService;
	@Autowired
	private ITaskService taskService;
	@Autowired
	private IUserService userService;

	// 提交申请
	// @Test
	public void submitApplication() {
		Long leaveId = (long) 1, //
				userId = (long) 26;
		boolean flag = leaveService.updateSubmitApplication(leaveId, userId);
		System.out.println("提交状态：" + flag);
	}

	// 待办流程任务列表
	// @Test
	public void selectPendingPage() {
		Long userId = (long) 27;
		PageInfo pageInfo = new PageInfo(1, 10);
		taskService.selectPendingPage(pageInfo, userId);

		JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
		String json = JSON.toJSONString(pageInfo, SerializerFeature.WriteDateUseDateFormat);
		System.out.println(json);
	}

	// 批准流程
	// @Test
	public void approvalProcess() {
		String taskId = "252508", //
				processInstanceId = "252501", //
				message = "同意";
		ShiroUser user = new ShiroUser("lis");
		user.setName("李四");

//		boolean flag = taskService.approvalProcess(taskId, message, user);
//		System.out.println("批准状态：" + flag);
	}

	// 查看历史批注
//	@Test
	public void queryHistoryComment() {
		String processInstanceId = "252501";
		List<Comment> comments = leaveService.queryHistoryComment(processInstanceId);
		System.out.println(JSON.toJSON(comments));
	}

	// 已完成的任务
	 @Test
	public void finishTaskProcess() {
		Long userId = (long) 27;
		PageInfo pageInfo = new PageInfo(1, 10);
		taskService.finishTaskProcess(pageInfo, userId);
		System.out.println(JSON.toJSON(pageInfo));
	}
}

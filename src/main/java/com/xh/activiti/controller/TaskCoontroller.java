package com.xh.activiti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xh.activiti.commons.result.PageInfo;
import com.xh.activiti.commons.utils.Assert;
import com.xh.activiti.service.ITaskService;

/**
 * <p>Title: 任务</p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年4月6日
 */
@Controller
@RequestMapping("/task")
public class TaskCoontroller extends BaseController {

	@Autowired
	private ITaskService taskService;

	/**
	 * <p>Title: 待办任务管理页</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年4月3日
	 * 
	 * @return
	 */
	@GetMapping("/pendingManager")
	public String pendingManager() {

		return "task/pendingTask";
	}

	/**
	 * <p>Title: 待办流程任务列表</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年4月6日
	 * 
	 * @return
	 */
	@PostMapping("/pendingGrid")
	@ResponseBody
	public Object pendingGrid(Integer page, Integer rows, String sort, String order) {
		PageInfo pageInfo = new PageInfo(page, rows, sort, order);
		taskService.selectPendingPage(pageInfo, this.getUserId());
		return pageInfo;
	}

	/**
	 * <p>Title: 批准流程</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年4月6日
	 * 
	 * @param paramId
	 * @param message
	 * @return
	 */
	@PostMapping("/approvalProcess")
	@ResponseBody
	public Object approvalProcess(Long paramId, String taskId, String message) {
		Assert.isNull(paramId, "请假ID不能为空");
		Assert.isBlank(message, "批准内容不能为空");

		if (taskService.approvalProcess(paramId, taskId, message, this.getShiroUser())) {
			return renderSuccess("批准成功");
		}
		return renderError("批准失败");
	}

	/**
	 * <p>Title: 已办任务管理页</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年4月3日
	 * 
	 * @return
	 */
	@GetMapping("/finishManager")
	public String finishManager() {

		return "task/finishTask";
	}

	/**
	 * <p>Title: 已完成任务</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年4月6日
	 * 
	 * @return
	 */
	@PostMapping("/finishTaskProcess")
	@ResponseBody
	public Object finishGrid(Integer page, Integer rows, String sort, String order) {
		PageInfo pageInfo = new PageInfo(page, rows, sort, order);
		taskService.finishTaskProcess(pageInfo, this.getUserId());
		return pageInfo;
	}
}

package com.xh.activiti.controller;

import java.util.Date;
import java.util.List;

import org.activiti.engine.task.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xh.activiti.commons.result.PageInfo;
import com.xh.activiti.commons.utils.Assert;
import com.xh.activiti.model.ActLeave;
import com.xh.activiti.service.IActLeaveService;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年4月3日
 */
@Controller
@RequestMapping("/leave")
public class ActLeaveController extends BaseController {

	@Autowired
	private IActLeaveService leaveService;

	/**
	 * <p>Title: 请假管理页</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年4月3日
	 * 
	 * @return
	 */
	@GetMapping("/manager")
	public String manager() {
		return "leave/leave";
	}

	/**
	 * <p>Title: 列表</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年4月3日
	 * 
	 * @param page
	 * @param rows
	 * @param sort
	 * @param order
	 * @return
	 */
	@PostMapping("/dataGrid")
	@ResponseBody
	public Object dataGrid(Integer page, Integer rows, String sort, String order) {
		PageInfo pageInfo = new PageInfo(page, rows, sort, order);
		leaveService.selectPage(pageInfo, this.getUserId());
		return pageInfo;
	}

	/**
	 * <p>Title: 添加</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年4月3日
	 * 
	 * @param leave
	 * @return
	 */
	@PostMapping("/add")
	@ResponseBody
	public Object add(ActLeave leave) {
		leave.setUserId(this.getUserId());
		leave.setCreateDate(new Date());
		leave.setState(ActLeave.LeaveState.UNSUBMITTED.getValue());
		leave.setApprovalInfo("待提交申请");
		
		if (leaveService.insert(leave)) {
			return renderSuccess("添加成功");
		}
		return renderError("添加失败");
	}

	/**
	 * <p>Title: 更新</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年4月3日
	 * 
	 * @param leave
	 * @return
	 */
	@PostMapping("/edit")
	@ResponseBody
	public Object edit(ActLeave leave) {
		if (leaveService.updateById(leave)) {
			return renderSuccess("更新成功");
		}
		return renderError("更新失败");
	}

	/**
	 * <p>Title: 删除</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年4月3日
	 * 
	 * @param paramId
	 * @return
	 */
	@PostMapping("/remove")
	@ResponseBody
	public Object remove(Long paramId) {
		if (leaveService.deleteById(paramId)) {
			return renderSuccess("删除成功");
		}
		return renderError("删除失败");
	}

	/**
	 * <p>Title: 提交申请</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年4月6日
	 * 
	 * @param paramId
	 * @return
	 */
	@PostMapping("/submitApplication")
	@ResponseBody
	public Object submitApplication(Long paramId) {
		Assert.isNull(paramId, "请假ID不能为空！");
		if (leaveService.updateSubmitApplication(paramId, this.getUserId())) {
			return renderSuccess("提交成功");
		}
		return renderError("提交失败");
	}

	/**
	 * <p>Title: 查看历史批注列表(查看请假)</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年4月6日
	 * 
	 * @param processInstanceId
	 * @return
	 */
	@GetMapping("/historyCommentGrid/{processInstanceId}")
	@ResponseBody
	public Object historyCommentGrid(@PathVariable("processInstanceId") String processInstanceId) {
		Assert.isBlank(processInstanceId, "流程定义ID不能为空！");

		List<Comment> comments = leaveService.queryHistoryComment(processInstanceId);
		return comments;
	}
}

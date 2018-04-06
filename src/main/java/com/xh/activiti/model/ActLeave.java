package com.xh.activiti.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * <p>Title: 请假表</p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * 
 * @date 2018-03-28
 */
@TableName("act_leave")
public class ActLeave {
	private Long id;
	// 请假内容
	private String content;
	@TableField(value = "approval_info")
	private String approvalInfo;
	// 请假天数
	private Double days;
	// 备注
	private String remark;
	// 请假状态
	private Integer state;
	@TableField(value = "user_id")
	private Long userId;
	// 流程任务ID
	@TableField(value = "task_id")
	private String taskId;
	// 流程实例ID
	@TableField(value = "process_instance_id")
	private String processInstanceId;
	// 开始时间
	@TableField(value = "start_date")
	private Date startDate;
	// 结束时间
	@TableField(value = "end_date")
	private Date endDate;
	// 创建时间
	@TableField(value = "create_date")
	private Date createDate;

	@TableField(exist = false)
	private String userName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getApprovalInfo() {
		return approvalInfo;
	}

	public void setApprovalInfo(String approvalInfo) {
		this.approvalInfo = approvalInfo;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Double getDays() {
		return days;
	}

	public void setDays(Double days) {
		this.days = days;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "ActLeave [id=" + id + ", content=" + content + ", days=" + days + ", remark=" + remark + ", state=" + state + ", userId="
				+ userId + ", taskId=" + taskId + ", processInstanceId=" + processInstanceId + ", startDate=" + startDate + ", endDate="
				+ endDate + ", createDate=" + createDate + "]";
	}

	/**
	 * <p>Title: 枚举</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年4月3日
	 * 
	 */
	public enum LeaveState {
		/**0, "未提交"*/
		UNSUBMITTED(0, "未提交"), //
		/**1, "审批通过"*/
		APPROVAL(1, "审批通过"), //
		/**2, "审批拒绝"*/
		REFUSE(2, "审批拒绝"), //
		/**3, "审批中"*/
		UNDER_REVIEW(3, "审批中"), //
		/**4, "驳回"*/
		TURN_DOWN(4, "驳回"),//
		/**推送信息：-1, 已审批*/
		NEXT_INFO(-1, "已审批");

		private Integer value;
		private String name;

		LeaveState(Integer value, String name) {
			this.value = value;
			this.name = name;
		}

		public Integer getValue() {
			return value;
		}

		public String getName() {
			return name;
		}
	}
}

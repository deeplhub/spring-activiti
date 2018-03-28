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
@TableName("leave_bill")
public class LeaveBill {
	private Long id;
	// 请假内容
	private String content;
	// 请假天数
	private Double days;
	// 备注
	private String remark;
	// 请假状态
	private Integer state;
	@TableField(value = "user_id")
	private Integer userId;
	// 开始时间
	@TableField(value = "start_date")
	private Date startDate;
	// 结束时间
	@TableField(value = "end_date")
	private Date endDate;
	// 创建时间
	@TableField(value = "create_date")
	private Date createDate;

	public void setid(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	/**
	 * 请假内容
	 */
	public void setcontent(String content) {
		this.content = content;
	}

	/**
	 * 请假内容
	 */
	public String getContent() {
		return content;
	}

	/**
	 * 请假天数
	 */
	public void setdays(Double days) {
		this.days = days;
	}

	/**
	 * 请假天数
	 */
	public Double getDays() {
		return days;
	}

	/**
	 * 备注
	 */
	public void setremark(String remark) {
		this.remark = remark;
	}

	/**
	 * 备注
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * 请假状态
	 */
	public void setstate(Integer state) {
		this.state = state;
	}

	/**
	 * 请假状态
	 */
	public Integer getState() {
		return state;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getUserId() {
		return userId;
	}

	/**
	 * 开始时间
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * 开始时间
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * 结束时间
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * 结束时间
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * 创建时间
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * 创建时间
	 */
	public Date getCreateDate() {
		return createDate;
	}
}

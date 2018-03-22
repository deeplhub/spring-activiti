package com.xh.activiti.model;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotations.TableField;

/**
 * <p>Title: 组织机构</p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * 
 * @date 2018-03-14
 */
public class Organization {
	// 主键id
	private Long id;
	// 组织名
	private String name;
	// 地址
	private String address;
	// 编号
	private String code;
	// 图标
	private String icon;
	// 父级主键
	private Long pid;
	// 排序
	private Integer seq;
	// 创建时间
	@TableField(value = "create_time")
	private Date createTime;

	@TableField(exist = false)
	private List<Organization> children;
	// 展开或折叠状态
	@TableField(exist = false)
	private String state;

	/**
	 * 主键id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 主键id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * 组织名
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 组织名
	 */
	public String getName() {
		return name;
	}

	/**
	 * 地址
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * 地址
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * 编号
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 编号
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 图标
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * 图标
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * 父级主键
	 */
	public void setPid(Long pid) {
		this.pid = pid;
	}

	/**
	 * 父级主键
	 */
	public Long getPid() {
		return pid;
	}

	/**
	 * 排序
	 */
	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	/**
	 * 排序
	 */
	public Integer getSeq() {
		return seq;
	}

	/**
	 * 创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * 创建时间
	 */
	public Date getCreateTime() {
		return createTime;
	}

	public List<Organization> getChildren() {
		return children;
	}

	public void setChildren(List<Organization> children) {
		this.children = children;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}

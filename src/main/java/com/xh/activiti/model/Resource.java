package com.xh.activiti.model;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotations.TableField;

/**
 * <p>Title: 资源</p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * 
 * @date 2018-03-14
 */
public class Resource {
	// 主键
	private Long id;
	// 资源名称
	private String name;
	// 资源路径
	private String url;
	// 打开方式 ajax,iframe
	@TableField(value = "open_mode")
	private String openMode;
	// 资源介绍
	private String description;
	// 资源图标
	private String icon;
	// 父级资源id
	private Long pid;
	// 排序
	private Integer seq;
	// 状态
	private Integer status;
	// 打开状态
	private Integer opened;
	// 资源类别
	@TableField(value = "resource_type")
	private Integer resourceType;
	// 创建时间
	@TableField(value = "create_time")
	private Date createTime;

	@TableField(exist = false)
	private List<Resource> children;
	// 展开或折叠状态
	@TableField(exist = false)
	private String state = "closed";

	/**
	 * 主键
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 主键
	 */
	public Long getId() {
		return id;
	}

	/**
	 * 资源名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 资源名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 资源路径
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 资源路径
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 打开方式 ajax,iframe
	 */
	public void setOpenMode(String openMode) {
		this.openMode = openMode;
	}

	/**
	 * 打开方式 ajax,iframe
	 */
	public String getOpenMode() {
		return openMode;
	}

	/**
	 * 资源介绍
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 资源介绍
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 资源图标
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * 资源图标
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * 父级资源id
	 */
	public void setPid(Long pid) {
		this.pid = pid;
	}

	/**
	 * 父级资源id
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
	 * 状态
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * 状态
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * 打开状态
	 */
	public void setOpened(Integer opened) {
		this.opened = opened;
	}

	/**
	 * 打开状态
	 */
	public Integer getOpened() {
		return opened;
	}

	/**
	 * 资源类别
	 */
	public void setResourceType(Integer resourceType) {
		this.resourceType = resourceType;
	}

	/**
	 * 资源类别
	 */
	public Integer getResourceType() {
		return resourceType;
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

	public List<Resource> getChildren() {
		return children;
	}

	public void setChildren(List<Resource> children) {
		this.children = children;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}

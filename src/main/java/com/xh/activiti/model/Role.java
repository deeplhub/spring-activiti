package com.xh.activiti.model;

/**
 * <p>Title: 角色</p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * 
 * @date 2018-03-14
 */ 
public class Role{
	// 主键id
	private Long id;
	// 角色名
	private String name;
	// 排序号
	private Integer seq;
	// 简介
	private String description;
	// 状态
	private Integer status;

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
	 * 角色名
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 角色名
	 */
	public String getName() {
		return name;
	}

	/**
	 * 排序号
	 */
	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	/**
	 * 排序号
	 */
	public Integer getSeq() {
		return seq;
	}

	/**
	 * 简介
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 简介
	 */
	public String getDescription() {
		return description;
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
}

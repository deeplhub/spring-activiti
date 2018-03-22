package com.xh.activiti.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * <p>Title: 角色资源</p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * 
 * @date 2018-03-14
 */ 
@TableName("role_resource")
public class RoleResource{
	// 主键id
	private Long id;
	// 角色id
	@TableField(value = "role_id")
	private Long roleId;
	// 资源id
	@TableField(value = "resource_id")
	private Long resourceId;

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
	 * 角色id
	 */
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	/**
	 * 角色id
	 */
	public Long getRoleId() {
		return roleId;
	}

	/**
	 * 资源id
	 */
	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

	/**
	 * 资源id
	 */
	public Long getResourceId() {
		return resourceId;
	}
}

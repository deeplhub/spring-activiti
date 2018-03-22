package com.xh.activiti.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * <p>Title: 用户角色</p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * 
 * @date 2018-03-14
 */ 
@TableName("user_role")
public class UserRole{
	// 主键id
	private Long id;
	// 用户id
	@TableField(value = "user_id")
	private Long userId;
	// 角色id
	@TableField(value = "role_id")
	private Long roleId;

	/**
	 * 主键id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * 主键id
	 */
	public long getId() {
		return id;
	}

	/**
	 * 用户id
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}

	/**
	 * 用户id
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * 角色id
	 */
	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	/**
	 * 角色id
	 */
	public long getRoleId() {
		return roleId;
	}
}

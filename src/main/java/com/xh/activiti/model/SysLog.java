package com.xh.activiti.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * <p>Title: 系统日志</p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * 
 * @date 2018-03-14
 */ 
@TableName("sys_log")
public class SysLog{
	// 主键id
	private Long id;
	// 登陆名
	@TableField(value = "login_name")
	private String loginName;
	// 角色名
	@TableField(value = "role_name")
	private String roleName;
	// 内容
	@TableField(value = "opt_content")
	private String optContent;
	// 客户端ip
	@TableField(value = "client_ip")
	private String clientIp;
	// 创建时间
	@TableField(value = "create_time")
	private Date createTime;

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
	 * 登陆名
	 */
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	/**
	 * 登陆名
	 */
	public String getLoginName() {
		return loginName;
	}

	/**
	 * 角色名
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	/**
	 * 角色名
	 */
	public String getRoleName() {
		return roleName;
	}

	/**
	 * 内容
	 */
	public void setOptContent(String optContent) {
		this.optContent = optContent;
	}

	/**
	 * 内容
	 */
	public String getOptContent() {
		return optContent;
	}

	/**
	 * 客户端ip
	 */
	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	/**
	 * 客户端ip
	 */
	public String getClientIp() {
		return clientIp;
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
}

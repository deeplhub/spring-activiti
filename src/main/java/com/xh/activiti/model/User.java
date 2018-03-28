package com.xh.activiti.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;

/**
 * <p>Title: 用户</p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * 
 * @date 2018-03-14
 */
public class User {
	// 主键id
	private Long id;
	// 登陆名
	@TableField(value = "login_name")
	private String loginName;
	// 用户名
	private String name;
	// 密码
	private String password;
	// 密码加密盐
	private String salt;
	// 性别
	private Integer sex;
	// 年龄
	private Integer age;
	// 手机号
	private String phone;
	// 用户类别
	@TableField(value = "user_type")
	private Integer userType;
	// 用户状态
	private Integer status;
	// 所属机构
	@TableField(value = "organization_id")
	private Long organizationId;
	// 是否管理员
	@TableField(value = "is_admin")
	private boolean isAdmin;
	// 创建时间
	@TableField(value = "create_time")
	private Date createTime;

	@TableField(exist = false)
	private String organizationName;
	@TableField(exist = false)
	private Long roleId;
	@TableField(exist = false)
	private String roleIds;
	@TableField(exist = false)
	private String roleName;

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
	 * 用户名
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 用户名
	 */
	public String getName() {
		return name;
	}

	/**
	 * 密码
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 密码
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * 密码加密盐
	 */
	public void setSalt(String salt) {
		this.salt = salt;
	}

	/**
	 * 密码加密盐
	 */
	public String getSalt() {
		return salt;
	}

	/**
	 * 性别
	 */
	public void setSex(Integer sex) {
		this.sex = sex;
	}

	/**
	 * 性别
	 */
	public Integer getSex() {
		return sex;
	}

	/**
	 * 年龄
	 */
	public void setAge(Integer age) {
		this.age = age;
	}

	/**
	 * 年龄
	 */
	public Integer getAge() {
		return age;
	}

	/**
	 * 手机号
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * 手机号
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * 用户类别
	 */
	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	/**
	 * 用户类别
	 */
	public Integer getUserType() {
		return userType;
	}

	/**
	 * 用户状态
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * 用户状态
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * 所属机构
	 */
	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	/**
	 * 所属机构
	 */
	public Long getOrganizationId() {
		return organizationId;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
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

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", loginName=" + loginName + ", name=" + name + ", password=" + password + ", salt=" + salt + ", sex="
				+ sex + ", age=" + age + ", phone=" + phone + ", userType=" + userType + ", status=" + status + ", organizationId="
				+ organizationId + ", isAdmin=" + isAdmin + ", createTime=" + createTime + ", organizationName=" + organizationName
				+ ", roleId=" + roleId + ", roleIds=" + roleIds + ", roleName=" + roleName + "]";
	}

}

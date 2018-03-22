package com.xh.activiti.dao;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xh.activiti.model.UserRole;

/**
 * <p>Title: 用户角色表</p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年3月14日
 */
public interface IUserRoleDao extends BaseMapper<UserRole> {

	/**
	 * <p>Title: 根据用户ID查询当前用户下的角色信息</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月20日
	 * 
	 * @param userId
	 * @return
	 */
	List<UserRole> selectByUserId(Long userId);

	/**
	 * <p>Title: 根据用户ID查询当前用户下的角色ID</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月20日
	 * 
	 * @param userId
	 * @return
	 */
	List<Long> selectRoleIdListByUserId(Long userId);
	
	/**
	 * <p>Title: 根据用户ID删除用户与角色中间表</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月20日
	 * 
	 * @param userId
	 * @return
	 */
	int deleteByUserId(Long userId);

	/**
	 * <p>Title: 根据角色ID删除用户角色中间表</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月20日
	 * 
	 * @param roleId
	 */
	int deleteByRoleId(Long roleId);
}

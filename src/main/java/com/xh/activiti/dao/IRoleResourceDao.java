package com.xh.activiti.dao;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xh.activiti.model.RoleResource;

/**
 * <p>Title: 角色权限表</p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年3月14日
 */
public interface IRoleResourceDao extends BaseMapper<RoleResource> {

	/**
	 * <p>Title: 根据资源ID删除资源与角色中间表</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月20日
	 * 
	 * @param paramId
	 * @return
	 */
	int deleteByListResourceId(List<String> paramId);

	/**
	 * <p>Title: 根据角色ID删除资源管理与角色中间表</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月20日
	 * 
	 * @param roleId
	 */
	@Deprecated
	void deleteByRoleId(Long roleId);
	
	/**
	 * <p>Title: 根据角色ID查询角色与资源管理中间表</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月20日
	 * 
	 * @param roleId
	 * @return
	 */
	List<Long> selectRoleResourceListByRoleId(Long roleId);
}

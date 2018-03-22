package com.xh.activiti.dao;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xh.activiti.model.Resource;
import com.xh.activiti.model.Role;

/**
 * <p>Title: 角色表</p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年3月14日
 */
public interface IRoleDao extends BaseMapper<Role>{

	/**
	 * <p>Title: 根据用户与角色中间表的角色ID查询资源管理列表</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月20日
	 * 
	 * @param list
	 * @return
	 */
	List<Resource> selectResourceListByRoleIdList(List<Long> list);

	/**
	 * <p>Title: 根据角色ID查询资源管理列表</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月20日
	 * 
	 * @param id
	 * @return
	 */
	List<Resource> selectResourceListByRoleId(Long id);
}

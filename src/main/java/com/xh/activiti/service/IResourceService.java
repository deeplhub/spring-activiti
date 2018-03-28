package com.xh.activiti.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.xh.activiti.commons.result.Tree;
import com.xh.activiti.commons.shiro.ShiroUser;
import com.xh.activiti.model.Resource;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年3月14日
 */
public interface IResourceService extends IService<Resource> {

	/**
	 * <p>Title: 资源管理列表</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月17日
	 * 
	 * @return
	 */
	List<Resource> selectTreeGridAll();

	/**
	 * <p>Title: 根据用户权限查询菜单</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月17日
	 * 
	 * @param shiroUser
	 * @return
	 */
	List<Tree> selectTree(ShiroUser shiroUser);

	/**
	 * <p>Title: 删除菜单</p>
	 * <p>Description: 删除父级或子级菜单时先删除菜单和角色关联信息，再删除菜单</p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月17日
	 * 
	 * @param paramId
	 * @return
	 */
	boolean deleteBatchIds(Long paramId);

	/**
	 * <p>Title: 查询所有菜单，并根据角色ID对已授权的菜单做上标识</p>
	 * <p>Description: 角色授权时用</p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月20日
	 * 
	 * @param paramId
	 * @return
	 */
	List<Tree> selectResourceListByRoleIdTree(Long paramId);

}

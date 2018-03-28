package com.xh.activiti.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.baomidou.mybatisplus.service.IService;
import com.xh.activiti.commons.result.PageInfo;
import com.xh.activiti.commons.result.Tree;
import com.xh.activiti.model.Role;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年3月14日
 */
public interface IRoleService extends IService<Role> {

	/**
	 * <p>Title: 角色列表-分页查询</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月18日
	 * 
	 * @param pageInfo
	 */
	void selectDataGrid(PageInfo pageInfo);

	/**
	 * <p>Title: 角色与菜单的授权</p>
	 * <p>Description: 授权时先删除当前角色已有的授权信息，再保存</p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月20日
	 * 
	 * @param roleId
	 * @param resourceIds
	 */
	boolean updateRoleResourceAuthorized(Long roleId, String resourceIds);

	/**
	 * <p>Title: 根据userId查询资源</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月14日
	 * 
	 * @param userId
	 * @return
	 */
	Map<String, Set<String>> selectResourceMapByUserId(Long userId);

	/**
	 * <p>Title: 删除角色</p>
	 * <p>Description: 删除角色，并删除用户与角色中间表，删除资源管理与角色中间表</p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月19日
	 * 
	 * @param id
	 * @return
	 */
	boolean deleteBatchIds(Long paramId);

	/**
	 * <p>Title: 用户与角色的授权</p>
	 * <p>Description: 授权时先删除当前角色已有的授权信息，再保存</p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月23日
	 * 
	 * @param paramId
	 * @param paramRoleId
	 * @return
	 */
	boolean updateUserRoleAuthorized(Long paramId, String roleIds);

	/**
	 * <p>Title: 角色权限树</p>
     * <p>Description: 查询所有角色，并根据用户ID对已授权的角色做上标识</p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月23日
	 * 
	 * @return
	 */
	List<Tree> selectRoleListByUserIdTree(Long userId);
}

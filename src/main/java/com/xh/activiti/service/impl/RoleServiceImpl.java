package com.xh.activiti.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xh.activiti.commons.result.PageInfo;
import com.xh.activiti.dao.IRoleDao;
import com.xh.activiti.dao.IRoleResourceDao;
import com.xh.activiti.dao.IUserRoleDao;
import com.xh.activiti.model.Resource;
import com.xh.activiti.model.Role;
import com.xh.activiti.model.RoleResource;
import com.xh.activiti.service.IRoleService;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年3月14日
 */
@Service
public class RoleServiceImpl extends ServiceImpl<IRoleDao, Role> implements IRoleService {

	@Autowired
	private IRoleDao roleDao;

	@Autowired
	private IUserRoleDao userRoleDao;

	@Autowired
	private IRoleResourceDao roleResourceDao;

	/**
	 * <p>Title: 角色列表-分页查询</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月18日
	 * 
	 * @param pageInfo
	 */
	@Override
	public void selectDataGrid(PageInfo pageInfo) {
		Page<Role> page = new Page<Role>(pageInfo.getNowpage(), pageInfo.getSize());

		EntityWrapper<Role> wrapper = new EntityWrapper<Role>();
		wrapper.orderBy(pageInfo.getSort(), pageInfo.getOrder().equalsIgnoreCase("ASC"));
		selectPage(page, wrapper);

		pageInfo.setRows(page.getRecords());
		pageInfo.setTotal(page.getTotal());
	}

	/**
	 * <p>Title: 根据userId查询资源</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月20日
	 * 
	 * @param userId
	 * @return
	 */
	@Override
	public Map<String, Set<String>> selectResourceMapByUserId(Long userId) {
		Map<String, Set<String>> resourceMap = new HashMap<String, Set<String>>();
		List<Long> roleIdList = userRoleDao.selectRoleIdListByUserId(userId);
		Set<String> urlSet = new HashSet<String>();
		Set<String> roles = new HashSet<String>();
		for (Long roleId : roleIdList) {
			List<Resource> resourceList = roleDao.selectResourceListByRoleId(roleId);
			if (resourceList != null) {
				for (Resource resource : resourceList) {
					if (resource != null && StringUtils.isNotBlank(resource.getUrl())) {
						urlSet.add(resource.getUrl());
					}
				}
			}
			Role role = roleDao.selectById(roleId);
			if (role != null) {
				roles.add(role.getName());
			}
		}
		resourceMap.put("urls", urlSet);
		resourceMap.put("roles", roles);
		return resourceMap;
	}

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
	@Override
	public boolean deleteBatchIds(Long paramId) {
		//默认管理员角色不能删除
		Role role = roleDao.selectById(paramId);
		if(role.getName().equals("admin")) {
			return false;
		}
		// 删除用户与角色中间表
		userRoleDao.deleteByRoleId(paramId);
		// 删除资源管理与角色中间表
		roleResourceDao.deleteByRoleId(paramId);
		// 角色
		return super.deleteById(paramId);
	}
	
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
	@Override
	public boolean updateAuthorized(Long roleId, String resourceIds) {
		// 先删除后添加,有点爆力
		RoleResource roleResource = new RoleResource();
		roleResource.setRoleId(roleId);
		roleResourceDao.delete(new EntityWrapper<RoleResource>(roleResource));

		String[] resourceIdArray = resourceIds.split(",");
		for (String resourceId : resourceIdArray) {
			roleResource = new RoleResource();
			roleResource.setRoleId(roleId);
			roleResource.setResourceId(Long.parseLong(resourceId));
			roleResourceDao.insert(roleResource);
		}
		return true;
	}
}

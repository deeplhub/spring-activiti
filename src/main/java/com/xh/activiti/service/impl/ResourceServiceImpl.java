package com.xh.activiti.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xh.activiti.commons.result.Tree;
import com.xh.activiti.commons.shiro.ShiroUser;
import com.xh.activiti.dao.IResourceDao;
import com.xh.activiti.dao.IRoleDao;
import com.xh.activiti.dao.IRoleResourceDao;
import com.xh.activiti.dao.IUserRoleDao;
import com.xh.activiti.model.Resource;
import com.xh.activiti.service.IResourceService;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年3月14日
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<IResourceDao, Resource> implements IResourceService {
	private static final int RESOURCE_MENU = 0; // 菜单

	@Autowired
	private IResourceDao resourceDao;

	@Autowired
	private IUserRoleDao userRoleDao;

	@Autowired
	private IRoleDao roleDao;

	@Autowired
	private IRoleResourceDao roleResourceDao;

	/**
	 * <p>Title: 查询-资源管理列表</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月17日
	 * 
	 * @return
	 */
	@Override
	public List<Resource> selectTreeGridAll() {
		EntityWrapper<Resource> wrapper = new EntityWrapper<Resource>();
		wrapper.orderBy("seq");

		return buildByRecursive(resourceDao.selectList(wrapper));
	}

	/**
	 * <p>Title: 查询-根据用户权限查询菜单</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月17日
	 * 
	 * @param shiroUser
	 * @return
	 */
	@Override
	public List<Tree> selectTree(ShiroUser shiroUser) {
		List<Tree> trees = new ArrayList<Tree>();
		// shiro中缓存的用户角色
		Set<String> roles = shiroUser.getRoles();
		if (roles == null) {
			return trees;
		}
		// 如果有超级管理员权限
		if (roles.contains("admin")) {
			List<Resource> resourceList = this.selectByType(RESOURCE_MENU);
			if (resourceList == null) {
				return trees;
			}
			for (Resource resource : resourceList) {
				Tree tree = new Tree();
				tree.setId(resource.getId());
				tree.setpId(resource.getPid());
				tree.setName(resource.getName());
				tree.setIconCls(resource.getIcon());
				tree.setAttributes(resource.getUrl());
				tree.setOpenMode(resource.getOpenMode());
				tree.setState(resource.getOpened());
				trees.add(tree);
			}
			return trees;
		}
		// 普通用户
		List<Long> roleIdList = userRoleDao.selectRoleIdListByUserId(shiroUser.getId());
		if (roleIdList == null) {
			return trees;
		}
		List<Resource> resourceLists = roleDao.selectResourceListByRoleIdList(roleIdList);
		if (resourceLists == null) {
			return trees;
		}
		for (Resource resource : resourceLists) {
			Tree tree = new Tree();
			tree.setId(resource.getId());
			tree.setpId(resource.getPid());
			tree.setName(resource.getName());
			tree.setIconCls(resource.getIcon());
			tree.setAttributes(resource.getUrl());
			tree.setOpenMode(resource.getOpenMode());
			tree.setState(resource.getOpened());
			trees.add(tree);
		}
		return trees;
	}

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
	public boolean deleteBatchIds(Long paramId) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("paramId", paramId);
		paramMap.put("resultIds", null);
		resourceDao.getResourceChildIds(paramMap);

		String param = (String) paramMap.get("resultIds");
		List<String> list = Arrays.asList(param.split(","));
		// 删除资源与角色中间表
		roleResourceDao.deleteByListResourceId(list);
		// 删除资源管理
		return super.deleteBatchIds(list);
	}

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
	@Override
	public List<Tree> selectResourceListByRoleIdTree(Long paramId) {
		List<Tree> trees = new ArrayList<Tree>();
		// 查询所有资源管理
		List<Resource> resourceList = resourceDao.selectList(null);

		// 查询当前角色下的资源管理列表
		List<Long> roleResourceList = roleResourceDao.selectRoleResourceListByRoleId(paramId);

		Tree tree = null;
		for (Resource resource : resourceList) {
			tree = new Tree();
			for (Long roleResourceId : roleResourceList) {
				if (resource.getId().equals(roleResourceId)) {
					tree.setChecked(true);
				}
			}
			tree.setId(resource.getId());
			tree.setpId(resource.getPid());
			tree.setName(resource.getName());
			tree.setIconCls(resource.getIcon());
			tree.setAttributes(resource.getUrl());
			tree.setOpenMode(resource.getOpenMode());
			tree.setState(resource.getOpened());
			trees.add(tree);
		}

		return trees;
	}

	private List<Resource> selectByType(Integer type) {
		EntityWrapper<Resource> wrapper = new EntityWrapper<Resource>();
		Resource resource = new Resource();
		wrapper.setEntity(resource);
		wrapper.addFilter("resource_type = {0}", type);
		wrapper.orderBy("seq");
		return resourceDao.selectList(wrapper);
	}

	/**
	 * <p>Title: 使用递归方法建树 （默认State=closed,关闭状态）</p>
	 * <p>Description: 递归比两层循环机快很多倍</p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月15日
	 * 
	 * @param treeNodes
	 * @return
	 */
	private static List<Resource> buildByRecursive(List<Resource> treeNodes) {
		List<Resource> trees = new ArrayList<Resource>();
		for (Resource treeNode : treeNodes) {
			if (treeNode.getPid() == null) {
				treeNode.setState("open");
				trees.add(findChildren(treeNode, treeNodes));
			}
		}
		return trees;
	}

	/**
	 * <p>Title: 递归查找子节点 </p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月15日
	 * 
	 * @param treeNode
	 * @param treeNodes
	 * @return
	 */
	private static Resource findChildren(Resource treeNode, List<Resource> treeNodes) {
		for (Resource it : treeNodes) {
			if (treeNode.getId().equals(it.getPid())) {
				if (treeNode.getChildren() == null) {
					treeNode.setChildren(new ArrayList<Resource>());
				}
				//关闭第子级菜单
				it.setState("closed");
				treeNode.getChildren().add(findChildren(it, treeNodes));
			} else {
				//打开没有下级的菜单
				if (it.getChildren() == null) {
					it.setState("open");
				}
			}
		}
		return treeNode;
	}
}

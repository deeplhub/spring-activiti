package com.xh.activiti.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xh.activiti.commons.result.Tree;
import com.xh.activiti.commons.utils.Assert;
import com.xh.activiti.dao.IOrganizationDao;
import com.xh.activiti.dao.IUserDao;
import com.xh.activiti.model.Organization;
import com.xh.activiti.service.IOrganizationService;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年3月14日
 */
@Service
public class OrganizationServiceImpl extends ServiceImpl<IOrganizationDao, Organization> implements IOrganizationService {

	@Autowired
	private IOrganizationDao organizationDao;
	
	@Autowired
	private IUserDao userDao;

	/**
	 * <p>Title: 树型菜单</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月22日
	 * 
	 * @return
	 */
	@Override
	public List<Tree> selectTree() {
		EntityWrapper<Organization> wrapper = new EntityWrapper<Organization>();
		wrapper.orderBy("seq");
		List<Organization> organizationList = organizationDao.selectList(wrapper);

		List<Tree> trees = new ArrayList<Tree>();
		if (organizationList != null) {
			for (Organization organization : organizationList) {
				Tree tree = new Tree();
				tree.setId(organization.getId());
				tree.setName(organization.getName());
				tree.setIconCls(organization.getIcon());
				tree.setpId(organization.getPid());
				trees.add(tree);
			}
		}
		return trees;
	}

	/**
	 * <p>Title: 分页列表</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月22日
	 * 
	 * @return
	 */
	@Override
	public List<Organization> selectTreeGrid() {
		EntityWrapper<Organization> wrapper = new EntityWrapper<Organization>();
		wrapper.orderBy("seq");
		return buildByRecursive(organizationDao.selectList(wrapper));
	}

	/**
	 * <p>Title: 使用递归方法建树 </p>
	 * <p>Description: 递归比两层循环机快很多倍</p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月15日
	 * 
	 * @param treeNodes
	 * @return
	 */
	private static List<Organization> buildByRecursive(List<Organization> treeNodes) {
		List<Organization> trees = new ArrayList<Organization>();
		for (Organization treeNode : treeNodes) {
			if (treeNode.getPid() == null) {
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
	private static Organization findChildren(Organization treeNode, List<Organization> treeNodes) {
		for (Organization it : treeNodes) {
			if (treeNode.getId().equals(it.getPid())) {
				if (treeNode.getChildren() == null) {
					treeNode.setChildren(new ArrayList<Organization>());
				}
				// it.setState("closed");
				treeNode.getChildren().add(findChildren(it, treeNodes));
			}
		}
		return treeNode;
	}

	/**
	 * <p>Title: 删除部门</p>
	 * <p>Description: 删除部门前先检查当前部门下有没有用户，如果有用户就提示不能删除，否则删除部门</p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月22日
	 * 
	 * @param paramId
	 * @return
	 */
	@Override
	public boolean deleteById(Serializable paramId) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("paramId", paramId);
		paramMap.put("resultIds", null);
		organizationDao.getOrganizationChildIds(paramMap);
		
		String param = (String) paramMap.get("resultIds");
		List<String> list = Arrays.asList(param.split(","));
		
		int count = userDao.selectCount(list);
		if(count > 0) {
			Assert.isNull(null, "删除失败，当前部门下有 [" + count + "] 个用户！");
		}
		return super.deleteById(paramId);
	}
	
	
}

package com.xh.activiti.test.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xh.activiti.commons.result.Tree;
import com.xh.activiti.commons.shiro.ShiroUser;
import com.xh.activiti.model.Resource;
import com.xh.activiti.service.IResourceService;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年3月15日
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/spring-config.xml" })
public class ResourceServiceTest {

	@Autowired
	private IResourceService resourceService;

	// @Test
	public void selectZTree() {

		Set<String> roles = new HashSet<>();
		roles.add("admin");
		ShiroUser shiroUser = new ShiroUser("admin");
		shiroUser.setRoles(roles);
		List<Tree> listTree = resourceService.selectTree(shiroUser);
		System.out.println(JSON.toJSON(listTree));
	}

	// @Test
	public void selectByType() {
		Integer type = 0;
		EntityWrapper<Resource> wrapper = new EntityWrapper<Resource>();
		Resource resource = new Resource();
		wrapper.setEntity(resource);
		wrapper.addFilter("resource_type = {0}", type);
		wrapper.orderBy("seq");
		// resourceDao.selectList(wrapper);

		List<Resource> list = resourceService.selectList(wrapper);
		System.out.println(list.size());
	}

	// @Test
	public void call() {
		boolean flag = resourceService.deleteBatchIds((long) 1);
		System.out.println(flag);
	}

	@Test
	public void selectResourceListByRoleIdTree() {
		long roleId = 1;
		List<Tree> treeList = resourceService.selectResourceListByRoleIdTree(roleId);
		System.out.println(JSON.toJSON(treeList));
	}
}

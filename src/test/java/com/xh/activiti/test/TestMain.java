package com.xh.activiti.test;

import java.util.List;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年3月28日
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/spring-config.xml" })
public class TestMain {

	@Autowired
	private RepositoryService repositoryService;

	@Test
	public void queryDeploymentFlow() {
		List<Model> resultList = repositoryService.createModelQuery()//
				.orderByCreateTime()//
				.desc()//
				.list();
		for (Model model : resultList) {
			System.out.println(model.getId());
			System.out.println(model.getName());

			repositoryService.deleteModel(model.getId());
		}
	}
}

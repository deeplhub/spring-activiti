package com.xh.activiti.test.service;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.xh.activiti.commons.result.PageInfo;
import com.xh.activiti.service.ITaskService;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年4月6日
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/spring-config.xml" })
public class TaskServiceTest {

	@Autowired
	private ITaskService taskService;

	// @Test
	public void selectPendingPage() {
		PageInfo pageInfo = new PageInfo(1, 10);
		Long userId = (long) 28;
		taskService.selectPendingPage(pageInfo, userId);
		System.out.println(JSON.toJSON(pageInfo));

	}
}

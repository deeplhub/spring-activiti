package com.xh.activiti.test.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.xh.activiti.commons.utils.PageData;
import com.xh.activiti.service.activiti.IActivitiProcessService;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年3月30日
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/spring-config.xml" })
public class ActivitiProcessServiceTest {

	@Autowired
	private IActivitiProcessService processService;

	@Test
	public void queryList() {
		List<PageData> list = processService.selectDeployList();
		System.out.println(JSON.toJSON(list));
	}
}

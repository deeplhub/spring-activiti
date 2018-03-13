package com.xh.activiti.test;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年3月10日
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
		locations = { "classpath:spring/spring-config.xml", "classpath:spring/spring-activiti.xml", "classpath:spring/spring-mybatis.xml" })
public class TestActiviti {

	/**
	 * <p>Title: 查询流程定义列表</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月12日
	 *
	 */
	@Test
	public void queryActiviti() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		// 流程定义查询对象，查询表act_re_procdef
		ProcessDefinitionQuery query = processEngine.getRepositoryService()//
				.createProcessDefinitionQuery();
		List<ProcessDefinition> list = query.list();
		for (ProcessDefinition pd : list) {
			System.out.println(pd.getName() + "" + pd.getId());
		}
	}
}

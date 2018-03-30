package com.xh.activiti.test.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.repository.Model;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.xh.activiti.commons.utils.PageData;
import com.xh.activiti.service.activiti.IActivitiModelService;
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
public class ActivitiModelServiceTest {

	@Autowired
	private IActivitiModelService modelService;

	@Autowired
	private IActivitiProcessService processService;

	/**
	 * <p>Title: 查询模型列表</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月29日
	 * 
	 */
	// @Test
	public void queryModelList() {
		// List<Model> list = modelService.selectModelList();
		// for (Model model : list) {
		// System.out.println(model.getId());
		// System.out.println(model.getName());
		// System.out.println(model.getDeploymentId());
		// }

		List<Model> list = modelService.selectModelList();
		List<PageData> listPd = processService.selectDeployList();

		for (Model model : list) {
			System.out.println(model.getId());
			System.out.println(model.getName());
			System.out.println(model.getDeploymentId());
			System.out.println("#######################################");

			for (PageData pd : listPd) {
				if (model.getDeploymentId().equals(pd.getString("id"))) {
					System.out.println(pd.getString("id"));
					System.out.println(pd.getString("name"));
					System.out.println(pd.get("deploymentTime"));
				}
			}
		}

	}

	/**
	 * <p>Title: 查询模型列表</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月29日
	 * 
	 */
	@Test
	public void queryModelMapList() {
		List<PageData> list = modelService.selectModelMapList();
		System.out.println(JSON.toJSON(list));
	}

	/**
	 * <p>Title: 根据模型ID查询</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月29日
	 * 
	 */
	// @Test
	public void findByModelId() {
		String modelId = "85001";
		Model model = modelService.selectByModelId(modelId);

		System.out.println(model.getId());
		System.out.println(model.getName());
	}

	/**
	 * <p>Title: 删除模型流程</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月29日
	 * 
	 */
	// @Test
	public void remove() {
		String modelId = "87501";
		boolean flag = modelService.deleteModel(modelId);
		System.out.println("删除状态：" + flag);
	}

	/**
	 * <p>Title: 部署模型流程</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月29日
	 * 
	 */
	// @Test
	public void deploy() {
		String modelId = "85001";
		boolean falg = modelService.deployModel(modelId);
		System.out.println("部署状态：" + falg);
	}

	/**
	 * <p>Title: 删除流程部署</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月26日
	 * 
	 */
	// @Test
	public void deleteProcessDefinition() {
		// act_re_deployment表的ID
		String deploymentId = "117501";
		// 删除流程定义，包括启动过的流程
		boolean flag = processService.deleteProcessDefinition(deploymentId, true);
		System.out.println("流程删除状态:" + flag);
	}

	/**
	 * <p>Title: 查询部署表，对应act_re_deployment表</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月29日
	 * 
	 */
	// @Test
	public void selectDeployList() {
		List<PageData> list = processService.selectDeployList();
		for (PageData pd : list) {
			System.out.println("流程ID：" + pd.getString("id"));
			System.out.println("流程名称：" + pd.getString("name"));
			System.out.println("流程类别：" + pd.getString("category"));
			System.out.println("流程时间：" + pd.getString("deploymentTime"));
			System.out.println("################################");
		}
	}

	/**
	 * <p>Title: 查询流程定义（部署、删除、读取流程资源），对应act_re_procdef表</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月26日
	 * 
	 */
	// @Test
	public void queryFlow() {
		// 查询流程定义，可以排序，查询数量，分页等
		List<PageData> list = processService.selectDefinitionList();
		for (PageData pd : list) {
			System.out.println("流程部署ID： " + pd.getString("deploymentId"));
			System.out.println("流程定义ID： " + pd.getString("id"));
			System.out.println("流程定义key " + pd.getString("key"));
			System.out.println("流程设计名称： " + pd.getString("name"));
			System.out.println("################################");
		}
	}

	/**
	 * <p>Title: 启动流程实例</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月26日
	 * 
	 */
	// @Test
	public void startProcess() {
		String processDefinitionId = "92501";
		Map<String, Object> variables = new HashMap<>();
		variables.put("userName", "王五");

		boolean flag = processService.startProcess(processDefinitionId, new PageData(variables));
		System.out.println("流程启动状态：" + flag);
	}
}

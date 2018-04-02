package com.xh.activiti.test.activiti;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * <p>Title: 流程模型</p>
 * <p>Description: 创建模型->部署流程->启动流程 </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年3月28日
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/spring-config.xml" })
public class LeaveModelActivitiTest {

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private RuntimeService runtimeService;

	/**
	 * <p>Title: 创建模型</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月29日
	 * 
	 */
	// @Test
	public void create() {
		try {
			String name = "changhy请假流程", //
					key = "changhy", //
					description = "changhy---请假流程";
			ObjectMapper objectMapper = new ObjectMapper();
			ObjectNode editorNode = objectMapper.createObjectNode();
			editorNode.put("id", "canvas");
			editorNode.put("resourceId", "canvas");
			ObjectNode stencilSetNode = objectMapper.createObjectNode();
			stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
			editorNode.put("stencilset", stencilSetNode);
			Model modelData = repositoryService.newModel();

			ObjectNode modelObjectNode = objectMapper.createObjectNode();
			modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, name);
			modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
			modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
			modelData.setMetaInfo(modelObjectNode.toString());
			modelData.setName(name);
			modelData.setKey(key);

			// 保存模型
			repositoryService.saveModel(modelData);
			repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));
			System.out.println("模型ID：" + modelData.getId());
		} catch (Exception e) {
			e.printStackTrace();
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
	// @Test
	public void queryModelList() {
		List<Model> resultList = repositoryService.createModelQuery()//
				.orderByCreateTime()//
				.desc()//
				.list();
		for (Model model : resultList) {
			System.out.println(model.getId());
			System.out.println(model.getName());
		}
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
		Model model = repositoryService.getModel(modelId);

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
		repositoryService.deleteModel(modelId);
		System.out.println("删除成功");
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
		try {
			String modelId = "85001";
			Model modelData = repositoryService.getModel(modelId);
			System.out.println(modelData.getId());
			System.out.println(modelData.getName());
			System.out.println(modelData.getKey());

			ObjectNode modelNode = (ObjectNode) new ObjectMapper()//
					.readTree(repositoryService.getModelEditorSource(modelData.getId()));
			BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);

			byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(model);
			String processName = modelData.getName() + ".bpmn20.xml";

			Deployment deployment = repositoryService.createDeployment()//
					.name(modelData.getName())//
					.addString(processName, new String(bpmnBytes, "UTF-8"))//
					.deploy();
			System.out.println("部署成功，部署ID=" + deployment.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		String deploymentId = "115001";

		// 删除流程定义，只删除没有启动过的流程，如果流程启动则抛出异常
		// repositoryService.deleteDeployment(deploymentId);

		// 删除流程定义，包括启动过的流程
		repositoryService.deleteDeployment(deploymentId, true);
		System.out.println("流程删除成功");
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
	public void queryDeploymentFlow() {
		List<Deployment> list = repositoryService//
				.createDeploymentQuery()//
				.list();
		for (Deployment deployment : list) {
			System.out.println("流程ID：" + deployment.getId());
			System.out.println("流程名称：" + deployment.getName());
			System.out.println("流程类别：" + deployment.getCategory());
			System.out.println("流程时间：" + deployment.getDeploymentTime());
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
		List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()//
				.list();
		for (ProcessDefinition processDefinition : list) {
			System.out.println("流程组织机构： " + processDefinition.getCategory());
			System.out.println("流程部署ID： " + processDefinition.getDeploymentId());
			System.out.println("流程描述：       " + processDefinition.getDescription());
			System.out.println("流程图片文件： " + processDefinition.getDiagramResourceName());
			System.out.println("流程定义ID： " + processDefinition.getId());
			System.out.println("流程定义key " + processDefinition.getKey());
			System.out.println("流程设计名称： " + processDefinition.getName());
			System.out.println("流程定义文件： " + processDefinition.getResourceName());
			System.out.println("流程所有人ID：    " + processDefinition.getTenantId());
			System.out.println("流程版本：       " + processDefinition.getVersion());
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
	public void startFlow() {
		String processDefinitionId = "92501";
		Map<String, Object> variables = new HashMap<>();
		variables.put("userName", "王五");
		// ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("LeaveProcessKey", variables);
		ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinitionId, variables);

		// 流程实例中包含的信息
		System.out.println("当前活动节点  " + processInstance.getActivityId());
		System.out.println("关联业务键：  " + processInstance.getBusinessKey());
		System.out.println("流程部署id： " + processInstance.getDeploymentId());
		System.out.println("流程描述：       " + processInstance.getDescription());
		System.out.println("流程实例id： " + processInstance.getId());
		System.out.println("流程实例名称： " + processInstance.getName());
		System.out.println("父流程id：      " + processInstance.getParentId());
		System.out.println("流程定义id： " + processInstance.getProcessDefinitionId());
		System.out.println("流程定义key：    " + processInstance.getProcessDefinitionKey());
		System.out.println("流程定义名称： " + processInstance.getProcessDefinitionName());
		System.out.println("流程实例id： " + processInstance.getProcessInstanceId());
		System.out.println("流程所属人id：    " + processInstance.getTenantId());
		System.out.println("流程定义版本： " + processInstance.getProcessDefinitionVersion());
		System.out.println("流程变量：       " + processInstance.getProcessVariables().get("userName"));
		System.out.println("是否结束：       " + processInstance.isEnded());
		System.out.println("是否暂停：       " + processInstance.isSuspended());
		System.out.println("################################");
	}

	/**
	 * <p>Title: 导出模型-模型导出bpmn文件或xml文件</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年4月2日
	 * 
	 * @throws IOException
	 */
	@Test
	public void exportBpmnOrXml() throws IOException {
		String modelId = "85001", //
				filename = "";
		Model model = repositoryService.getModel(modelId);
		JsonNode jsonNode = new ObjectMapper().readTree(repositoryService.getModelEditorSource(model.getId()));
		BpmnModel bpmnModel = new BpmnJsonConverter().convertToBpmnModel(jsonNode);

		// filename = bpmnModel.getMainProcess().getId() + ".bpmn20.xml";
		filename = model.getName() + ".bpmn";
		byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(bpmnModel);

		ByteArrayInputStream in = new ByteArrayInputStream(bpmnBytes);

		File f = new File("E:/" + filename);
		FileUtils.copyInputStreamToFile(in, f);
	}

}

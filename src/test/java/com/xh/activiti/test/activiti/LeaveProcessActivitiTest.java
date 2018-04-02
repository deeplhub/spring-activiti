package com.xh.activiti.test.activiti;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * <p>Title: 请假流程</p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年3月26日
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/spring-config.xml" })
public class LeaveProcessActivitiTest {

	private static ProcessEngine processEngine = null;

	public static ProcessEngine getProcessEngine() {
		return ProcessEngines.getDefaultProcessEngine();
	}

	/**
	 * <p>Title: 流程部署</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月26日
	 * 
	 */
	// @Test
	public void deploymentFlow() {
		String tenantId = "LeaveProcessKey", //
				name = "LeaveProcessName";
		Deployment deployment = getProcessEngine().getRepositoryService()//
				.createDeployment()// 创建一个部署对象
				.name("请假流程2")// 添加部署名称
				.addClasspathResource("LeaveProcess.bpmn")// 从classpath的资源中加载，一次只能加载一个文件
				.deploy();// 完成部署

		System.out.println(deployment.getId());
		System.out.println(deployment.getName());
		System.out.println(deployment.getTenantId());
		System.out.println(deployment.getCategory());
		System.out.println(deployment.getDeploymentTime());
	}

	/**
	 * <p>Title: 查询部署表，对应act_re_deployment表</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月28日
	 * 
	 */
	// @Test
	public void queryDeploymentFlow() {
		List<Deployment> list = getProcessEngine().getRepositoryService()//
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
	 * <p>Title: 删除流程部署</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月26日
	 * 
	 */
	// @Test
	public void deleteProcessDefinition() {
		RepositoryService repositoryService = getProcessEngine().getRepositoryService();

		// act_re_deployment表的ID
		String deploymentId = "92501";

		// 删除流程定义，只删除没有启动过的流程，如果流程启动则抛出异常
		// repositoryService.deleteDeployment(deploymentId);

		// 删除流程定义，包括启动过的流程
		repositoryService.deleteDeployment(deploymentId, true);
		System.out.println("流程删除成功");
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
		// 创建流程实例 - 运行时Service，可以也拿过来处理所有正在运行状态的流程实例、任务等
		RuntimeService runtimeService = getProcessEngine().getRuntimeService();
		// ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("LeaveProcessKey");
		Map<String, Object> variables = new HashMap<>();
		// variables.put("Assignee", "张三");
		variables.put("userName", "张三");
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("LeaveProcessKey", variables);

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
	 * <p>Title: 查询流程定义（部署、删除、读取流程资源），对应act_re_procdef表</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月26日
	 * 
	 */
	// @Test
	public void queryFlow1() {
		// 流程仓库Service，用于管理流程仓库，例如部署、删除、读取流程资源
		RepositoryService repositoryService = getProcessEngine().getRepositoryService();

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
	 * <p>Title: 查询流程定义实例，对应act_ru_execution表</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月26日
	 * 
	 */
	// @Test
	public void queryFlow2() {
		// 创建流程实例 - 运行时Service，可以也拿过来处理所有正在运行状态的流程实例、任务等
		// 执行管理，包括启动，推进，删除流程实例 等操作
		RuntimeService runtimeService = getProcessEngine().getRuntimeService();

		List<ProcessInstance> list = runtimeService.createProcessInstanceQuery()//
				.list();
		for (ProcessInstance processInstance : list) {
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
			System.out.println("流程变量：       " + processInstance.getProcessVariables());
			System.out.println("是否结束：       " + processInstance.isEnded());
			System.out.println("是否暂停：       " + processInstance.isSuspended());
			System.out.println("################################");
		}
	}

	/**
	 * <p>Title: 查询待办任务，对应act_ru_task表</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月26日
	 * 
	 */
	// @Test
	public void findMyProcessTask() {
		String assignee = "张三";
		List<Task> list = getProcessEngine().getTaskService()//
				.createTaskQuery()// 创建查询对象
				.taskAssignee(assignee)// 条件
				.list();

		for (Task task : list) {
			System.out.println("待办任务ID：" + task.getId());
			System.out.println("任务名称：" + task.getName());
			System.out.println("任务创建时间：" + task.getCreateTime());
			System.out.println("任务办理人：" + task.getAssignee());
			System.out.println("流程实例ID：" + task.getProcessInstanceId());
			System.out.println("执行对象ID：" + task.getExecutionId());
			System.out.println("流程定义ID" + task.getProcessDefinitionId());
			System.out.println("################################");
		}
	}

	/**
	 * <p>Title: 完成任务，将任务向下一步推送</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月26日
	 * 
	 */
	// @Test
	public void completeTask() {
		// act_ru_task表
		String taskId = "35005";

		// 没有变量的情况下
		// getProcessEngine().getTaskService().complete(taskId);// 完成任务，参数做为任务ID

		// 有变量的情况下
		Map<String, Object> variables = new HashMap<>();
		variables.put("userName", "李四");
		getProcessEngine().getTaskService().complete(taskId, variables);
		System.out.println("完成任务，任务ID：" + taskId);
	}

	/**
	 * <p>Title: 完成任务，将任务向下一步推送（不同意）</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月26日
	 * 
	 */
	// @Test
	public void completeTask2() {
		// act_ru_task表
		String taskId = "37502";

		// 没有变量的情况下
		// getProcessEngine().getTaskService().complete(taskId);// 完成任务，参数做为任务ID

		// 有变量的情况下
		Map<String, Object> variables = new HashMap<>();
		variables.put("pass", "No");
		getProcessEngine().getTaskService().complete(taskId, variables);
		System.out.println("完成任务，任务ID：" + taskId);
	}

	/**
	 * <p>Title: 结束流程实例，同时会关闭任务</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月26日
	 * 
	 */
	// @Test
	public void deleteProcessInstance() {
		RuntimeService runtimeService = getProcessEngine().getRuntimeService();

		// act_hi_procinst表的ID
		String processInstanceId = "30001";

		// 结束流程实例，同时会关闭任务
		runtimeService.deleteProcessInstance(processInstanceId, "");

		System.out.println("流程删除成功");
	}

	/**
	 * <p>Title: 历史活动查询，对应act_hi_actinst表</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月28日
	 * 
	 */
	// @Test
	public void historyActInstanceList() {
		// act_hi_procinst表的ID
		String processInstanceId = "35001";
		List<HistoricActivityInstance> list = getProcessEngine().getHistoryService() // 历史相关Service
				.createHistoricActivityInstanceQuery() // 创建历史活动实例查询
				.processInstanceId(processInstanceId) // 执行流程实例id
				.finished().list();
		for (HistoricActivityInstance hai : list) {
			System.out.println("活动ID:" + hai.getId());
			System.out.println("流程实例ID:" + hai.getProcessInstanceId());
			System.out.println("活动名称：" + hai.getActivityName());
			System.out.println("办理人：" + hai.getAssignee());
			System.out.println("开始时间：" + hai.getStartTime());
			System.out.println("结束时间：" + hai.getEndTime());
			System.out.println("################################");
		}
	}

	/**
	 * <p>Title: 历史任务查询，对应act_hi_actinst表</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月28日
	 * 
	 */
	// @Test
	public void historyTaskList() {
		// act_hi_procinst表ID
		String processInstanceId = "35001";
		List<HistoricTaskInstance> list = getProcessEngine().getHistoryService() // 历史相关Service
				.createHistoricTaskInstanceQuery() // 创建历史任务实例查询
				.processInstanceId(processInstanceId) // 用流程实例id查询
				.finished() // 查询已经完成的任务
				.list();
		for (HistoricTaskInstance hti : list) {
			System.out.println("任务ID:" + hti.getId());
			System.out.println("流程实例ID:" + hti.getProcessInstanceId());
			System.out.println("任务名称：" + hti.getName());
			System.out.println("办理人：" + hti.getAssignee());
			System.out.println("开始时间：" + hti.getStartTime());
			System.out.println("结束时间：" + hti.getEndTime());
			System.out.println("################################");
		}
	}

	/**
	 * <p>Title: 查询流程状态（正在执行 or 已经执行结束）</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月28日
	 * 
	 */
	// @Test
	public void processState() {
		// act_hi_procinst表ID
		String processInstanceId = "35001";
		ProcessInstance pi = getProcessEngine().getRuntimeService() // 获取运行时Service
				.createProcessInstanceQuery() // 创建流程实例查询
				.processInstanceId(processInstanceId) // 用流程实例id查询
				.singleResult();
		if (pi != null) {
			System.out.println("流程正在执行！");
		} else {
			System.out.println("流程已经执行结束！");
		}
	}

	/**
	 * <p>Title: 流程定义转换模型</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年4月2日
	 * 
	 * @throws UnsupportedEncodingException
	 * @throws XMLStreamException
	 */
	// @Test
	public void definitionToModel() throws UnsupportedEncodingException, XMLStreamException {
		String definitionId = "LeaveProcessKey:1:127504";

		RepositoryService repositoryService = getProcessEngine().getRepositoryService();

		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()//
				.processDefinitionId(definitionId)//
				.singleResult();
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

		InputStream bpmnStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),
				processDefinition.getResourceName());
		XMLInputFactory xif = XMLInputFactory.newInstance();
		InputStreamReader in = new InputStreamReader(bpmnStream, "UTF-8");
		XMLStreamReader xtr = xif.createXMLStreamReader(in);
		BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);

		BpmnJsonConverter converter = new BpmnJsonConverter();
		ObjectNode modelNode = converter.convertToJson(bpmnModel);

		Model model = repositoryService.newModel();

		ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();
		modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, processDefinition.getName());
		modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
		modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, processDefinition.getDescription());

		model.setKey(processDefinition.getKey());
		model.setName(processDefinition.getName());
		// model.setCategory(processDefinition.getDeploymentId());
		model.setMetaInfo(modelObjectNode.toString());
		model.setDeploymentId(processDefinition.getDeploymentId());

		repositoryService.saveModel(model);
		repositoryService.addModelEditorSource(model.getId(), modelNode.toString().getBytes("UTF-8"));
		System.out.println(model.getId());
	}
}

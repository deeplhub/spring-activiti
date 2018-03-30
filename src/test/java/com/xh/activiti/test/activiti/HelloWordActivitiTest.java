package com.xh.activiti.test.activiti;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * <p>Title: 熟悉Activiti</p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年3月26日
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/spring-config.xml" })
public class HelloWordActivitiTest {

	private static ProcessEngine processEngine = null;

	/**
	 * <p>Title: 使用框架提供的自动建表（使用配置文件） </p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月26日
	 * 
	 */
	// @Test
	public void createTable() {
		// 创建一个流程引擎配置对象
		processEngine = ProcessEngines.getDefaultProcessEngine();
	}

	/**
	 * <p>Title: 流程部署（执行流程）</p>
	 * <p>Description: 启动完成后在act_re_deployment流程部署表和act_re_procdef流程定义表中会有对应的数据信息.</p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月26日
	 * 
	 */
	// @Test
	public void deploymentFlow() {
		processEngine = ProcessEngines.getDefaultProcessEngine();
		// 管理流程定义 - 流程仓库Service，用于管理流程仓库，例如部署、删除、读取流程资源
		RepositoryService repositoryService = processEngine.getRepositoryService();

		// 方法一
		// DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
		// deploymentBuilder.name("HelleWord入门");
		// deploymentBuilder.addClasspathResource("HelloWord.bpmn");
		// Deployment deployment = deploymentBuilder.deploy();

		// 方法二
		Deployment deployment = processEngine.getRepositoryService()// 与流程定义和部署对象相关的Service
				.createDeployment()// 创建一个部署对象
				.name("HelleWord入门")// 添加部署名称
				.addClasspathResource("HelloWord.bpmn")// 从classpath的资源中加载，一次只能加载一个文件
				.deploy();// 完成部署

		System.out.println(deployment.getId());
		System.out.println(deployment.getName());
	}

	/**
	 * <p>Title: 启动流程</p>
	 * <p>Description:  </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月26日
	 * 
	 */
	// @Test
	public void startFlow() {
		processEngine = ProcessEngines.getDefaultProcessEngine();
		// 创建流程实例 - 运行时Service，可以也拿过来处理所有正在运行状态的流程实例、任务等
		// 执行管理，包括启动，推进，删除流程实例 等操作
		RuntimeService runtimeService = processEngine.getRuntimeService();
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("HelloWordKey");
		// 或
		// ProcessInstance processInstance = runtimeService.startProcessInstanceById("HelloWordKey:1:4");

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
		System.out.println("流程变量：       " + processInstance.getProcessVariables());
		System.out.println("是否结束：       " + processInstance.isEnded());
		System.out.println("是否暂停：       " + processInstance.isSuspended());
		System.out.println("################################");
	}

	/**
	 * <p>Title: 查询流程定义（部署、删除、读取流程资源）</p>
	 * <p>Description: act_re_procdef</p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月26日
	 * 
	 */
	// @Test
	public void queryFlow1() {
		processEngine = ProcessEngines.getDefaultProcessEngine();
		// 流程仓库Service，用于管理流程仓库，例如部署、删除、读取流程资源
		RepositoryService repositoryService = processEngine.getRepositoryService();

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
	 * <p>Title: 查询流程定义实例（只能读取）</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月26日
	 * 
	 */
	// @Test
	public void queryFlow2() {
		processEngine = ProcessEngines.getDefaultProcessEngine();
		// 创建流程实例 - 运行时Service，可以也拿过来处理所有正在运行状态的流程实例、任务等
		// 执行管理，包括启动，推进，删除流程实例 等操作
		RuntimeService runtimeService = processEngine.getRuntimeService();

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
	 * <p>Title: 查询待办任务</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月26日
	 * 
	 */
	// @Test
	public void findMyProcessTask() {
		String assignee = "张三";
		processEngine = ProcessEngines.getDefaultProcessEngine();
		List<Task> list = processEngine.getTaskService()//
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
			System.out.println();
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
		String taskId = "2504";
		processEngine = ProcessEngines.getDefaultProcessEngine();
		processEngine.getTaskService().complete(taskId);// 完成任务，参数做为任务ID
		System.out.println("完成任务，任务ID：" + taskId);
	}

	/**
	 * <p>Title: 删除流程定义</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月26日
	 * 
	 */
	// @Test
	public void deleteProcessDefinition() {
		processEngine = ProcessEngines.getDefaultProcessEngine();
		RepositoryService repositoryService = processEngine.getRepositoryService();

		// act_re_deployment表的ID
		String deploymentId = "1";

		// 删除流程定义，只删除没有启动过的流程，如果流程启动则抛出异常
		// repositoryService.deleteDeployment(deploymentId);
		// 删除流程定义，包括启动过的流程
		repositoryService.deleteDeployment(deploymentId, true);
		System.out.println("流程删除成功");
	}
}

package com.xh.activiti.service.activiti.impl;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xh.activiti.commons.utils.PageData;
import com.xh.activiti.commons.utils.StringUtils;
import com.xh.activiti.service.activiti.IActivitiDeploymentService;

/**
 * <p>Title: 流程部署</p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年4月1日
 */
@Service
public class ActivitiDeploymentServiceImpl implements IActivitiDeploymentService {

	@Autowired
	private RepositoryService repositoryService;

	/**
	 * <p>Title: 查询流程部署</p>
	 * <p>Description: 操作以下表：<br>
	 * 部署信息表ACT_RE_DEPLOYMENT</p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月30日
	 * 
	 * @return
	 */
	@Override
	public List<PageData> selectDeployList() {

		List<Deployment> list = repositoryService.createDeploymentQuery()//
				.orderByDeploymenTime()//
				.desc()//
				.list();

		LinkedList<PageData> linked = new LinkedList<>();
		PageData pd = null;
		for (Deployment deployment : list) {
			pd = new PageData();
			pd.put("id", deployment.getId());
			pd.put("name", deployment.getName());
			pd.put("deploymentTime", deployment.getDeploymentTime());
			linked.add(pd);
		}
		return linked;
	}

	/**
	 * <p>Title: 删除流程部署</p>
	 * <p>Description: 操作以下表：<br>
	 * 运行时流程执行实例表ACT_RU_EXECUTION<br>
	 * 历史流程实例表ACT_HI_PROCINST<br>
	 * 历史节点表ACT_HI_ACTINST<br>
	 * 运行时任务节点表ACT_RU_TASK<br>
	 * 历史任务实例表ACT_HI_TASKINST<br>
	 * 运行时流程人员表，主要存储任务节点与参与者的相关信息ACT_RU_IDENTITYLINK<br>
	 * 历史流程人员表ACT_HI_IDENTITYLINK<p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月30日
	 * 
	 * @param deploymentId
	 * @param cascade true:删除流程定义，包括启动过的流程;false:删除流程定义，只删除没有启动过的流程，如果流程启动则抛出异常
	 * @return
	 */
	@Override
	public boolean deleteDeployment(String deploymentId, boolean cascade) {
		try {
			repositoryService.deleteDeployment(deploymentId, cascade);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * <p>Title: 用流的方式对流程部署</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年4月1日
	 * 
	 * @param fileName
	 * @param inputStream
	 * @return
	 */
	@Override
	public boolean deployInputStream(String fileName, InputStream inputStream) {
		try {
			// 读取资源作为一个输入流
			Deployment deployment = repositoryService.createDeployment()// 创建部署对象
					.name(fileName)//
					.addInputStream(fileName, inputStream)//
					.deploy();// 完成部署

			return StringUtils.isNotBlank(deployment.getId()) ? true : false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}

package com.xh.activiti.service.activiti;

import java.util.List;

import com.xh.activiti.commons.utils.PageData;

/**
 * <p>Title: Activiti流程-通用接口</p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年3月30日
 */
public interface IActivitiProcessService {

	/**
	 * <p>Title: 查询流程部署</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月30日
	 * 
	 * @return
	 */
	List<PageData> selectDeployList();

	/**
	 * <p>Title: 查询流程定义</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月30日
	 * 
	 * @return
	 */
	List<PageData> selectDefinitionList();

	/**
	 * <p>Title: 启动流程</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月30日
	 * 
	 * @param processDefinitionId
	 * @param variables
	 * @return
	 */
	boolean startProcess(String processDefinitionId, PageData variables);

	/**
	 * <p>Title: 删除流程部署</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月30日
	 * 
	 * @param deploymentId
	 * @param cascade true:删除流程定义，包括启动过的流程;false:删除流程定义，只删除没有启动过的流程，如果流程启动则抛出异常
	 * @return
	 */
	boolean deleteDeployment(String deploymentId, boolean cascade);
}

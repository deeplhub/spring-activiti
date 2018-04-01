package com.xh.activiti.service.activiti;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.xh.activiti.commons.utils.PageData;

/**
 * <p>Title: 流程定义</p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年3月30日
 */
public interface IActivitiDefinitionService {

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
//	boolean startProcess(String processDefinitionId, PageData variables);

	/**
	 * <p>Title: 查看流程定义图</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月31日
	 * 
	 * @param deploymentId
	 * @param response
	 */
	void readDefinitionStream(String deploymentId, HttpServletResponse response);
}

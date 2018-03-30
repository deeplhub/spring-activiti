package com.xh.activiti.controller.activiti;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xh.activiti.commons.utils.PageData;
import com.xh.activiti.controller.BaseController;

/**
 * <p>Title: 流程定义</p>
 * <p>Description: 对以下表进行操作：act_re_procdef</p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年3月29日
 */
@Controller
@RequestMapping("/admin/definition")
public class ActivitiDefinitionController extends BaseController {

	@Autowired
	private RepositoryService repositoryService;

	/**
	 * <p>Title: 流程定义页面</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月28日
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@GetMapping("/manager")
	public String queryList(HttpServletRequest request, HttpServletResponse response) {

		return "admin/activiti/definition";
	}

	/**
	 * <p>Title: 流程定义列表</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月29日
	 * 
	 * @return
	 */
	@PostMapping("/definitionList")
	@ResponseBody
	public Object definitionDataGrid() {
		// 查询流程定义，可以排序，查询数量，分页等
		List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()//
				.list();
		LinkedList<PageData> linkedList = new LinkedList<>();
		PageData pd = null;
		for (ProcessDefinition processDefinition : list) {
			pd = new PageData();
			pd.put("id", processDefinition.getId());
			pd.put("name", processDefinition.getName());
			pd.put("key", processDefinition.getKey());
			pd.put("version", processDefinition.getVersion());
			pd.put("diagramResourceName", processDefinition.getDiagramResourceName());
			pd.put("resourceName", processDefinition.getResourceName());
			pd.put("deploymentId", processDefinition.getDeploymentId());
			pd.put("description", processDefinition.getDescription());
			linkedList.add(pd);
		}
		return linkedList;
	}

	/**
	 * <p>Title: 流程定义图</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月29日
	 * 
	 * @return
	 */
	@PostMapping("/definitionDiagram")
	@ResponseBody
	public Object definitionDiagram(String paramId) {

		return "";
	}
}

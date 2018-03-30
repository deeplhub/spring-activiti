package com.xh.activiti.controller.activiti;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xh.activiti.commons.utils.Assert;
import com.xh.activiti.commons.utils.PageData;
import com.xh.activiti.controller.BaseController;

/**
 * <p>Title: 流程部署</p>
 * <p>Description: 对以下表进行操作：act_re_deployment,act_re_procdef</p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年3月29日
 */
@Controller
@RequestMapping("/admin/deploy")
public class ActivitiDeployController extends BaseController {

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private RuntimeService runtimeService;

	/**
	 * <p>Title: 流程部署页面</p>
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

		return "admin/activiti/deploy";
	}

	/**
	 * <p>Title: 流程部署列表</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月29日
	 * 
	 * @return
	 */
	@PostMapping("/deployList")
	@ResponseBody
	public Object deployDataGrid() {
		List<Deployment> list = repositoryService.createDeploymentQuery()//
				.list();
		LinkedList<PageData> linkedList = new LinkedList<>();
		PageData pd = null;
		for (Deployment deployment : list) {
			pd = new PageData();
			pd.put("id", deployment.getId());
			pd.put("name", deployment.getName());
			pd.put("deploymentTime", deployment.getDeploymentTime());
			linkedList.add(pd);
		}
		return linkedList;
	}

	/**
	 * <p>Title: 删除流程部署</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月28日
	 * 
	 * @param modelId
	 * @return
	 */
	@PostMapping("/remove")
	@ResponseBody
	public Object remove(String paramId) {
		Assert.isBlank(paramId, "ID不能为空");
		try {
			repositoryService.deleteDeployment(paramId);
			return renderSuccess("删除成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return renderError("删除失败！");
		}
	}

	/**
	 * <p>Title: 流程部署</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月29日
	 * 
	 * @param paramId
	 * @return
	 */
	@PostMapping("/deploy")
	@ResponseBody
	public Object deployInputStream(String paramId) {

		return "";
	}
}

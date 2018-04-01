package com.xh.activiti.controller.activiti;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xh.activiti.commons.utils.Assert;
import com.xh.activiti.commons.utils.PageData;
import com.xh.activiti.controller.BaseController;
import com.xh.activiti.service.activiti.IActivitiDefinitionService;

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
	private IActivitiDefinitionService definitionService;

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

		// 查询流程定义
		List<PageData> list = definitionService.selectDefinitionList();
		return list;
	}

	/**
	 * <p>Title: 查看流程定义图</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月29日
	 * 
	 * @param deploymentId 流程定义ID
	 * @param response
	 */
	@GetMapping("/readDefinitionDiagram")
	public void readDefinitionDiagramStream(String deploymentId, HttpServletResponse response) {
		Assert.isBlank(deploymentId, "流程部署ID不能为空");
		definitionService.readDefinitionStream(deploymentId, response);
	}
}

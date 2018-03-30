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
import com.xh.activiti.commons.utils.StringUtils;
import com.xh.activiti.controller.BaseController;
import com.xh.activiti.service.activiti.IActivitiModelService;

/**
 * <p>Title: 流程模型</p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年3月26日
 */
@Controller
@RequestMapping("/admin/model")
public class ActivitiModelController extends BaseController {

	@Autowired
	private IActivitiModelService modelService;

	/**
	 * <p>Title: 流程模型页面</p>
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

		return "admin/activiti/model";
	}

	/**
	 * <p>Title: 流程模型列表</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月28日
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@PostMapping("/modelList")
	@ResponseBody
	public Object modelDataGrid() {

		List<PageData> list = modelService.selectModelMapList();
		return list;
	}

	/**
	 * <p>Title: 创建流程模型</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月28日
	 * 
	 * @param name
	 * @param key
	 * @param description
	 * @return
	 */
	@RequestMapping("/add")
	@ResponseBody
	public Object addModel(String name, String key, String description) {
		Assert.isBlank(name, "名称不能为空");
		Assert.isBlank(key, "KEY不能为空");
		Assert.isBlank(description, "描述不能为空");

		String modelId = modelService.insertModel(name, key, description);
		if (StringUtils.isNotBlank(modelId)) {
			Object obj = modelId;
			return renderSuccess(obj);
		}
		return renderError("创建模型失败");
	}

	/**
	 * <p>Title: 打开流程模型视图</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月29日
	 * 
	 * @param modelId
	 * @return
	 */
	@RequestMapping("/openModelView")
	public String openModelView(String modelId) {
		Assert.isBlank(modelId, "模型ID不能为空");
		// 重定向方式
		return "redirect:../../plugins/activiti/modeler.html?modelId=" + modelId;
	}

	/**
	 * <p>Title: 删除流程模型</p>
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

		if (modelService.deleteModel(paramId)) {
			return renderSuccess("删除成功！");
		}
		return renderError("删除失败！");
	}

	/**
	 * <p>Title: 模型部署</p>
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
	public Object deployModel(String paramId) {
		Assert.isBlank(paramId, "模型ID不能为空");
		if (modelService.deployModel(paramId)) {
			return renderSuccess("部署成功！");
		}
		return renderError("部署失败！");
	}

}

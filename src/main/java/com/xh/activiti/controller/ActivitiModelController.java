package com.xh.activiti.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xh.activiti.commons.utils.Assert;

/**
 * <p>Title: </p>
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
	private RepositoryService repositoryService;

	/**
	 * <p>Title: 流程模列表</p>
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
	 * <p>Title: 模型列表</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月28日
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@PostMapping("/queryModelDataGrid")
	@ResponseBody
	public Object queryModelDataGrid() {
		List<Model> list = repositoryService.createModelQuery()//
				.orderByCreateTime()//
				.desc()//
				.list();

		return list;
	}

	/**
	 * <p>Title: 创建模型</p>
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
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			ObjectNode editorNode = objectMapper.createObjectNode();
			editorNode.put("id", "canvas");
			editorNode.put("resourceId", "canvas");

			ObjectNode stencilSetNode = objectMapper.createObjectNode();
			stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
			editorNode.put("stencilset", stencilSetNode);

			Model model = repositoryService.newModel();

			ObjectNode modelObjectNode = objectMapper.createObjectNode();
			modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, name);
			modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
			modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);

			model.setMetaInfo(modelObjectNode.toString());
			model.setName(name);
			model.setKey(key);

			// 存入ACT_RE_MODEL
			repositoryService.saveModel(model);
			// 存入ACT_GE_BYTEARRAY
			repositoryService.addModelEditorSource(model.getId(), editorNode.toString().getBytes("utf-8"));

			Object obj = model.getId();
			return renderSuccess(obj);
		} catch (Exception e) {
			e.printStackTrace();
			return renderError("创建模型失败");
		}
	}

	/**
	 * <p>Title: 打开模型视图</p>
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
	 * <p>Title: 删除模型</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月28日
	 * 
	 * @param modelId
	 * @return
	 */
	@PostMapping("/remove")
	public Object remove(String modelId) {
		Assert.isBlank(modelId, "ID不能为空");
		try {
			repositoryService.deleteModel(modelId);
			return renderSuccess("添加成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return renderError("添加失败！");
		}
	}

}

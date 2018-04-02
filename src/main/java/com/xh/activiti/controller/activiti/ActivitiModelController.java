package com.xh.activiti.controller.activiti;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.repository.Model;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
	public String queryList() {

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
	@PostMapping("/add")
	@ResponseBody
	public Object addModel(String name, String key, String description) {
		Assert.isBlank(name, "名称不能为空");
		Assert.isBlank(key, "KEY不能为空");
		Assert.isBlank(description, "描述不能为空");

		String modelId = modelService.insertModel(name, key, description);
		if (StringUtils.isNotBlank(modelId)) {
			Object obj = modelId;
			return renderSuccess("创建模型成功", obj);
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
	@GetMapping("/open/{paramId}/view")
	public String openModelView(@PathVariable("paramId") String paramId) {
		Assert.isBlank(paramId, "模型ID不能为空");
		// 重定向方式
		return "redirect:/plugins/activiti/modeler.html?modelId=" + paramId;
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

	/**
	 * <p>Title: 导出文件</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年4月2日
	 * 
	 * @param paramId
	 * @param type 导出文件类型(bpmn\json\xml)
	 * @param response
	 */
	@GetMapping("/export/{paramId}/{type}")
	public void export(@PathVariable("paramId") String paramId, @PathVariable("type") String type, HttpServletResponse response) {
		Assert.isBlank(paramId, "模型ID不能为空");
		try {
			Model model = modelService.selectByModelId(paramId);
			byte[] modelEditorSource = modelService.getModelEditorSource(model.getId());

			JsonNode jsonNode = new ObjectMapper().readTree(modelEditorSource);
			BpmnModel bpmnModel = new BpmnJsonConverter().convertToBpmnModel(jsonNode);

			// 此代码注释说明：获取流程定义中的名称，如果只创建模型，没对创建的模型制图或设置流程定义操作会出现以下异常
			// 处理异常
			// if (bpmnModel.getMainProcess() == null) {
			// String msg = "<h2>未知的流程模型</h2>";
			// response.setContentType("text/html;charset=UTF-8");
			// response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
			// response.getWriter().print(msg); // 换成这个就好了
			// response.getWriter().close();
			// return;
			// }

			// 这是获取流程定义中的名称
			// String fileName = bpmnModel.getMainProcess().getName();

			byte[] exportBytes = null;
			String fileName = model.getName();
			if (type.equals("bpmn")) {
				exportBytes = new BpmnXMLConverter().convertToXML(bpmnModel);
				// 生成activiti文件
				fileName += ".bpmn";
			} else if (type.equals("xml")) {
				exportBytes = new BpmnXMLConverter().convertToXML(bpmnModel);
				// 生成xml文件
				fileName += ".bpmn20.xml";
			} else {
				exportBytes = modelEditorSource;
				fileName += ".json";
			}

			ByteArrayInputStream in = new ByteArrayInputStream(exportBytes);
			IOUtils.copy(in, response.getOutputStream());

			response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
			response.flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

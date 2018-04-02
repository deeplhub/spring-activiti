package com.xh.activiti.service.activiti.impl;

import java.util.LinkedList;
import java.util.List;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xh.activiti.commons.exception.ResultException;
import com.xh.activiti.commons.utils.PageData;
import com.xh.activiti.commons.utils.StringUtils;
import com.xh.activiti.service.activiti.IActivitiDeploymentService;
import com.xh.activiti.service.activiti.IActivitiModelService;

/**
 * <p>Title: 流程模型</p>
 * <p>Description: 页面创建流程模型(操作：act_re_model)和模型部署(操作：act_re_deployment,act_re_procdef)</p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年3月30日
 */
@Service
public class ActivitiModelServiceImpl implements IActivitiModelService {

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private IActivitiDeploymentService deploymentService;

	/**
	 * <p>Title: 流程模型列表</p>
	 * <p>Description: 操作以下表：<br>
	 * 流程设计模型部署表ACT_RE_MODEL</p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月30日
	 * 
	 * @return
	 */
	@Override
	public List<Model> selectModelList() {
		List<Model> list = repositoryService.createModelQuery()//
				.orderByCreateTime()//
				.desc()//
				.list();
		return list;
	}

	/**
	 * <p>Title: 流程模型列表</p>
	 * <p>Description: 操作以下表：<br>
	 * 流程设计模型部署表ACT_RE_MODEL</p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月30日
	 * 
	 * @return
	 */
	@Override
	public List<PageData> selectModelMapList() {
		List<Model> list = this.selectModelList();
		// 查询流程部署
		List<PageData> listPd = deploymentService.selectDeployList();

		LinkedList<PageData> linked = new LinkedList<>();
		PageData pd = null;
		for (Model model : list) {
			pd = new PageData();
			pd.put("id", model.getId());
			pd.put("name", model.getName());
			pd.put("key", model.getKey());
			pd.put("version", model.getVersion());
			pd.put("createTime", model.getCreateTime());
			pd.put("lastUpdateTime", model.getLastUpdateTime());
			pd.put("deployStatus", "未部署");
			pd.put("deploymentTime", null);

			for (PageData data : listPd) {
				if (model.getDeploymentId() != null && model.getDeploymentId().equals(data.getString("id"))) {
					pd.put("deployStatus", "已部署");
					pd.put("deploymentTime", data.get("deploymentTime"));
					break;
				}
			}
			linked.add(pd);
		}
		return linked;
	}

	/**
	 * <p>Title: 根据模型ID查询模型</p>
	 * <p>Description: 操作以下表：<br>
	 * 流程设计模型部署表ACT_RE_MODEL</p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月30日
	 * 
	 * @param modelId
	 * @return
	 */
	@Override
	public Model selectByModelId(String modelId) {

		return repositoryService.getModel(modelId);
	}

	/**
	 * <p>Title: 添加模型</p>
	 * <p>Description: 操作以下表：<br>
	 * 部署信息表ACT_RE_DEPLOYMENT<br>
	 * 二进制数据表ACT_GE_BYTEARRA<br>
	 * 
	 * @author H.Yang
	 * @date 2018年3月30日
	 * 
	 * @param name
	 * @param key
	 * @param description
	 * @return 返回模型ID
	 */
	@Override
	public String insertModel(String name, String key, String description) {
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
		modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);// 型号修订
		modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);

		model.setMetaInfo(modelObjectNode.toString());
		model.setName(name);
		model.setKey(key);

		try {
			// 存入ACT_RE_MODEL
			repositoryService.saveModel(model);
			// 存入ACT_GE_BYTEARRAY
			repositoryService.addModelEditorSource(model.getId(), editorNode.toString().getBytes("utf-8"));

			return model.getId();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * <p>Title: 根据模型ID删除模型</p>
	 * <p>Description: 操作以下表：<br>
	 * 流程设计模型部署表ACT_RE_MODEL</p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月30日
	 * 
	 * @param modelId
	 * @return
	 */
	@Override
	public boolean deleteModel(String modelId) {
		try {
			repositoryService.deleteModel(modelId);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * <p>Title: 部署模型</p>
	 * <p>Description: <h3>部署模型时操作以下表：</h3>
	 * 部署信息表ACT_RE_DEPLOYMENT<br>
	 * 二进制数据表ACT_GE_BYTEARRA<br>
	 * 流程定义数据表ACT_RE_PROCDEF<br><br>
	 * 
	 * @author H.Yang
	 * @date 2018年3月30日
	 * 
	 * @param modelId
	 * @return
	 */
	@Override
	public boolean deployModel(String modelId) {
		Model modelData = this.selectByModelId(modelId);
		if (StringUtils.isNotBlank(modelData.getDeploymentId()))
			throw new ResultException("模型已部署");

		try {
			ObjectNode modelNode = (ObjectNode) new ObjectMapper()//
					.readTree(this.getModelEditorSource(modelData.getId()));
			BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
			byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(model);
			String processName = modelData.getName() + ".bpmn20.xml";

			Deployment deployment = repositoryService.createDeployment()//
					.name(modelData.getName())//
					.addString(processName, new String(bpmnBytes, "UTF-8"))//
					.deploy();

			// 更新部署信息ID
			modelData.setDeploymentId(deployment.getId());
			repositoryService.saveModel(modelData);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * <p>Title: 将模型编辑器作为字节数组返回</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年4月2日
	 * 
	 * @param modelId
	 * @return
	 */
	@Override
	public byte[] getModelEditorSource(String modelId) {

		return repositoryService.getModelEditorSource(modelId);
	}
}

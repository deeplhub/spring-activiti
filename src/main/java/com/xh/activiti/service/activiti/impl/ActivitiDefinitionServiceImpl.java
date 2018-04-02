package com.xh.activiti.service.activiti.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xh.activiti.commons.exception.ResultException;
import com.xh.activiti.commons.utils.PageData;
import com.xh.activiti.service.activiti.IActivitiDefinitionService;

/**
 * <p>Title: 流程定义</p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年3月30日
 */
@Service
public class ActivitiDefinitionServiceImpl implements IActivitiDefinitionService {

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private RuntimeService runtimeService;

	/**
	 * <p>Title: 查询流程定义</p>
	 * <p>Description: 操作以下表：<br>
	 * 流程定义数据表ACT_RE_PROCDEF</p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月30日
	 * 
	 * @return
	 */
	@Override
	public List<PageData> selectDefinitionList() {
		List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()//
				.orderByDeploymentId()//
				.desc()//
				.list();

		LinkedList<PageData> linked = new LinkedList<>();
		PageData pd = null;
		for (ProcessDefinition processDefinition : list) {
			pd = new PageData();
			pd.put("id", processDefinition.getId());// 流程定义ID
			pd.put("name", processDefinition.getName());// 流程设计名称
			pd.put("key", processDefinition.getKey());// 流程定义key
			pd.put("version", processDefinition.getVersion());// 流程版本
			pd.put("diagramResourceName", processDefinition.getDiagramResourceName());// 流程图片文件
			pd.put("resourceName", processDefinition.getResourceName());// 流程定义文件
			pd.put("deploymentId", processDefinition.getDeploymentId());// 流程部署ID
			pd.put("description", processDefinition.getDescription());// 流程描述
			pd.put("category", processDefinition.getCategory());// 流程组织机构
			pd.put("tenantId", processDefinition.getTenantId());// 流程所有人ID
			linked.add(pd);
		}
		return linked;
	}

	/**
	 * <p>Title: 启动流程</p>
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
	 * @param processDefinitionId
	 * @param variables
	 * @return
	 */
	// @Override
	// public boolean startProcess(String processDefinitionId, PageData variables) {
	//
	// ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinitionId, variables);
	//
	// // 流程实例中包含的信息
	// System.out.println("当前活动节点 " + processInstance.getActivityId());
	// System.out.println("关联业务键： " + processInstance.getBusinessKey());
	// System.out.println("流程部署id： " + processInstance.getDeploymentId());
	// System.out.println("流程描述： " + processInstance.getDescription());
	// System.out.println("流程实例id： " + processInstance.getId());
	// System.out.println("流程实例名称： " + processInstance.getName());
	// System.out.println("父流程id： " + processInstance.getParentId());
	// System.out.println("流程定义id： " + processInstance.getProcessDefinitionId());
	// System.out.println("流程定义key： " + processInstance.getProcessDefinitionKey());
	// System.out.println("流程定义名称： " + processInstance.getProcessDefinitionName());
	// System.out.println("流程实例id： " + processInstance.getProcessInstanceId());
	// System.out.println("流程所属人id： " + processInstance.getTenantId());
	// System.out.println("流程定义版本： " + processInstance.getProcessDefinitionVersion());
	// System.out.println("流程变量： " + processInstance.getProcessVariables().get("userName"));
	// System.out.println("是否结束： " + processInstance.isEnded());
	// System.out.println("是否暂停： " + processInstance.isSuspended());
	// System.out.println("################################");
	// return StringUtils.isNotBlank(processInstance.getId()) ? true : false;
	// }

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
	@Override
	public void readDefinitionStream(String deploymentId, HttpServletResponse response) {
		InputStream in = null;
		OutputStream out = null;
		try {
			List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()//
					.deploymentId(deploymentId)//
					.list();
			ProcessDefinition processDefinition = list.get(0);
			// 通过部署ID和文件名称得到文件的输入流
			in = repositoryService.getResourceAsStream(deploymentId, processDefinition.getDiagramResourceName());
			out = response.getOutputStream();
			// 把图片的输入流程写入response的输出流中
			byte[] bytes = new byte[1024];
			int len;
			while ((len = in.read(bytes, 0, 1024)) != -1) {
				out.write(bytes, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭流
			try {
				if (out != null)
					out.close();
				if (in != null)
					in.close();
			} catch (IOException e) {
				e.printStackTrace();

			}
		}

	}

	/**
	 * <p>Title: 流程定义转换成流程模型</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年4月2日
	 * 
	 * @param definitionId
	 * @return
	 */
	@Override
	public boolean definitionToModel(String definitionId) {
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()//
				.processDefinitionId(definitionId)//
				.singleResult();

		long count = repositoryService.createModelQuery()//
				.deploymentId(processDefinition.getDeploymentId())//
				.count();
		if (count > 0)
			throw new ResultException("模型已生成，不能重复生成");

		try {
			InputStream bpmnStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),
					processDefinition.getResourceName());
			XMLInputFactory xif = XMLInputFactory.newInstance();
			InputStreamReader in = new InputStreamReader(bpmnStream, "UTF-8");
			XMLStreamReader xtr = xif.createXMLStreamReader(in);
			BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);

			BpmnJsonConverter converter = new BpmnJsonConverter();
			ObjectNode modelNode = converter.convertToJson(bpmnModel);

			Model model = repositoryService.newModel();

			ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();
			modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, processDefinition.getName());
			modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
			modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, processDefinition.getDescription());

			model.setKey(processDefinition.getKey());
			model.setName(processDefinition.getName());
			// model.setCategory(processDefinition.getDeploymentId());
			model.setMetaInfo(modelObjectNode.toString());
			model.setDeploymentId(processDefinition.getDeploymentId());

			repositoryService.saveModel(model);
			repositoryService.addModelEditorSource(model.getId(), modelNode.toString().getBytes("UTF-8"));

			return true;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}

		return false;
	}
}

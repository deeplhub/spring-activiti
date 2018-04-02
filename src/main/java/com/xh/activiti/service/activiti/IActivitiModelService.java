package com.xh.activiti.service.activiti;

import java.util.List;

import org.activiti.engine.repository.Model;

import com.xh.activiti.commons.utils.PageData;

/**
 * <p>Title: 模型</p>
 * <p>Description: 页面创建流程模型(操作：act_re_model)和模型部署(操作：act_re_deployment,act_re_procdef)</p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年3月30日
 */
public interface IActivitiModelService {

	/**
	 * <p>Title: 流程模型列表</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月30日
	 * 
	 * @return
	 */
	List<Model> selectModelList();

	/**
	 * <p>Title: 流程模型列表</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月30日
	 * 
	 * @return
	 */
	List<PageData> selectModelMapList();

	/**
	 * <p>Title: 根据模型ID查询模型</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月30日
	 * 
	 * @param modelId
	 * @return
	 */
	Model selectByModelId(String modelId);

	/**
	 * <p>Title: 添加模型</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月30日
	 * 
	 * @param name
	 * @param key
	 * @param description
	 * @return 返回模型ID
	 */
	String insertModel(String name, String key, String description);

	/**
	 * <p>Title: 根据模型ID删除模型</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月30日
	 * 
	 * @param modelId
	 * @return
	 */
	boolean deleteModel(String modelId);

	/**
	 * <p>Title: 模型部署</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月30日
	 * 
	 * @param paramId
	 * @return
	 */
	boolean deployModel(String paramId);

	/**
	 * <p>Title: 将模型编辑器源作为字节数组返回</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年4月2日
	 * 
	 * @param modelId
	 * @return
	 */
	byte[] getModelEditorSource(String modelId);
}

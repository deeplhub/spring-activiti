package com.xh.activiti.dao;

import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xh.activiti.model.Resource;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年3月14日
 */
public interface IResourceDao extends BaseMapper<Resource> {

	/**
	 * <p>Title: 调用存储过程-根据ID查询所有子级</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月17日
	 * 
	 * @param paramId
	 * @return
	 */
	void getResourceChildIds(Map<String, Object> param);
}

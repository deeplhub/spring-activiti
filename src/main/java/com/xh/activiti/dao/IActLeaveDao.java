package com.xh.activiti.dao;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xh.activiti.model.ActLeave;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年3月28日
 */
public interface IActLeaveDao extends BaseMapper<ActLeave> {

	/**
	 * <p>Title: 根据流程定义ID查询请假列表</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年4月6日
	 * 
	 * @param processInstanceIds
	 * @return
	 */
	List<ActLeave> selectListByProcessInstanceId(List<String> list);

}

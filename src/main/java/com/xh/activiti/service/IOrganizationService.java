package com.xh.activiti.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.xh.activiti.commons.result.Tree;
import com.xh.activiti.model.Organization;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年3月14日
 */
public interface IOrganizationService extends IService<Organization> {

	/**
	 * <p>Title: 树型菜单</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月22日
	 * 
	 * @return
	 */
	List<Tree> selectTree();

	/**
	 * <p>Title: 分页列表</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月22日
	 * 
	 * @return
	 */
	List<Organization> selectTreeGrid();

}

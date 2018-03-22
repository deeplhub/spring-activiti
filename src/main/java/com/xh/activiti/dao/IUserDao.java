package com.xh.activiti.dao;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.xh.activiti.model.User;

/**
 * <p>Title: 用户表</p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年3月14日
 */
public interface IUserDao extends BaseMapper<User> {

	User selectUserById(Long id);

	List<User> selectUserPage(Pagination page);

	/**
	 * <p>Title: 根据部门ID查询用户条数</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月22日
	 * 
	 * @param list
	 * @return
	 */
	int selectCount(List<String> list);
}

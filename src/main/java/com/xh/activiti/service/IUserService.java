package com.xh.activiti.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.xh.activiti.commons.result.PageInfo;
import com.xh.activiti.model.User;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年3月14日
 */
public interface IUserService extends IService<User> {

	/**
	 * <p>Title: 根据ID查询User</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月14日
	 * 
	 * @param id
	 * @return
	 */
	User selectUserById(Long id);

	/**
	 * <p>Title: 登陆</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月14日
	 * 
	 * @param user
	 * @return
	 */
	List<User> selectByLoginName(User user);

	/**
	 * <p>Title: 分页查询 - 查询用户列表</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月22日
	 * 
	 * @param pageInfo
	 * @param user
	 */
	void selectUserPage(PageInfo pageInfo, User user);

	/**
	 * <p>Title: 修改密码</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月14日
	 * 
	 * @param userId
	 * @param md5Hex
	 * @return
	 */
	boolean updatePwdByUserId(Long userId, String md5Hex);
}

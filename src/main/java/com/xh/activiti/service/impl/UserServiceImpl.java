package com.xh.activiti.service.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xh.activiti.commons.result.PageInfo;
import com.xh.activiti.dao.IUserDao;
import com.xh.activiti.dao.IUserRoleDao;
import com.xh.activiti.model.User;
import com.xh.activiti.service.IUserService;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年3月14日
 */
@Service
public class UserServiceImpl extends ServiceImpl<IUserDao, User> implements IUserService {

	@Autowired
	private IUserDao userDao;

	@Autowired
	private IUserRoleDao userRoleDao;

	@Override
	public User selectUserById(Long id) {
		return userDao.selectUserById(id);
	}

	@Override
	public List<User> selectByLoginName(User user) {
		EntityWrapper<User> wrapper = new EntityWrapper<User>(user);
		if (user.getId() != null) {
			wrapper.where("id != {0}", user.getId());
		}
		List<User> list = this.selectList(wrapper);
		return list;
	}

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
	@Override
	public void selectUserPage(PageInfo pageInfo, User user) {
		Page<User> page = new Page<User>(pageInfo.getNowpage(), pageInfo.getSize());
		Map<String, Object> condition = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(user.getName())) {
			condition.put("name", user.getName());
		}
		if (user.getOrganizationId() != null) {
			condition.put("organizationId", user.getOrganizationId());
		}
		pageInfo.setCondition(condition);
		page.setRecords(userDao.selectUserPage(page));

		pageInfo.setRows(page.getRecords());
		pageInfo.setTotal(page.getTotal());
	}

	/**
	 * <p>Title: 修改密码</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月23日
	 * 
	 * @param userId
	 * @param md5Hex
	 * @return
	 */
	@Override
	public boolean updatePwdByUserId(Long userId, String md5Hex) {
		User user = new User();
		user.setId(userId);
		user.setPassword(md5Hex);
		return this.updateById(user);
	}

	/**
	 * 重写父类方法
	 */
	@Override
	public boolean deleteById(Serializable id) {
		Long uid = Long.valueOf(id.toString());
		userRoleDao.deleteByUserId(uid);
		return super.deleteById(id);
	}
}

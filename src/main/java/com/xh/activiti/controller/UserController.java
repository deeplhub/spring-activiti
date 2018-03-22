package com.xh.activiti.controller;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xh.activiti.commons.result.PageInfo;
import com.xh.activiti.commons.shiro.PasswordHash;
import com.xh.activiti.commons.utils.Assert;
import com.xh.activiti.commons.utils.StringUtils;
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
@Controller
@RequestMapping("/admin/user")
public class UserController extends BaseController {

	@Autowired
	private IUserService userService;
	@Autowired
	private PasswordHash passwordHash;

	/**
	 * 用户管理页
	 *
	 * @return
	 */
	@GetMapping("/manager")
	public String manager() {
		return "admin/user/user";
	}

	/**
	 * <p>Title: 分页查询 - 查询用户列表</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月22日
	 * 
	 * @param user
	 * @param page
	 * @param rows
	 * @param sort
	 * @param order
	 * @return
	 */
	@PostMapping("/dataGrid")
	@ResponseBody
	public Object dataGrid(User user, Integer page, Integer rows, String sort, String order) {
		PageInfo pageInfo = new PageInfo(page, rows, sort, order);
		userService.selectUserPage(pageInfo, user);
		return pageInfo;
	}

	/**
	 * 添加用户
	 *
	 * @param user
	 * @return
	 */
	@PostMapping("/add")
	@ResponseBody
	public Object add(User user) {
		Assert.isNull(user, "参数不能为空");
		List<User> list = userService.selectByLoginName(user);
		if (list != null && !list.isEmpty()) {
			return renderError("登录名已存在!");
		}
		String salt = StringUtils.getUUId();
		String pwd = passwordHash.toHex(user.getPassword(), salt);
		user.setSalt(salt);
		user.setPassword(pwd);
		if (userService.insert(user)) {
			return renderSuccess("添加成功");
		}
		return renderError("添加失败");
	}

	/**
	 * 编辑用户
	 *
	 * @param user
	 * @return
	 */
	@RequiresRoles("admin")
	@PostMapping("/edit")
	@ResponseBody
	public Object edit(User user) {
		Assert.isNull(user, "参数不能为空");
		List<User> list = userService.selectByLoginName(user);
		if (list != null && !list.isEmpty()) {
			return renderError("登录名已存在!");
		}
		// 更新密码
		if (StringUtils.isNotBlank(user.getPassword())) {
			user = userService.selectById(user.getId());
			String salt = user.getSalt();
			String pwd = passwordHash.toHex(user.getPassword(), salt);
			user.setPassword(pwd);
		}
		if (userService.updateById(user)) {
			return renderSuccess("修改成功");
		}
		return renderError("修改失败");
	}

	/**
	 * 修改密码
	 *
	 * @param oldPwd
	 * @param pwd
	 * @return
	 */
	@PostMapping("/editUserPwd")
	@ResponseBody
	public Object editUserPwd(String oldPwd, String pwd) {
		Assert.isBlank(oldPwd, "旧密码不能为空");
		Assert.isBlank(pwd, "新密码不能为空");
		User user = userService.selectById(getUserId());
		String salt = user.getSalt();
		if (!user.getPassword().equals(passwordHash.toHex(oldPwd, salt))) {
			return renderError("旧密码不正确!");
		}
		if (userService.updatePwdByUserId(getUserId(), passwordHash.toHex(pwd, salt))) {
			return renderSuccess("密码修改成功！");
		}
		return renderError("密码修改失败！");
	}

	/**
	 * 删除用户
	 *
	 * @param id
	 * @return
	 */
	@RequiresRoles("admin")
	@PostMapping("/delete")
	@ResponseBody
	public Object delete(Long paramId) {
		Assert.isNull(paramId, "主键不能为空");
		Long currentUserId = getUserId();
		if (paramId == currentUserId) {
			return renderError("不可以删除自己！");
		}
		if (userService.deleteById(paramId)) {
			return renderSuccess("删除成功！");
		}
		return renderError("删除失败！");
	}
}

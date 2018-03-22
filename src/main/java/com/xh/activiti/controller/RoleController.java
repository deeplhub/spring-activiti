package com.xh.activiti.controller;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xh.activiti.commons.result.PageInfo;
import com.xh.activiti.commons.utils.Assert;
import com.xh.activiti.model.Role;
import com.xh.activiti.service.IRoleService;

/**
 * <p>Title: 权限管理</p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @date 2018年3月18日
 * 
 */
@Controller
@RequestMapping("/admin/role")
public class RoleController extends BaseController {

	@Autowired
	private IRoleService roleService;

	/**
	 * <p>Title: 权限管理页</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月19日
	 * 
	 * @return
	 */
	@GetMapping("/manager")
	public String manager() {
		return "admin/role/role";
	}

	/**
	 * <p>Title: 角色列表</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月19日
	 * 
	 * @param page
	 * @param rows
	 * @param sort
	 * @param order
	 * @return
	 */
	@PostMapping("/dataGrid")
	@ResponseBody
	public Object dataGrid(Integer page, Integer rows, String sort, String order) {
		PageInfo pageInfo = new PageInfo(page, rows, sort, order);
		roleService.selectDataGrid(pageInfo);
		return pageInfo;
	}

	/**
	 * <p>Title: 添加角色</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月19日
	 * 
	 * @param role
	 * @return
	 */
	@PostMapping("/add")
	@ResponseBody
	public Object add(Role role) {
		if (roleService.insert(role)) {
			return renderSuccess();
		}
		return renderError("添加失败");
	}

	/**
	 * <p>Title: 删除角色</p>
	 * <p>Description: 删除角色，并删除用户与角色中间表，删除资源管理与角色中间表</p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月19日
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/remove")
	@ResponseBody
	public Object remove(Long paramId) {
		Assert.isNull(paramId, "主键不能为空");
		if (roleService.deleteBatchIds(paramId)) {
			return renderSuccess("删除成功！");
		}
		return renderSuccess("删除失败！");
	}

	/**
	 * <p>Title: 修改角色</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月19日
	 * 
	 * @param role
	 * @return
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public Object edit(Role role) {
		if (roleService.updateById(role)) {
			return renderSuccess();
		}
		return renderSuccess("编辑失败！");
	}

	/**
	 * 授权页面
	 *
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/grantPage")
	public String grantPage(Model model, Long id) {
		model.addAttribute("id", id);
		return "admin/role/roleGrant";
	}

	/**
	 * 授权
	 *
	 * @param id
	 * @param resourceIds
	 * @return
	 */
	@RequiresRoles("admin")
	@RequestMapping("/updateAuthorized")
	@ResponseBody
	public Object updateAuthorized(Long paramId, String resourceIds) {
		Assert.isNull(paramId, "主键不能为空");
		Assert.isNull(resourceIds, "资源不能为空");
		if (roleService.updateAuthorized(paramId, resourceIds)) {
			return renderSuccess();
		}
		return renderError("授权失败！");
	}

}

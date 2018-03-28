package com.xh.activiti.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xh.activiti.commons.shiro.ShiroUser;
import com.xh.activiti.commons.utils.Assert;
import com.xh.activiti.model.Resource;
import com.xh.activiti.service.IResourceService;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年3月14日
 */
@Controller
@RequestMapping("/admin/resource")
public class ResourceController extends BaseController {

	@Autowired
	private IResourceService resourceService;

	/**
	 * <p>Title: 菜单树</p>
	 * <p>Description: 主页树形菜单</p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月17日
	 * 
	 * @return
	 */
	@PostMapping("/homeTree")
	@ResponseBody
	public Object tree() {
		ShiroUser shiroUser = getShiroUser();
		return resourceService.selectTree(shiroUser);
	}

	/**
	 * <p>Title: 页面 - 资源管理列表</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月17日
	 * 
	 * @return
	 */
	@GetMapping("/manager")
	public String manager() {
		return "admin/resource/resource";
	}

	/**
	 * <p>Title: 资源管理树形列表</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月17日
	 * 
	 * @return
	 */
	@PostMapping("/treeGridAll")
	@ResponseBody
	public Object treeGridAll() {
		return resourceService.selectTreeGridAll();
	}

	/**
	 * <p>Title: 添加-资源管理</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月17日
	 * 
	 * @param resource
	 * @return
	 */
	@RequestMapping("/add")
	@ResponseBody
	public Object add(Resource resource) {
		resource.setCreateTime(new Date());
		// 选择菜单时将openMode设置为null
		Integer type = resource.getResourceType();
		if (null != type && type == 0) {
			resource.setOpenMode(null);
		}
		if (resourceService.insert(resource)) {
			return renderSuccess("添加成功！");
		}
		return renderError("添加失败！");
	}

	/**
	 * <p>Title: 删除-资源管理</p>
	 * <p>Description: 在删除前先判断是树形等级，如果是父级则删除其下所有的子级，如果是子级只删除一条</p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月17日
	 * 
	 * @param paramId
	 * @return
	 */
	@RequestMapping("/remove")
	@ResponseBody
	public Object remove(Long paramId) {
		Assert.isNull(paramId, "主键不能为空");
		if (resourceService.deleteBatchIds(paramId)) {
			return renderSuccess();
		}
		return renderError("删除失败！");
	}

	/**
	 * <p>Title: 修改 - 资源管理</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月17日
	 * 
	 * @param resource
	 * @return
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public Object edit(Resource resource) {
		// 选择菜单时将openMode设置为null
		Integer type = resource.getResourceType();
		if (null != type && type == 0) {
			resource.setOpenMode(null);
		}
		if (resourceService.updateById(resource)) {
			return renderSuccess();
		}
		return renderError("修改失败！");
	}

	/**
	 * <p>Title: 查询所有菜单，并根据角色ID对已授权的菜单做上标识</p>
	 * <p>Description: 角色授权时用</p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月20日
	 * 
	 * @param paramId
	 * @return
	 */
	@RequestMapping("/selectAuthorized")
	@ResponseBody
	public Object selectAuthorized(Long paramId) {
		Assert.isNull(paramId, "主键不能为空");
		return resourceService.selectResourceListByRoleIdTree(paramId);
	}

}

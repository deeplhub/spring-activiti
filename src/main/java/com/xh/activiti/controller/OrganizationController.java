package com.xh.activiti.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xh.activiti.commons.utils.Assert;
import com.xh.activiti.model.Organization;
import com.xh.activiti.service.IOrganizationService;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年3月22日
 */
@Controller
@RequestMapping("/admin/organization")
public class OrganizationController extends BaseController {

	@Autowired
	private IOrganizationService organizationService;

	/**
	 * 部门管理主页
	 *
	 * @return
	 */
	@GetMapping(value = "/manager")
	public String manager() {
		return "admin/organization/organization";
	}

	/**
	 * 部门资源树
	 *
	 * @return
	 */
	@PostMapping(value = "/tree")
	@ResponseBody
	public Object tree() {
		return organizationService.selectTree();
	}

	/**
	 * 部门列表
	 *
	 * @return
	 */
	@RequestMapping("/treeGrid")
	@ResponseBody
	public Object treeGrid() {
		return organizationService.selectTreeGrid();
	}

	/**
	 * 添加部门
	 *
	 * @param organization
	 * @return
	 */
	@RequestMapping("/add")
	@ResponseBody
	public Object add(Organization organization) {
		organization.setCreateTime(new Date());
		if (organizationService.insert(organization)) {
			return renderSuccess("添加成功！");
		}
		return renderError("添加失败");
	}

	/**
	 * 编辑
	 *
	 * @param organization
	 * @return
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public Object edit(Organization organization) {
		if (organizationService.updateById(organization)) {
			return renderSuccess("编辑成功！");
		}
		return renderError("编辑失败");
	}

	/**
	 * 删除部门
	 *
	 * @param id
	 * @return
	 */
	@RequestMapping("/remove")
	@ResponseBody
	public Object remove(Long paramId) {
		Assert.isNull(paramId, "主键不能为空");
		if (organizationService.deleteById(paramId)) {
			return renderSuccess("删除成功！");
		}
		return renderError("删除失败");
	}
}

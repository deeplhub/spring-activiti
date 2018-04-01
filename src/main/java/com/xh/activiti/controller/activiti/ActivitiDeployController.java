package com.xh.activiti.controller.activiti;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.xh.activiti.commons.utils.Assert;
import com.xh.activiti.commons.utils.PageData;
import com.xh.activiti.controller.BaseController;
import com.xh.activiti.service.activiti.IActivitiDeploymentService;

/**
 * <p>Title: 流程部署</p>
 * <p>Description: 对以下表进行操作：act_re_deployment,act_re_procdef</p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年3月29日
 */
@Controller
@RequestMapping("/admin/deploy")
public class ActivitiDeployController extends BaseController {

	@Autowired
	private IActivitiDeploymentService deploymentService;

	/**
	 * <p>Title: 流程部署页面</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月28日
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@GetMapping("/manager")
	public String queryList(HttpServletRequest request, HttpServletResponse response) {

		return "admin/activiti/deploy";
	}

	/**
	 * <p>Title: 流程部署列表</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月29日
	 * 
	 * @return
	 */
	@PostMapping("/deployList")
	@ResponseBody
	public Object deployDataGrid() {

		List<PageData> list = deploymentService.selectDeployList();
		return list;
	}

	/**
	 * <p>Title: 删除流程部署</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月28日
	 * 
	 * @param modelId
	 * @return
	 */
	@PostMapping("/remove")
	@ResponseBody
	public Object remove(String paramId) {
		Assert.isBlank(paramId, "ID不能为空");

		if (deploymentService.deleteDeployment(paramId, true)) {
			return renderSuccess("删除成功！");
		}
		return renderError("删除失败！");
	}

	/**
	 * <p>Title: 上传流程并部署</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月29日
	 * 
	 * @param request
	 * @param file
	 * @return
	 */
	@PostMapping("/add")
	@ResponseBody
	public Object deployInputStream(HttpServletRequest request, MultipartFile file) {
		try {
			// 如果文件不为空，写入上传路径
			if (!file.isEmpty()) {
				InputStream inputStream = file.getInputStream();
				// 上传文件名
				String fileName = file.getOriginalFilename();
				// 判断是否为限制文件类型
				if (!checkFile(fileName)) {
					// 限制文件类型，请求转发到原始请求页面，并携带错误提示信息
					return renderError("不支持的文件类型！");
				}
				deploymentService.deployInputStream(fileName, inputStream);
				return renderSuccess("上传成功！");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return renderError("上传失败！");
	}

	/**
	 * 判断是否为允许的上传文件类型,true表示允许
	 */
	private boolean checkFile(String fileName) {
		// 设置允许上传文件类型
		String suffixList = "bpmn";
		// 获取文件后缀
		String suffix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
		if (suffixList.contains(suffix.trim().toLowerCase())) {
			return true;
		}
		return false;
	}
}

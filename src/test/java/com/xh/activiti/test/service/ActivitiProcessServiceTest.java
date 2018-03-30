package com.xh.activiti.test.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.activiti.engine.RepositoryService;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.xh.activiti.commons.utils.PageData;
import com.xh.activiti.service.activiti.IActivitiProcessService;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年3月30日
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/spring-config.xml" })
public class ActivitiProcessServiceTest {

	@Autowired
	private IActivitiProcessService processService;

	@Autowired
	private RepositoryService repositoryService;

	// @Test
	public void queryList() {
		List<PageData> list = processService.selectDeployList();
		System.out.println(JSON.toJSON(list));
	}

	@Test
	public void showView() throws IOException {
		// 创建仓库服务对对象
		// 从仓库中找需要展示的文件
		String deploymentId = "120001";
		List<String> names = repositoryService.getDeploymentResourceNames(deploymentId);
		String imageName = null;
		for (String name : names) {
			if (name.indexOf(".png") >= 0) {
				imageName = name;
			}
		}
		if (imageName != null) {
			// System.out.println(imageName);
			File f = new File("E:/" + imageName);
			// 通过部署ID和文件名称得到文件的输入流
			InputStream in = repositoryService.getResourceAsStream(deploymentId, imageName);
			FileUtils.copyInputStreamToFile(in, f);
		}
	}
}

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
public class ActivitiImageServiceTest {

	@Autowired
	private RepositoryService repositoryService;

	/**
	 * <p>Title: 保存流程部署图片</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年4月2日
	 * 
	 * @throws IOException
	 */
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

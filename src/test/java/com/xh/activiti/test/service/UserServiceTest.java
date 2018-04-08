package com.xh.activiti.test.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
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
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/spring-config.xml")
public class UserServiceTest {

	@Autowired
	private IUserService userService;

	// @Test
	public void selectByLoginName() {
		User user = new User();
		user.setLoginName("admin");

		List<User> list = userService.selectByLoginName(user);
		System.out.println(list.size());
	}

	// @Test
	public void selectList() {
		User user = new User();
		user.setLoginName("admin");
		EntityWrapper<User> wrapper = new EntityWrapper<User>(user);
		List<User> list = userService.selectList(wrapper);
		System.out.println(list.size());
		for (User user2 : list) {
			System.out.println(user2.toString());
		}
	}

	@Test
	public void findAll() throws InterruptedException {
		for (int i = 0; i < 10; i++) {
			long startTime = System.currentTimeMillis(), //
					endTime = 0, //
					totalTime = 0;
			List<User> list = userService.selectList(null);
			
			endTime = System.currentTimeMillis();
			totalTime = (endTime - startTime);

			System.out.println("###########################################");
			System.out.println();
			System.out.println("当前编号：" + i);
			System.out.println("总条数：" + list.size());
			System.out.println("耗时：" + totalTime);
			System.out.println();
			System.out.println("###########################################");
		}
	}
}

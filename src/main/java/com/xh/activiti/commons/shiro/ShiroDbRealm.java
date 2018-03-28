package com.xh.activiti.commons.shiro;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.xh.activiti.model.User;
import com.xh.activiti.service.IRoleService;
import com.xh.activiti.service.IUserService;

/**
 * <p>Title: shiro权限认证</p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年3月5日
 */
public class ShiroDbRealm extends AuthorizingRealm {
	private static final Logger LOGGER = LogManager.getLogger(ShiroDbRealm.class);

	@Autowired
	private IUserService userService;
	@Autowired
	private IRoleService roleService;

	public ShiroDbRealm(CacheManager cacheManager, CredentialsMatcher matcher) {
		super(cacheManager, matcher);
	}

	/**
	 * 认证<br>
	 * Shiro登录认证(原理：用户提交 用户名和密码 --- shiro 封装令牌 ---- realm 通过用户名将密码查询返回 ----
	 * shiro 自动去比较查询出密码和用户输入密码是否一致---- 进行登陆控制 )
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		LOGGER.info("Shiro开始登录认证");
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		User uservo = new User();
		uservo.setLoginName(token.getUsername());
		List<User> list = userService.selectByLoginName(uservo);
		// 账号不存在
		if (list == null || list.isEmpty()) {
			return null;
		}
		User user = list.get(0);
		// 账号未启用
		if (user.getStatus() == 1) {
			return null;
		}
		// 读取用户的url和角色
		Map<String, Set<String>> resourceMap = roleService.selectResourceMapByUserId(user.getId());
		Set<String> urls = resourceMap.get("urls");
		Set<String> roles = resourceMap.get("roles");
		ShiroUser shiroUser = new ShiroUser(user.getId(), user.getLoginName(), user.getName(), user.isAdmin(), urls);
		shiroUser.setRoles(roles);
		// 认证缓存信息
		return new SimpleAuthenticationInfo(shiroUser, user.getPassword().toCharArray(), ShiroByteSource.of(user.getSalt()), getName());
	}

	/**
	 * Shiro权限认证<br>
	 * 授权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		LOGGER.info("========= Shiro权限认证 - 授权 ===========");
		// 获得经过认证的主体信息
		ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.setRoles(shiroUser.getRoles());
		info.addStringPermissions(shiroUser.getUrlSet());

		// 以上完成了动态地对用户授权
		LOGGER.debug("role => " + shiroUser.getRoles());
		LOGGER.debug("permission => " + shiroUser.getUrlSet());
		return info;
	}

	@Override
	public void onLogout(PrincipalCollection principals) {
		LOGGER.info("退出登录并清除缓存");
		Cache c = getAuthenticationCache();
		LOGGER.info("清除【认证】缓存之前");
		for (Object o : c.keys()) {
			LOGGER.info(o + " , " + c.get(o));
		}

		super.clearCachedAuthorizationInfo(principals);
		LOGGER.info("调用父类清除【认证】缓存之后");
		for (Object o : c.keys()) {
			LOGGER.info(o + " , " + c.get(o));
		}
		ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();

		SimplePrincipalCollection collection = new SimplePrincipalCollection();
		collection.add(shiroUser.getLoginName(), super.getName());
		super.clearCachedAuthenticationInfo(collection);
		LOGGER.info("添加了代码清除【认证】缓存之后");
		int cacheSize = c.keys().size();
		LOGGER.info("【认证】缓存的大小:" + c.keys().size());
		if (cacheSize == 0) {
			LOGGER.info("说明【认证】缓存被清空了。");
		}
	}

}

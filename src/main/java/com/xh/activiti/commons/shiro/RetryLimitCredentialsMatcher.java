package com.xh.activiti.commons.shiro;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * <p>Title: 输错5次密码锁定半小时，ehcache.xml配置</p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年3月5日
 */
public class RetryLimitCredentialsMatcher extends HashedCredentialsMatcher implements InitializingBean {
	
	private final static Logger LOGGER = LogManager.getLogger(RetryLimitCredentialsMatcher.class);
	private final static String DEFAULT_CHACHE_NAME = "retryLimitCache";

	private final CacheManager cacheManager;
	private String retryLimitCacheName;
	private Cache<String, AtomicInteger> passwordRetryCache;
	private PasswordHash passwordHash;

	public RetryLimitCredentialsMatcher(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
		this.retryLimitCacheName = DEFAULT_CHACHE_NAME;
	}

	public String getRetryLimitCacheName() {
		return retryLimitCacheName;
	}

	public void setRetryLimitCacheName(String retryLimitCacheName) {
		this.retryLimitCacheName = retryLimitCacheName;
	}

	public void setPasswordHash(PasswordHash passwordHash) {
		this.passwordHash = passwordHash;
	}

	@Override
	public boolean doCredentialsMatch(AuthenticationToken authcToken, AuthenticationInfo info) {
		LOGGER.info("RetryLimitCredentialsMatcher.class:doCredentialsMatch()");
		LOGGER.info("输入密码错误记录");
		String username = (String) authcToken.getPrincipal();
		LOGGER.info("用户名：" + username);
		// retry count + 1
		AtomicInteger retryCount = passwordRetryCache.get(username);
		if (retryCount == null) {
			retryCount = new AtomicInteger(0);
			passwordRetryCache.put(username, retryCount);
		}
		if (retryCount.incrementAndGet() > 5) {
			// if retry count > 5 throw
			LOGGER.warn("username: " + username + " tried to login more than 5 times in period");
			throw new ExcessiveAttemptsException("用户名: " + username + " 密码连续输入错误超过5次，锁定半小时！");
		} else {
			passwordRetryCache.put(username, retryCount);
		}

		boolean matches = super.doCredentialsMatch(authcToken, info);
		LOGGER.info(matches);
		if (matches) {
			// clear retry data
			passwordRetryCache.remove(username);
		}
		return matches;
	}

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(passwordHash, "you must set passwordHash!");
		super.setHashAlgorithmName(passwordHash.getAlgorithmName());
		super.setHashIterations(passwordHash.getHashIterations());
		this.passwordRetryCache = cacheManager.getCache(retryLimitCacheName);
	}
}

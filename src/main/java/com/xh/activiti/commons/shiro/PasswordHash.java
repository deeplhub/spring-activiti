package com.xh.activiti.commons.shiro;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.DigestUtils;

/**
 * <p>Title: shiro密码加密配置</p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @date 2018年3月6日
 * 
 */
public class PasswordHash extends DigestUtils implements InitializingBean {

	private String algorithmName;
	private int hashIterations;

	public void afterPropertiesSet() throws Exception {
		Assert.hasLength(algorithmName, "algorithmName mast be MD5、SHA-1、SHA-256、SHA-384、SHA-512");
	}

	public String toHex(Object source, Object salt) {
		return hashByShiro(algorithmName, source, salt, hashIterations);
	}

	/**
	* 使用shiro的hash方式
	* @param algorithmName 算法
	* @param source 源对象
	* @param salt 加密盐
	* @param hashIterations hash次数
	* @return 加密后的字符
	*/
	public static String hashByShiro(String algorithmName, Object source, Object salt, int hashIterations) {
		return new SimpleHash(algorithmName, source, salt, hashIterations).toHex();
	}

	public String getAlgorithmName() {
		return algorithmName;
	}

	public void setAlgorithmName(String algorithmName) {
		this.algorithmName = algorithmName;
	}

	public int getHashIterations() {
		return hashIterations;
	}

	public void setHashIterations(int hashIterations) {
		this.hashIterations = hashIterations;
	}
}

package com.xh.activiti.commons.utils;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年3月13日
 */
public class GenEntityTableTest {

	public static void main(String[] args) {
		GenEntityTable entity = new GenEntityTable();
		entity.setPropertiesPath("config/db.properties");
		entity.setAuthorName("H.Yang");
		entity.setPackageName("com.xh.activiti.model");
		entity.setTableName("leave_bill");
		entity.init();
		entity.startTable();
	}

}

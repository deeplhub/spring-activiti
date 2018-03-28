package com.xh.activiti.commons.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;

/**
 * <p>Title: 获取数据库基本信息的工具类</p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年3月12日
 */
public class GenEntityTable {

	private static String remarkes = "";// 数据库表注释
	private static LinkedList<Map> resultCol = null;// 列信息
	private static boolean utilPackage = false; // 是否需要导入包java.util.*
	private static boolean sqlPackage = false; // 是否需要导入包java.sql.*
	private static Connection connection = null;
	private static ResultSet resultSet = null;

	private String propertiesPath;
	private String packageName;// 指定实体生成所在包的路径
	private String authorName;// 作者名字
	private String tableName;// 表名

	public GenEntityTable() {
		super();
	}

	public GenEntityTable(String propertiesPath, String packageName, String authorName, String tableName, String... str) {
		super();
		this.propertiesPath = propertiesPath;
		this.packageName = packageName;
		this.authorName = authorName;
		this.tableName = tableName;
	}

	public GenEntityTable(final String driver, final String url, final String name, final String pass) {
		try {
			Properties props = new Properties();
			props.setProperty("user", name);
			props.setProperty("password", pass);
			props.setProperty("remarks", "true"); // 设置可以获取remarks信息
			props.setProperty("useInformationSchema", "true");// 设置可以获取tables remarks信息

			// 创建连接
			Class.forName(driver);
			// getConnection = DriverManager.getConnection(URL, NAME, PASS);
			connection = DriverManager.getConnection(url, props);
			System.out.println("数据库连接成功");
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println("数据库驱动加载或连接异常！");
			e.printStackTrace();
		}
	}

	/**
	 * <p>Title: 读取配置文件连接数据库</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月12日
	 * 
	 */
	public void init() {
		try {
			Properties properties = new Properties();
			InputStream in = GenEntityTable.class.getClassLoader().getResourceAsStream(this.propertiesPath);
			properties.load(in);

			Properties props = new Properties();
			props.setProperty("user", properties.getProperty("db.master.user"));
			props.setProperty("password", properties.getProperty("db.master.password"));
			props.setProperty("remarks", "true"); // 设置可以获取remarks信息
			props.setProperty("useInformationSchema", "true");// 设置可以获取tables remarks信息

			// 创建连接
			Class.forName(properties.getProperty("db.master.driver"));
			connection = DriverManager.getConnection(properties.getProperty("db.master.url"), props);
			System.out.println("数据库连接成功");
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	/**
	 * <p>Title: 获取指定表的基本信息：字段名、字段类型、字段注释</p>
	 * <p>Description: 自带关闭连接</p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月12日
	 * 
	 */
	public void startTable() {
		try {
			resultCol = new LinkedList<>();
			Map<String, Object> map = null;
			resultSet = connection.getMetaData().getTables(null, "%", tableName, new String[] { "TABLE" });
			while (resultSet.next()) {
				remarkes = resultSet.getString("REMARKS");

				ResultSet rs = connection.getMetaData().getColumns(null, "%", resultSet.getString("TABLE_NAME").toUpperCase(), "%");
				while (rs.next()) {
					map = new HashMap<>();
					map.put("columnName", rs.getString("COLUMN_NAME"));
					map.put("remarks", rs.getString("REMARKS"));
					map.put("dbType", rs.getString("TYPE_NAME"));
					map.put("valueType", sqlType2JavaType(rs.getString("TYPE_NAME")));

					if (rs.getString("TYPE_NAME").equalsIgnoreCase("datetime") || rs.getString("TYPE_NAME").equalsIgnoreCase("timestamp")) {
						utilPackage = true;
					}
					if (rs.getString("TYPE_NAME").equalsIgnoreCase("image") || rs.getString("TYPE_NAME").equalsIgnoreCase("text")) {
						sqlPackage = true;
					}
					resultCol.add(map);
				}
				if (rs != null) {
					rs.close();
				}
			}

			// 在内存中生成代码
			String content = parse(transVar(tableName));
			// 写入到文件中
			File directory = new File("");
			String outputPath = directory.getAbsolutePath() + "/src/main/java/" + this.packageName.replace(".", "/") + "/"
					+ initcap(transVar(tableName)) + ".java";
			System.out.println("写出的路径:" + outputPath);
			// 创建文件
			File file = new File(outputPath);
			if (!file.exists()) {
				file.createNewFile();
			}
			// 写出到硬盘
			FileWriter fw = new FileWriter(file);
			PrintWriter pw = new PrintWriter(fw);
			pw.println(content);
			pw.flush();
			pw.close();
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		} finally {
			this.close();
		}
	}

	/**
	 * <p>Title: 获取当前是数据库下的所有表的基本信息：字段名、字段类型、字段注释</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月13日
	 * 
	 */
	public void startMultitermTable() {
		try {
			Map<String, Object> map = null;
			resultSet = connection.getMetaData().getTables(null, "%", null, new String[] { "TABLE" });
			while (resultSet.next()) {
				resultCol = new LinkedList<>();
				tableName = resultSet.getString("TABLE_NAME");
				remarkes = resultSet.getString("REMARKS");
				ResultSet rs = connection.getMetaData().getColumns(null, "%", tableName.toUpperCase(), "%");
				while (rs.next()) {
					map = new HashMap<>();
					map.put("columnName", rs.getString("COLUMN_NAME"));
					map.put("remarks", rs.getString("REMARKS"));
					map.put("dbType", rs.getString("TYPE_NAME"));
					map.put("valueType", sqlType2JavaType(rs.getString("TYPE_NAME")));

					if (rs.getString("TYPE_NAME").equalsIgnoreCase("datetime") || rs.getString("TYPE_NAME").equalsIgnoreCase("timestamp")) {
						utilPackage = true;
					}
					if (rs.getString("TYPE_NAME").equalsIgnoreCase("image") || rs.getString("TYPE_NAME").equalsIgnoreCase("text")) {
						sqlPackage = true;
					}
					resultCol.add(map);
				}
				if (rs != null) {
					rs.close();
				}

				// 在内存中生成代码
				String content = parse(transVar(tableName));
				// 写入到文件中
				File directory = new File("");
				String outputPath = directory.getAbsolutePath() + "/src/main/java/" + this.packageName.replace(".", "/") + "/"
						+ transVar(tableName) + ".java";
				System.out.println("写出的路径:" + outputPath);
				// 创建文件
				File file = new File(outputPath);
				if (!file.exists()) {
					file.createNewFile();
				}
				// 写出到硬盘
				FileWriter fw = new FileWriter(file);
				PrintWriter pw = new PrintWriter(fw);
				pw.println(content);
				pw.flush();
				pw.close();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			this.close();
		}
	}

	/**
	 * <p>Title: 关闭数据库连接</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月12日
	 * 
	 */
	private static void close() {
		try {
			if (resultSet != null) {
				resultSet.close();
			}
			if (connection != null) {
				connection.close();
			}
			System.out.println("数据库连接关闭");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/** 
	 * 功能：生成实体类主体代码 
	 * @param colNames 
	 * @param colTypes 
	 * @param colSizes 
	 * @return 
	 */
	private String parse(String tablename) {
		StringBuffer sb = new StringBuffer();
		sb.append("package " + this.packageName + ";\r\n");
		sb.append("\r\n");
		// 判断是否导入工具包
		if (utilPackage) {
			sb.append("import java.util.Date;\r\n");
		}
		if (sqlPackage) {
			sb.append("import java.sql.*;\r\n");
		}
		// 注释部分
		sb.append("\r\n");
		sb.append("/**\r\n");
		sb.append(" * <p>Title: " + remarkes + "</p>\r\n");
		sb.append(" * <p>Description: </p>\r\n");
		sb.append(" * \r\n");
		sb.append(" * @author " + this.authorName + "\r\n");
		sb.append(" * @QQ 1033542070\r\n");
		sb.append(" * \r\n");
		sb.append(" * @date " + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "\r\n");
		sb.append(" */ \r\n");
		// 实体部分
		sb.append("public class " + transVar(tableName) + "{\r\n");
		processAllAttrs(sb);// 属性
		processAllMethod(sb);// get set方法
		sb.append("}");

		return sb.toString();
	}

	/**
	 * <p>Title: 生成所有属性 </p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月12日
	 * 
	 * @param sb
	 */
	private void processAllAttrs(StringBuffer sb) {
		// 列名集合
		for (Map<String, String> mapAttr : resultCol) {
			if (StringUtils.isNotBlank(mapAttr.get("remarks"))) {
				sb.append("\t// " + mapAttr.get("remarks") + "\r\n");
			}
			sb.append("\tprivate " + mapAttr.get("valueType") + " " + defineVar(mapAttr.get("columnName")) + ";\r\n");
		}
	}

	/**
	 * <p>Title: 生成所有方法 </p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月12日
	 * 
	 * @param sb
	 */
	private void processAllMethod(StringBuffer sb) {

		for (Map<String, String> mapMethod : resultCol) {
			// SET
			sb.append("\r\n");
			if (StringUtils.isNotBlank(mapMethod.get("remarks"))) {
				sb.append("\t/**\r\n");
				sb.append("\t * " + mapMethod.get("remarks") + "\r\n");
				sb.append("\t */\r\n");
			}
			sb.append("\tpublic void set" + transVar(mapMethod.get("columnName")) + "(" + mapMethod.get("valueType") + " "
					+ defineVar(mapMethod.get("columnName")) + ") {\r\n");
			sb.append("\t\tthis." + defineVar(mapMethod.get("columnName")) + " = " + defineVar(mapMethod.get("columnName")) + ";\r\n");
			sb.append("\t}\r\n");

			// GET
			sb.append("\r\n");
			if (StringUtils.isNotBlank(mapMethod.get("remarks"))) {
				sb.append("\t/**\r\n");
				sb.append("\t * " + mapMethod.get("remarks") + "\r\n");
				sb.append("\t */\r\n");
			}
			sb.append("\tpublic " + mapMethod.get("valueType") + " get" + initcap(transVar(mapMethod.get("columnName"))) + "() {\r\n");
			sb.append("\t\treturn " + defineVar(mapMethod.get("columnName")) + ";\r\n");
			sb.append("\t}\r\n");
		}
	}

	/**
	 * <p>Title: 将输入字符串的首字母改成大写</p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月12日
	 * 
	 * @param str
	 * @return
	 */
	private String initcap(String str) {
		char[] ch = str.toCharArray();
		if (ch[0] >= 'a' && ch[0] <= 'z') {
			ch[0] = (char) (ch[0] - 32);
		}
		return new String(ch);
	}

	/**
	 * <p>Title: 用于生成get/set方法时  </p>
	 * <p>Description: 第一个字母大写，“_”后面一个字母大写，并去掉“_”</p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月12日
	 * 
	 * @param str
	 * @return
	 */
	private String transVar(String str) {
		StringBuffer sb = new StringBuffer(str.toLowerCase());
		String sign = "_";
		int index = 0;
		while ((index = sb.indexOf(sign, index)) != -1) {
			sb.replace(index, (index + sign.length()), "");

			char[] ch = new String(sb).toCharArray();
			if (ch[0] >= 'a' && ch[0] <= 'z') {
				ch[0] = (char) (ch[0] - 32);
			}
			if (index != 0 && index != ch.length) {
				ch[index] = (char) (ch[index] - 32);
			}
			sb = new StringBuffer(new String(ch));
			index = index + sign.length();
		}
		return sb.toString();
	}

	/**
	 * <p>Title: 用于定义变量名  </p>
	 * <p>Description: 首字母小写，“_”后面一个字母大写，并去掉“_”</p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月12日
	 * 
	 * @param str
	 * @return
	 */
	private String defineVar(String str) {
		StringBuffer sb = new StringBuffer(str.toLowerCase());
		String sign = "_";
		int index = 0;
		while ((index = sb.indexOf(sign, index)) != -1) {
			sb.replace(index, (index + sign.length()), "");
			char[] ch = new String(sb).toCharArray();
			if (ch[0] >= 'a' && ch[0] <= 'z' && index == 0) {
				ch[0] = (char) (ch[0] - 32);
			}
			if (index != 0 && index != ch.length) {
				ch[index] = (char) (ch[index] - 32);
			}
			sb = new StringBuffer(new String(ch));
			index = index + sign.length();
		}
		return sb.toString();
	}

	/**
	 * <p>Title: 获得列的数据类型 </p>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * @date 2018年3月12日
	 * 
	 * @param sqlType
	 * @return
	 */
	private String sqlType2JavaType(String sqlType) {
		if (sqlType.equalsIgnoreCase("bit")) {
			return "boolean";
		} else if (sqlType.equalsIgnoreCase("tinyint")) {
			return "byte";
		} else if (sqlType.equalsIgnoreCase("smallint")) {
			return "short";
		} else if (sqlType.equalsIgnoreCase("int")) {
			return "Integer";
		} else if (sqlType.equalsIgnoreCase("bigint")) {
			return "Long";
		} else if (sqlType.equalsIgnoreCase("float")) {
			return "Float";
		} else if (sqlType.equalsIgnoreCase("decimal") || sqlType.equalsIgnoreCase("numeric") || sqlType.equalsIgnoreCase("real")
				|| sqlType.equalsIgnoreCase("money") || sqlType.equalsIgnoreCase("smallmoney")) {
			return "Double";
		} else if (sqlType.equalsIgnoreCase("varchar") || sqlType.equalsIgnoreCase("char") || sqlType.equalsIgnoreCase("nvarchar")
				|| sqlType.equalsIgnoreCase("nchar") || sqlType.equalsIgnoreCase("text")) {
			return "String";
		} else if (sqlType.equalsIgnoreCase("datetime") || sqlType.equalsIgnoreCase("timestamp")) {
			return "Date";
		} else if (sqlType.equalsIgnoreCase("image")) {
			return "Blod";
		}

		return null;
	}

	public String getPropertiesPath() {
		return propertiesPath;
	}

	public void setPropertiesPath(String propertiesPath) {
		this.propertiesPath = propertiesPath;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

}

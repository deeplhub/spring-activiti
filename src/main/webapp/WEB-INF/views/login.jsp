<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>登录</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

</head>

<body>
	<form action="login" method="post">
		<p>
			登陆名：<input type="text" name="username" value="admin">
		</p>
		<p>
			密码：<input type="password" name="password" value="test">
		</p>
		<p>
			${_csrf.parameterName}：<input type="text" name="${_csrf.parameterName}" value="${_csrf.token}" />
		</p>
		<p>
			<input type="submit" value="提交">
		</p>
	</form>
</body>
</html>

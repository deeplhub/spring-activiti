<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="edge" />
<link rel="stylesheet" type="text/css" href="${staticPath }/static/easyui/themes/gray/easyui.css" />
<link rel="stylesheet" type="text/css" href="${staticPath }/static/easyui/themes/icon.css" />
<link rel="stylesheet" type="text/css" href="${staticPath }/static/easyui/themes/color.css" />
<link rel="stylesheet" type="text/css" href="${staticPath }/static/zTree/css/zTreeStyle/zTreeStyle.css" />
<style type="text/css">
#fm {
	margin: 0;
	padding: 10px 30px;
}

.ftitle {
	font-size: 14px;
	font-weight: bold;
	color: #666;
	padding: 5px 0;
	margin-bottom: 10px;
	border-bottom: 1px solid #ccc;
}

.fitem {
	margin-bottom: 5px;
}

.fitem label {
	display: inline-block;
	width: 80px;
}
.hidden-label{
	display: none;
}
</style>
<script type="text/javascript" src="${staticPath }/static/js/jquery-3.3.1.js"></script>
<script type="text/javascript" src="${staticPath }/static/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${staticPath }/static/easyui/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${staticPath }/static/zTree/js/jquery.ztree.all.js"></script>
<script type="text/javascript">
	var basePath = "${staticPath }";
</script>
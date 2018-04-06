<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>已办任务管理页</title>
<%@ include file="/commons/base.jsp"%>

<script type="text/javascript">
	$(function() {
		$('#dataGrid').datagrid({
			title : '已办任务列表',
			width : 700, //高度
			height : 'auto', //宽度自动
			iconCls : "icon-edit", //图标
			loadMsg : "正在加载数据...",
			method : "post",
			url : basePath + "/task/finishTaskProcess",
			//sortName: 'createTime',
			//sortOrder: 'desc',
			nowrap : false,
			striped : true,
			border : true,
			collapsible : false, //是否可折叠的
			fit : true, //自动大小
			remoteSort : false,
			idField : 'fldId',
			singleSelect : true, //是否单选
			pagination : true, //分页控件
			rownumbers : true, //行号
		});

	});

</script>
</head>
<body>
	<table id="dataGrid" class="easyui-datagrid hidden-label">
		<thead>
			<tr>
				<!-- <th field="hid" width="100">编号</th> -->
				<th field="userName" width="100">请假人</th>
				<th field="content" width="300">请假内容</th>
				<th field="days" width="60">请假天数</th>
				<th field="startTime" formatter="formatterDate" width="150">开始时间</th>
				<th field="endTime" formatter="formatterDate" width="150">结束时间</th>
				<!-- <th field="createTime" formatter="formatterDate" width="150">创建时间</th> -->
			</tr>
		</thead>
	</table>

</body>
</html>
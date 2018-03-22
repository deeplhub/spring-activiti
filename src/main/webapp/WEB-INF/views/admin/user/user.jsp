<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>用户</title>
<%@ include file="/commons/base.jsp"%>
<link rel="stylesheet" type="text/css" href="${staticPath }/static/css/main.css">
<script type="text/javascript" src="${staticPath }/static/js/jquery.time.js"></script>
<script type="text/javascript" src="${staticPath }/static/js/main.js"></script>
<script type="text/javascript">
	$(function () {
		$('#dataGrid').datagrid({
			title: '用户列表',
			width: 700, //高度
			height: 'auto', //宽度自动
			iconCls: "icon-edit", //图标
			loadMsg: "正在加载数据...",
			method: "post",
			url: basePath + "/admin/user/dataGrid",
			sortName: 'createTime',
			sortOrder: 'desc',
			nowrap: false,
			striped: true,
			border: true,
			collapsible: false, //是否可折叠的
			fit: true, //自动大小
			remoteSort: false,
			idField: 'fldId',
			singleSelect: true, //是否单选
			pagination: true, //分页控件
			rownumbers: true, //行号
		});
	
		//设置分页控件
		var p = $('#dataGrid').datagrid('getPager');
		$(p).pagination({
			pageSize: 10, //每页显示的记录条数，默认为10
			pageList: [10, 20, 35, 40, 50], //可以设置每页记录条数的列表
			beforePageText: '第', //页数文本框前显示的汉字
			afterPageText: '页    共 {pages} 页',
			displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录',
		});
	});
	
	function formatterDate(value, row, index) {
		return TimeObjectUtil.longMsTimeConvertToDateTime(value);
	}
	
	function sex(value, row, index) {
		return (value == 0) ? "男" : "女";
	}
	function status(value, row, index) {
		return (value == 0) ? "正常" : "停用";
	}
	function userType(value, row, index) {
		if (value == 0) {
			return "管理员";
		} else if (value == 1) {
			return "用户";
		}
		return "未知类型";
	}
	
	function successCallback(result) {
		if (result.code == 0) {
			$("#editOneDialog").dialog('close');
			$("#dataGrid").datagrid('reload');
		} else {
			$.messager.show({
				title: 'Error',
				msg: result.msg
			});
		}
	}
</script>
</head>
<body>
	<table id="dataGrid" class="easyui-datagrid hidden-label" toolbar="#toolbar">
		<thead>
			<tr>
				<th field="loginName" width="120">登陆名</th>
				<th field="name" width="120">用户名</th>
				<th field="sex" formatter="sex" width="80">性别</th>
				<th field="age" width="80">年龄</th>
				<th field="phone" width="120">手机号</th>
				<th field="organizationName" width="120">所属机构</th>
				<th field="roleName" width="120">角色</th>
				<th field="userType" formatter="userType" width="80">用户类别</th>
				<th field="status" formatter="status" width="80">用户状态</th>
				<th field="createTime" formatter="formatterDate" width="150">创建时间</th>
			</tr>
		</thead>
	</table>
	<div id="toolbar" class="hidden-label">
		<button class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="editGrid('#dataGrid', '#editOneDialog', '#formOneId', '', '/admin/user/add')">添加</button>
		<button class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editGrid('#dataGrid', '#editOneDialog', '#formOneId', '#keyId', '/admin/user/edit')">编辑</button>
		<button class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="remove('#dataGrid', '/admin/role/remove')">删除</button>
		<button class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="authorized()">授权</button>
	</div>
	
	<div id="editOneDialog" class="easyui-dialog hidden-label" style="width:380px; height:420px; padding:10px 20px;"
		closed="true" buttons="#dlg-buttons">
		<div class="ftitle"></div>
		<form id="formOneId" method="post">
			<input id="keyId" name="id" type="hidden">
			<div class="fitem">
				<label>登陆名：</label>
				<input name="loginName" class="easyui-validatebox">
			</div>
			<div class="fitem">
				<label>用户名：</label>
				<input name="name" class="easyui-validatebox">
			</div>
			<div class="fitem">
				<label>密码：</label>
				<input name="password" class="easyui-validatebox">
			</div>
			<div class="fitem">
				<label>性别：</label>
				<input name="sex">
			</div>
			<div class="fitem">
				<label>年龄：</label>
				<input name="age" class="easyui-validatebox">
			</div>
			<div class="fitem">
				<label>手机号：</label>
				<input name="phone" class="easyui-validatebox">
			</div>
			<div class="fitem">
				<label>用户类别：</label>
				<input name="userType" class="easyui-validatebox">
			</div>
			<div class="fitem">
				<label>用户状态：</label>
				<input name="status" class="easyui-validatebox">
			</div>
			<div class="fitem">
				<label>所属机构：</label>
				<input name="status" class="easyui-validatebox">
			</div>
		</form>
	</div>
	<div id="dlg-buttons" class="hidden-label">
		<button class="easyui-linkbutton" iconCls="icon-ok" onclick="saveAndUpdate('#formOneId')">保存</button>
		<button class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#baseDialog').dialog('close')">取消</button>
	</div>
</body>
</html>
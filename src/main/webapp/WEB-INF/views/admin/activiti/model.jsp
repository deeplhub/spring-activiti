<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>流程模列表</title>
<%@ include file="/commons/base.jsp"%>
<link rel="stylesheet" type="text/css" href="${staticPath }/static/css/main.css">
<link rel="stylesheet" type="text/css" href="${staticPath }/static/zTree/css/demo.css">
<script type="text/javascript" src="${staticPath }/static/js/main.js"></script>

<script type="text/javascript">
	$(function() {
		$('#dataGrid').datagrid({
			title : '角色管理列表',
			width : 700, //高度
			height : 'auto', //宽度自动
			iconCls : "icon-edit", //图标
			loadMsg : "正在加载数据...",
			method : "post",
			url : basePath + "/admin/activitimodel/queryModelDataGrid",
			//sortName : 'createTime',
			//sortOrder : 'desc',
			nowrap : false,
			striped : true,
			border : true,
			collapsible : false,//是否可折叠的 
			fit : true,//自动大小 
			remoteSort : false,
			idField : 'fldId',
			singleSelect : true,//是否单选 
			pagination : true,//分页控件 
			rownumbers : true,//行号 
		});

	});

	function successCallback(result) {
		if (result.code == 0) {
			$("#editOneDialog").dialog('close');
			$("#dataGrid").datagrid('reload');
			console.info(result.obj);
			window.location.href=basePath+"/admin/activitimodel/modeler?modelId="+result.obj;
		} else {
			$.messager.show({
				title : 'Error',
				msg : result.msg
			});
		}
	}

	function createModel() {
		var data = {
			url : basePath + "/admin/",
			data : {
				name : "常海洋",
				key : "changhy",
				description : "这是一个什么？"
			},
			returnType : "json"
		};
		setting.post(data);
	}
</script>
</head>
<body>
	<h1>流程模列表页面</h1>
	<table id="dataGrid" class="easyui-datagrid hidden-label" toolbar="#toolbar">
		<thead>
			<tr>
				<th field="name" width="100">名称</th>
				<th field="key" width="200">分类</th>
				<th field="category" width="80">分类2</th>
				<th field="createTime" width="100">创建时间</th>
				<th field="lastUpdateTime" width="100">最新修改时间</th>
				<th field="version" width="100">版本</th>
			</tr>
		</thead>
	</table>
	<div id="toolbar" class="hidden-label">
		<button class="easyui-linkbutton" iconCls="icon-add" plain="true"
			onclick="editGrid('#dataGrid', '#editDialog', '#formId', '', '/admin/activitimodel/add')">创建模型</button>
	</div>


	<div id="editDialog" class="easyui-dialog hidden-label" style="width: 400px; height: 280px; padding: 10px 20px;"
		closed="true" buttons="#dlg-buttons">
		<form id="formId" method="post">
			<div class="fitem">
				<label>名称:</label>
				<input name="name" class="easyui-validatebox" >
			</div>
			<div class="fitem">
				<label>KEY:</label>
				<input name="key" class="easyui-validatebox" >
			</div>
			<div class="fitem">
				<label>描述:</label>
				<input name="description" class="easyui-validatebox" >
			</div>
		</form>
	</div>
	<div id="dlg-buttons" class="hidden-label">
		<button class="easyui-linkbutton" iconCls="icon-ok" onclick="saveAndUpdate('#formId')">保存</button>
		<button class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#editDialog').dialog('close')">取消</button>
	</div>
</body>
</html>
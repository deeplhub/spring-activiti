<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>流程定义</title>
<%@ include file="/commons/base.jsp"%>
<script type="text/javascript">
	$(function() {
		$('#dataGrid').datagrid({
			title : '流程定义列表',
			width : 700, //高度
			height : 'auto', //宽度自动
			iconCls : "icon-edit", //图标
			loadMsg : "正在加载数据...",
			method : "post",
			url : basePath + "/admin/definition/definitionList",
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
			$("#editDialog").dialog('close');
			$("#dataGrid").datagrid('reload');
			$.messager.show({
				title : 'Success',
				msg : result.msg
			});
		} else {
			$.messager.show({
				title : 'Error',
				msg : result.msg
			});
		}
	}
	
	function readDiagram(){
		var row = $("#dataGrid").datagrid('getSelected');
		if (row) {
			window.open(basePath + "/admin/definition/readDefinitionDiagram?deploymentId=" + row.deploymentId);
		}
	}

</script>
</head>
<body>
	<table id="dataGrid" class="easyui-datagrid hidden-label" toolbar="#toolbar">
		<thead>
			<tr>
				<th field="id" width="200">流程编号</th>
				<th field="name" width="200">流程名称</th>
				<th field="key" width="150">流程KEY</th>
				<th field="version" width="60">流程版本</th>
				<th field="diagramResourceName" width="200">流程图片名称</th>
				<th field="resourceName" width="200">流程文件名称</th>
				<th field="deploymentId" width="80">流程部署编号</th>
				<th field="description" width="200">流程描述</th>
			</tr>
		</thead>
	</table>
	<div id="toolbar" class="hidden-label">
		<button class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="readDiagram();">查看流程图</button>
	</div>

</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>流程部署</title>
<%@ include file="/commons/base.jsp"%>
<script type="text/javascript">
	$(function() {
		$('#dataGrid').datagrid({
			title : '流程部署列表',
			width : 700, //高度
			height : 'auto', //宽度自动
			iconCls : "icon-edit", //图标
			loadMsg : "正在加载数据...",
			method : "post",
			url : basePath + "/admin/deploy/deployList",
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

	function formatterDate(value, row, index) {
		return TimeObjectUtil.longMsTimeConvertToDateTime(value);
	}

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
</script>
</head>
<body>
	<table id="dataGrid" class="easyui-datagrid hidden-label" toolbar="#toolbar">
		<thead>
			<tr>
				<th field="name" width="200">流程名称</th>
				<th field="deploymentTime" formatter="formatterDate" width="150">流程时间</th>
			</tr>
		</thead>
	</table>
	<div id="toolbar" class="hidden-label">
		<button class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="editGrid('#dataGrid', '#editDialog', '#formId', '', '/admin/deploy/add')">添加</button>
		<button class="easyui-linkbutton" iconCls="icon-remove" plain="true"
			onclick="remove('#dataGrid', '/admin/deploy/remove');">删除</button>
	</div>

	<div id="editDialog" class="easyui-dialog hidden-label" style="width: 400px; height: 280px; padding: 10px 20px;"
		closed="true" buttons="#dlg-buttons">
		<form id="formId" method="post" enctype="multipart/form-data">
			<div class="fitem">
				<label>上传文件:</label>
				<input type="file" name="file" class="easyui-validatebox" onchange="validateFile(this);">
			</div>
		</form>
	</div>
	<div id="dlg-buttons" class="hidden-label">
		<button class="easyui-linkbutton" iconCls="icon-ok" onclick="saveAndUpdate('#formId')">保存</button>
		<button class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#editDialog').dialog('close')">取消</button>
	</div>
</body>
</html>
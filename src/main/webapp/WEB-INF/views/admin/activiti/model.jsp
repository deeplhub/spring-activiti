<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>流程模列表</title>
<%@ include file="/commons/base.jsp"%>
<script type="text/javascript">
	$(function() {
		$('#dataGrid').datagrid({
			title : '流程模型列表',
			width : 700, //高度
			height : 'auto', //宽度自动
			iconCls : "icon-edit", //图标
			loadMsg : "正在加载数据...",
			method : "post",
			url : basePath + "/admin/model/modelList",
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
		if(value != null){
			return TimeObjectUtil.longMsTimeConvertToDateTime(value);
		}
		return null;
	}

	function successCallback(result) {
		if (result.code == 0) {
			$("#editDialog").dialog('close');
			$("#dataGrid").datagrid('reload');
			$.messager.show({
				title : 'Success',
				msg : result.msg
			});
			if (result.obj != null) {
				window.open(basePath + "/admin/model/open/" + result.obj + "/view");
			}
		} else {
			$.messager.show({
				title : 'Error',
				msg : result.msg
			});
		}
	}

	function editModel() {
		var row = $("#dataGrid").datagrid('getSelected');
		if (row) {
			window.open(basePath + "/admin/model/open/" + row.id + "/view");
		}
	}
	
	function deploy(){
		var row = $("#dataGrid").datagrid('getSelected');
		if (row) {
			var data = {
					url:basePath + "/admin/model/deploy",
					data:{
						paramId: row.id
					},
					returnType:"json"
			};
			setting.post(data);
		}
	}
	
	function exportModel(type){
		var row = $("#dataGrid").datagrid('getSelected');
		if (row) {
			window.location.href = basePath + "/admin/model/export/" + row.id + "/" + type;
		}
	}
</script>
</head>
<body>
	<table id="dataGrid" class="easyui-datagrid hidden-label" toolbar="#toolbar">
		<thead>
			<tr>
				<th field="name" width="200">名称</th>
				<th field="key" width="200">KEY</th>
				<th field="version" width="70">版本</th>
				<th field="deployStatus" width="150">部署状态</th>
				<th field="createTime" formatter="formatterDate" width="150">创建时间</th>
				<th field="lastUpdateTime" formatter="formatterDate" width="150">最新修改时间</th>
				<th field="deploymentTime" formatter="formatterDate" width="150">部署时间</th>
			</tr>
		</thead>
	</table>
	<div id="toolbar" class="hidden-label">
		<button class="easyui-linkbutton" iconCls="icon-add" plain="true"
			onclick="editGrid('#dataGrid', '#editDialog', '#formId', '', '/admin/model/add')">添加</button>
		<button class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editModel()">编辑</button>
		<button class="easyui-linkbutton" iconCls="icon-remove" plain="true"
			onclick="remove('#dataGrid', '/admin/model/remove')">删除</button>
		<button class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="deploy();">部署</button>
		<button class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="exportModel('bpmn');">导出Bpmn</button>
		<button class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="exportModel('xml');">导出XML</button>
		<button class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="exportModel('json');">导出JSON</button>
	</div>


	<div id="editDialog" class="easyui-dialog hidden-label" style="width: 400px; height: 280px; padding: 10px 20px;"
		closed="true" buttons="#dlg-buttons">
		<form id="formId" method="post">
			<div class="fitem">
				<label>名称:</label>
				<input name="name" class="easyui-validatebox">
			</div>
			<div class="fitem">
				<label>KEY:</label>
				<input name="key" class="easyui-validatebox">
			</div>
			<div class="fitem">
				<label>描述:</label>
				<input name="description" class="easyui-validatebox">
			</div>
		</form>
	</div>
	<div id="dlg-buttons" class="hidden-label">
		<button class="easyui-linkbutton" iconCls="icon-ok" onclick="saveAndUpdate('#formId')">保存</button>
		<button class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#editDialog').dialog('close')">取消</button>
	</div>
</body>
</html>
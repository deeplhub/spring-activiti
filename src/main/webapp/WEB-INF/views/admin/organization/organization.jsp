<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>部门</title>
<%@ include file="/commons/base.jsp"%>
<script type="text/javascript">
	$(function () {
		$('#treeGrid').treegrid({
			title: "资源列表",
			width: "100%",
			height: "auto",
			iconCls: "icon-edit", //图标
			loadMsg: "正在加载数据...",
			method: "post",
			url: basePath + "/admin/organization/treeGrid",
			striped: true, //设置为true将交替显示行背景。
			idField: 'id',
			treeField: 'name',
			parentField: 'pid',
			animate: true, //定义当节点展开或折叠时是否显示动画效果。
			lines: true, //线型
			rownumbers: true, //行号
			fit: true, //自动大小
			fitColumns: false,
			border: true
		});
	});
	function formatterDate(value, row, index) {
		return TimeObjectUtil.longMsTimeConvertToDateTime(value);
	}
	
	function edit(flag) {
		var row = $('#treeGrid').datagrid('getSelected');
		if(row){
			if(flag){
				$("#childTitle").removeClass("ftitle");
				$("#childTitle").html("");
				$("#pid").val(null);
				
				$('#editDialog').dialog('open').dialog('setTitle', '修改');
				$('#formId').form('load', row);
				$("#keyId").val(row.id);
				
				baseUrl = "/admin/organization/edit";
			}else{
				$("#childTitle").addClass("ftitle");
				$("#childTitle").html(row.name);
				$("#pid").val(row.id);
				
				$('#editDialog').dialog('open').dialog('setTitle', '添加');
				$('#formId').form('clear');
				
				baseUrl = "/admin/organization/add";
			}
		}else{
			$("#childTitle").removeClass("ftitle");
			$("#childTitle").html("");
			$("#pid").val(null);
			
			$('#editDialog').dialog('open').dialog('setTitle', '添加');
			$('#formId').form('clear');
			
			baseUrl = "/admin/organization/add";
		}
	}
	
	
	function successCallback(result){
		if (result.code == 0) {
				$("#editDialog").dialog('close'); // close the dialog
				$("#treeGrid").treegrid('reload'); // reload the user data
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
	<table id="treeGrid" class="easyui-treegrid hidden-label" toolbar="#toolbar">
		<thead>
			<tr>
				<th field="name" width="180">组织名</th>
				<th field="address" width="200">地址</th>
				<th field="code" width="80">编号</th>
				<th field="seq" width="80">排序</th>
				<th field="createTime" formatter="formatterDate" width="150">创建时间</th>
			</tr>
		</thead>
	</table>

	<div id="toolbar" class="hidden-label">
		<button class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="edit(false)">新建</button>
		<button class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="edit(true)">修改</button>
		<button class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="remove('#treeGrid', '/admin/organization/remove')">删除</button>
	</div>
	
	<div id="editDialog" class="easyui-dialog hidden-label" style="width: 350px; height: 250px; padding: 10px 20px"; closed="true"
		buttons="#dlg-buttons">
		<div id="childTitle"></div>
		<form id="formId" method="post">
			<input id="keyId" type="hidden" name="id" >
			<input id="pid" type="hidden" name="pid" >
			<div class="fitem">
				<label>组织名：</label>
				<input name="name" class="easyui-validatebox" required="true">
			</div>
			<div class="fitem">
				<label>地址：</label>
				<input name="address" class="easyui-validatebox" required="true">
			</div>
			<div class="fitem">
				<label>编号：</label>
				<input name="code">
			</div>
			<div class="fitem">
				<label>排序：</label>
				<input name="seq">
			</div>
		</form>
	</div>
	<div id="dlg-buttons" class="hidden-label">
		<button class="easyui-linkbutton" iconCls="icon-ok" onclick="saveAndUpdate('#formId')">保存</button>
		<button class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#editDialog').dialog('close')">取消</button>
	</div>
	
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>请假申请列表</title>
<%@ include file="/commons/base.jsp"%>

<script type="text/javascript">
	$(function () {
		$('#dataGrid').datagrid({
			title: '请假申请列表',
			width: 700, //高度
			height: 'auto', //宽度自动
			iconCls: "icon-edit", //图标
			loadMsg: "正在加载数据...",
			method: "post",
			url: basePath + "/leave/dataGrid",
			//sortName: 'createTime',
			//sortOrder: 'desc',
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
	
	});
	
	function successCallback(result){
		if (result.code == 0) {
			$("#editDialog").dialog('close');
			$("#dataGrid").datagrid('reload');
			$.messager.show({
				title: 'Success',
				msg: result.msg
			});
		} else {
			$.messager.show({
				title: 'Error',
				msg: result.msg
			});
		}
	}
	
	
	function edit(obj) {
		var row = $("#dataGrid").datagrid('getSelected');
		if (row && keyId != "" && obj == true) {
			$('#editDialog').dialog('open').dialog('setTitle', '修改');
			row.startDate = TimeObjectUtil.longMsTimeConvertToDateTime(row.startDate);
			row.endDate = TimeObjectUtil.longMsTimeConvertToDateTime(row.endDate);
			$('#formId').form('load', row);
			$('#keyId').val(row.id);
			setting.url = '/leave/edit';
		} else {
			$('#editDialog').dialog('open').dialog('setTitle', '添加');
			$('#formId').form('clear');
			setting.url = '/leave/add';
		}
	}
	
	function submitApplication(){
		var row = $("#dataGrid").datagrid('getSelected');
		if (row) {
			var data = {
					url:basePath + "/leave/submitApplication",
					data:{
						paramId:row.id
					},
					returnType:"json"
			}
			setting.post(data);
		}
	}
	
	function viewApplication(){
		var row = $("#dataGrid").datagrid('getSelected');
		if (row) {
			$('#tableDialog').dialog('open').dialog('setTitle', '查看历史批注列表');
			$('#commentGrid').datagrid({
				width: "auto", //高度
				height: 'auto', //宽度自动
				iconCls: "icon-edit", //图标
				loadMsg: "正在加载数据...",
				method: "GET",
				url: basePath + "/leave/historyCommentGrid/"+row.processInstanceId,
				nowrap: false,
				striped: true,
				border: true,
				collapsible: false, //是否可折叠的
				fit: true, //自动大小
				remoteSort: false
			});
		}
	}
	
</script>
</head>
<body>

	<table id="dataGrid" class="easyui-datagrid hidden-label" toolbar="#toolbar">
		<thead>
			<tr>
				<th field="content" width="300">请假内容</th>
				<th field="days" width="60">请假天数</th>
				<th field="state" formatter="formatterLeaveState" width="100">请假状态</th>
				<th field="approvalInfo" width="100">审批信息</th>
				<th field="remark" width="300">备注</th>
				<th field="startDate" formatter="formatterDate" width="150">开始时间</th>
				<th field="endDate" formatter="formatterDate" width="150">结束时间</th>
				<!-- <th field="createDate" formatter="formatterDate" width="150">创建时间</th> -->
			</tr>
		</thead>
	</table>
	
	<div id="toolbar" class="hidden-label">
		<button class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="edit(false)">添加</button>
		<button class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="edit(true)">编辑</button>
		<button class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="remove('#dataGrid', '/leave/remove')">删除</button>
		<button class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="submitApplication();">提交申请</button>
		<button class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="viewApplication();">查看申请</button>
		<!-- <button class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="">撤回申请</button> -->
	</div>
	
	<!-- 添加或修改 -->
	<div id="editDialog" class="easyui-dialog hidden-label" style="width:380px; height:400px; padding:10px 20px;"
		closed="true" buttons="#dlg-buttons">
		<div class="ftitle"></div>
		<form id="formId" method="post">
			<input id="keyId" name="id" type="hidden">
			<div class="fitem">
				<label>请假天数：</label>
				<input name="days" class="easyui-validatebox" required>
			</div>
			<div class="fitem">
				<label>开始时间：</label>
				<input name="startDate" class="easyui-datetimebox" required>
			</div>
			<div class="fitem">
				<label>结束时间：</label>
				<input name="endDate" class="easyui-datetimebox"  required>
			</div>
			<div class="fitem">
				<label>请假内容：</label>
				<input name="content" class="easyui-validatebox" required>
			</div>
			<div class="fitem">
				<label>备注：</label>
				<input name="remark" class="easyui-validatebox" required>
			</div>
		</form>
	</div>
	<div id="dlg-buttons" class="hidden-label">
		<button class="easyui-linkbutton" iconCls="icon-ok" onclick="saveAndUpdate('#formId');">保存</button>
		<button class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#editDialog').dialog('close')">取消</button>
	</div>
	
	<!-- 查看申请 -->
	<div id="tableDialog" class="easyui-dialog hidden-label" style="width: 586px; height: 400px; padding: 10px 20px;" closed="true">
		<table id="commentGrid" class="easyui-datagrid hidden-label">
			<thead>
				<tr>
					<th field="userId" width="80">批注人</th>
					<th field="message" width="300">批注信息</th>
					<th field="time" formatter="formatterDate" width="150">批注时间</th>
				</tr>
			</thead>
		</table>
	</div>
</body>
</html>
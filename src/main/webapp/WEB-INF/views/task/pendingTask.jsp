<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>待办任务管理页</title>
<%@ include file="/commons/base.jsp"%>

<script type="text/javascript">
	$(function() {
		$('#dataGrid').datagrid({
			title : '待办任务列表',
			width : 700, //高度
			height : 'auto', //宽度自动
			iconCls : "icon-edit", //图标
			loadMsg : "正在加载数据...",
			method : "post",
			url : basePath + "/task/pendingGrid",
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
	
	function successCallback(result){
		if (result.code == 0) {
			$("#approvalDialog").dialog('close');
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

	function approval() {
		var row = $("#dataGrid").datagrid('getSelected');
		debugger;
		if (row) {
			$('#approvalDialog').dialog('open').dialog('setTitle', '查看审批');
			$("#keyId").val(row.id);
			$("#keyTaskId").val(row.tid);
			setting.url = "/task/approvalProcess";
		}
	}
</script>
</head>
<body>


	<table id="dataGrid" class="easyui-datagrid hidden-label" toolbar="#toolbar">
		<thead>
			<tr>
				<th field="tid" width="100">编号</th>
				<th field="userName" width="100">请假人</th>
				<th field="content" width="300">请假内容</th>
				<th field="days" width="60">请假天数</th>
				<th field="approvalInfo" width="100">上一审批人</th>
				<th field="state" formatter="formatterLeaveState" width="60">请假状态</th>
				<th field="createTime" formatter="formatterDate" width="150">创建时间</th>
			</tr>
		</thead>
	</table>
	<div id="toolbar" class="hidden-label">
		<button class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="approval('', '')">审批</button>
	</div>

	<!-- 查看申请 -->
	<div id="approvalDialog" class="easyui-dialog hidden-label" style="width: 586px; height: 400px; padding: 10px 20px;" closed="true"
		buttons="#dlg-buttons">
		<div class="ftitle"></div>
		<form id="formId" method="post">
			<input id="keyId" name="paramId" type="text">
			<input id="keyTaskId" name="taskId" type="text">
			<div class="fitem">
				<label>批注：</label>
				<input name="message" class="easyui-validatebox" required>
			</div>
		</form>
	</div>
	<div id="dlg-buttons" class="hidden-label">
		<button class="easyui-linkbutton" iconCls="icon-ok" onclick="saveAndUpdate('#formId');">批准</button>
		<button class="easyui-linkbutton" iconCls="icon-ok" onclick="">驳回</button>
		<button class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#editDialog').dialog('close')">取消</button>
	</div>
</body>
</html>
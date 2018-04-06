<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>角色</title>
<%@ include file="/commons/base.jsp"%>
<script type="text/javascript">
	$(function() {
		$('#dataGrid').datagrid({
			title : '角色管理列表',
			width : 700, //高度
			height : 'auto', //宽度自动
			iconCls : "icon-edit", //图标
			loadMsg : "正在加载数据...",
			method : "post",
			url : basePath + "/admin/role/dataGrid",
			sortName : 'seq',
			sortOrder : 'desc',
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

		//设置分页控件 
		var p = $('#dataGrid').datagrid('getPager');
		$(p).pagination({
			pageSize : 10,//每页显示的记录条数，默认为10 
			pageList : [ 10, 20, 35, 40, 50 ],//可以设置每页记录条数的列表 
			beforePageText : '第',//页数文本框前显示的汉字 
			afterPageText : '页    共 {pages} 页',
			displayMsg : '当前显示 {from} - {to} 条记录   共 {total} 条记录',
		});
	});
	
	function successCallback(result){
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
	
	function authorized(){
		var row = $('#dataGrid').datagrid('getSelected');
		if(row){
			$('#authorizedDialog').dialog('open').dialog('setTitle', '授权');
			setting.async.url = basePath + '/admin/resource/selectAuthorized?paramId='+row.id;
			setting.check.enable = true;
			setting.callback = {};
			$.fn.zTree.init($("#authorizedTree"), setting);
			
			$("#roleId").val(row.id);
		}
	}
	
	function saveAuthorized(){
		var zTree = $.fn.zTree.getZTreeObj("authorizedTree");
		var nodes = zTree.getCheckedNodes(true);
		var nodeId = "";
		for(var i=0;i<nodes.length;i++){
			nodeId += nodeId = nodes[i].id + ",";
		}
		nodeId = nodeId.substring(0, nodeId.length - 1);
		
		var data = {
			url:basePath + "/admin/role/updateRoleResourceAuthorized",
			data:{
				paramId:$("#roleId").val(),
				resourceIds:nodeId
			},
			returnType:"json"
		};
		setting.post(data);
		
		$("#authorizedDialog").dialog('close');
		$("#dataGrid").datagrid('reload');
	}
	
</script>
</head>
<body>
	<table id="dataGrid" class="easyui-datagrid hidden-label" toolbar="#toolbar">
		<thead>
			<tr>
				<th field="name" width="100">角色名</th>
				<th field="description" width="200">简介</th>
				<th field="seq" width="80">排序号</th>
				<th field="status" formatter="status" width="100">状态</th>
			</tr>
		</thead>
	</table>
	<div id="toolbar" class="hidden-label">
		<button class="easyui-linkbutton" iconCls="icon-add" plain="true"
			onclick="editGrid('#dataGrid', '#editOneDialog', '#formOneId', '', '/admin/role/add')">添加</button>
		<button class="easyui-linkbutton" iconCls="icon-edit" plain="true"
			onclick="editGrid('#dataGrid', '#editOneDialog', '#formOneId', '#keyId', '/admin/role/edit')">编辑</button>
		<button class="easyui-linkbutton" iconCls="icon-remove" plain="true"
			onclick="remove('#dataGrid', '/admin/role/remove')">删除</button>
		<button class="easyui-linkbutton" iconCls="icon-man" plain="true" onclick="authorized()">授权</button>
	</div>

	<div id="editOneDialog" class="easyui-dialog hidden-label" style="width: 400px; height: 280px; padding: 10px 20px;"
		closed="true" buttons="#dlg-buttons">
		<div class="ftitle"></div>
		<form id="formOneId" method="post">
			<input id="keyId" name="id" type="hidden">
			<div class="fitem">
				<label>角色名:</label>
				<input name="name" class="easyui-validatebox" required="true">
			</div>
			<div class="fitem">
				<label>状态:</label>
				<!-- <input name="status" class="easyui-validatebox" required="true"> -->
				<select class="easyui-combobox" name="status" panelHeight="auto" style="width:172px;" required="true">
					<option value="0">正常</option>
					<option value="1">禁用</option>
				</select>
			</div>
			<div class="fitem">
				<label>排序:</label>
				<input name="seq" class="easyui-validatebox" required="true">
			</div>
			<div class="fitem">
				<label>简介:</label>
				<input name="description" class="easyui-validatebox">
			</div>
		</form>
	</div>
	<div id="dlg-buttons" class="hidden-label">
		<button class="easyui-linkbutton" iconCls="icon-ok" onclick="saveAndUpdate('#formOneId')">保存</button>
		<button class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#editOneDialog').dialog('close')">取消</button>
	</div>

	<div id="authorizedDialog" class="easyui-dialog hidden-label" style="width:275px; height: 380px; padding: 10px 20px"
		closed="true" buttons="#authorizedDialog-buttons">
		<form id="formTwoId" method="post">
			<input id="roleId" name="paramId" type="hidden">
			<div class="fitem">
				<label>权限：</label>
				<ul id="authorizedTree" class="ztree" style="margin-left:16px;height:243px;width:162px;overflow: auto;"></ul>
			</div>
		</form>
	</div>
	<div id="authorizedDialog-buttons" class="hidden-label">
		<button class="easyui-linkbutton" iconCls="icon-ok" onclick="saveAuthorized('#formTwoId')">保存</button>
		<button class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#authorizedDialog').dialog('close')">取消</button>
	</div>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>用户</title>
<%@ include file="/commons/base.jsp"%>
<link rel="stylesheet" type="text/css" href="${staticPath }/static/css/main.css">
<link rel="stylesheet" type="text/css" href="${staticPath }/static/zTree/css/demo.css">
<style type="text/css">
.window {
	overflow: visible;
}
</style>
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
	
	function edit(grid, dialog, formId, keyId, url) {
		baseUrl = url;
		var row = $(grid).datagrid('getSelected');
		if (row && keyId != "") {
			$(dialog).dialog('open').dialog('setTitle', '修改');
			$(formId).form('load', row);
			$(keyId).val(row.id);
		} else {
			$(dialog).dialog('open').dialog('setTitle', '添加');
			$(formId).form('clear');
		}
		
		setting.async.url = basePath + '/admin/organization/tree';
		setting.check.enable = true;
		setting.check.chkStyle = "radio";
		setting.check.radioType = "all";
		setting.callback = {
				onClick: function(e, treeId, treeNode) {
					var zTree = $.fn.zTree.getZTreeObj("organizationTree");
					var nodes = zTree.getSelectedNodes();
					zTree.checkNode(nodes[0], true, true);
					$("#keyOrganizationId").val(nodes[0].id);
					$("#organizationNameId").val(nodes[0].name);
				},
				onCheck:function(e, treeId, treeNode){
					var zTree = $.fn.zTree.getZTreeObj("organizationTree");
					var nodes = zTree.getCheckedNodes(true);
					$("#keyOrganizationId").val(nodes[0].id);
					$("#organizationNameId").val(nodes[0].name);
				}
		};
		$.fn.zTree.init($("#organizationTree"), setting);
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
	
	function showTree(){
		//方法通过使用滑动效果，显示隐藏的被选元素。
		$("#menuContent").slideDown("fast");
		//鼠标点击事件
		$("body").bind("mousedown", onBodyDown);
	}
	
	function onBodyDown(event) {
		if (!(event.target.id == "organizationNameId" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length>0)) {
			//隐藏
			$("#menuContent").fadeOut("fast");
			$("body").unbind("mousedown", onBodyDown);
		}
	}
	
	function authorized(){
		var row = $('#dataGrid').datagrid('getSelected');
		if(row){
			$('#authorizedDialog').dialog('open').dialog('setTitle', '授权');
			setting.async.url = basePath + '/admin/role/selectAuthorized?paramId='+row.id;
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
			url:basePath + "/admin/role/updateUserRoleAuthorized",
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
		<button class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="edit('#dataGrid', '#editOneDialog', '#formOneId', '', '/admin/user/add')">添加</button>
		<button class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="edit('#dataGrid', '#editOneDialog', '#formOneId', '#keyId', '/admin/user/edit')">编辑</button>
		<button class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="remove('#dataGrid', '/admin/user/remove')">删除</button>
		<button class="easyui-linkbutton" iconCls="icon-man" plain="true" onclick="authorized()">授权</button>
	</div>
	
	<div id="editOneDialog" class="easyui-dialog hidden-label" style="width:380px; height:400px; padding:10px 20px;"
		closed="true" buttons="#dlg-buttons">
		<div class="ftitle"></div>
		<form id="formOneId" method="post">
			<input id="keyId" name="id" type="hidden">
			<input id="keyOrganizationId" name="organizationId" type="hidden"/>
			<div class="fitem">
				<label>登陆名：</label>
				<input name="loginName" class="easyui-validatebox" required>
			</div>
			<div class="fitem">
				<label>用户名：</label>
				<input name="name" class="easyui-validatebox" required>
			</div>
			<div id="password-html" class="fitem">
				<label>密码：</label>
				<input id="password" type="password" name="password" class="easyui-validatebox" >
			</div>
			<div class="fitem">
				<label>性别：</label>
				<input name="sex" class="easyui-validatebox" required>
			</div>
			<div class="fitem">
				<label>年龄：</label>
				<input name="age" class="easyui-validatebox" required>
			</div>
			<div class="fitem">
				<label>手机号：</label>
				<input name="phone" class="easyui-validatebox">
			</div>
			<div class="fitem">
				<label>用户类别：</label>
				<input name="userType" class="easyui-validatebox" required>
			</div>
			<div class="fitem">
				<label>用户状态：</label>
				<input name="status" class="easyui-validatebox" required>
			</div>
			<div class="fitem">
				<label>所属机构：</label>
				<input id="organizationNameId" name="organizationName" class="easyui-validatebox" onclick="showTree();" readonly="readonly" required/>
				<div id="menuContent" class="menuContent" style="display:none; position:absolute; z-index:9002;">
					<ul id="organizationTree" class="ztree" style="border: 1px solid #000000; height: 107px;width:162px; margin-left: 84px;margin-top: -1px;overflow: auto;"></ul>
				</div>
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
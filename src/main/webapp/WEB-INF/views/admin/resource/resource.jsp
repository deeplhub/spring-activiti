<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>首页</title>
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
			url: basePath + "/admin/resource/treeGridAll",
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
	
	function opened(value, row, index) {
		return (value == 1) ? "打开" : "关闭";
	}
	function formatterDate(value, row, index) {
		return TimeObjectUtil.longMsTimeConvertToDateTime(value);
	}
	
	function status(value, row, index) {
		return (value == 0) ? "正常" : "停用";
	}
	function resourceType(value, row, index) {
		return (value == 0) ? "菜单" : "按钮";
	}

	
	function edit(flag) {
		var row = $('#treeGrid').datagrid('getSelected');
		if(row){
			if(flag){
				//更新
				$("#childTitle").removeClass("ftitle");
				$("#childTitle").html("");
				$("#pid").val();
				
				$('#editDialog').dialog('open').dialog('setTitle', '修改');
				$('#formId').form('load', row);
				$("#keyId").val(row.id);
				
				baseUrl = "/admin/resource/edit";
				return true;
			}
			//添加子级
			$('#formId').form('clear');
			$("#childTitle").addClass("ftitle");
			$("#childTitle").html(row.name);
			$("#pid").val(row.id);
			comboboxDefault();
			
			$('#editDialog').dialog('open').dialog('setTitle', '添加');
			
			baseUrl = "/admin/resource/add";
			return true;
		}
		//添加
		$("#childTitle").addClass("ftitle");
		$("#childTitle").html();
		$("#pid").val();
		
		$('#editDialog').dialog('open').dialog('setTitle', '添加');
		$('#formId').form('clear');
		comboboxDefault();
		
		baseUrl = "/admin/resource/add";
	}
	
	function comboboxDefault(){
		$('#openMode').combobox({    
		    valueField:'ajax',
		    textField:'ajax',
		});
		$('#status').combobox({    
		    valueField:'0',
		    textField:'正常',
		});
		$('#opened').combobox({    
		    valueField:'1',
		    textField:'打开',
		});
		$('#resourceType').combobox({    
		    valueField:'1',
		    textField:'按钮',
		});
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
				<th field="name" width="180">资源名称</th>
				<th field="url" width="200">资源路径</th>
				<th field="openMode" width="80">打开方式</th>
				<!-- <th field="icon" width="120">资源图标</th> -->
				<th field="seq" width="80">排序</th>
				<th field="status" formatter="status" width="80">状态</th>
				<th field="opened" formatter="opened" width="80">打开状态</th>
				<th field="resourceType" formatter="resourceType" width="80">资源类别</th>
				<th field="description" width="100">资源介绍</th>
				<th field="createTime" formatter="formatterDate" width="150">创建时间</th>
			</tr>
		</thead>
	</table>

	<div id="toolbar" class="hidden-label">
		<button class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="edit(false)">新建</button>
		<button class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="edit(true)">修改</button>
		<button class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="remove('#treeGrid', '/admin/resource/remove')">删除</button>
	</div>

	<div id="editDialog" class="easyui-dialog hidden-label" style="width: 450px; height: 370px; padding: 10px 20px"; closed="true"
		buttons="#dlg-buttons">
		<div id="childTitle"></div>
		<form id="formId" method="post">
			<input id="keyId" type="hidden" name="id" >
			<input id="pid" type="hidden" name="pid" >
			<div class="fitem">
				<label>资源名称：</label>
				<input name="name" class="easyui-validatebox" required="true">
			</div>
			<div class="fitem">
				<label>资源路径：</label>
				<input name="url" class="easyui-validatebox">
			</div>
			<div class="fitem">
				<label>资源介绍：</label>
				<input name="description" required="true">
			</div>
			<div class="fitem">
				<label>打开方式：</label>
				<!-- <input id="combobox" name="openMode"> -->
				<select id="openMode" class="easyui-combobox" name="openMode" panelHeight="auto" style="width:172px;" required="true">
					<option value="ajax">ajax</option>
					<option value="iframe">iframe</option>
				</select>
			</div>
			<div class="fitem">
				<label>状态：</label>
				<!-- <input name="status"> -->
				<select id="status" class="easyui-combobox" name="status" panelHeight="auto" style="width:172px;" required="true">
					<option value="0">正常</option>
					<option value="1">禁用</option>
				</select>
			</div>
			<div class="fitem">
				<label>打开状态：</label>
				<!-- <input name="opened"> -->
				<select id="opened" class="easyui-combobox" name="opened" panelHeight="auto" style="width:172px;" required="true">
					<option value="1">打开</option>
					<option value="0">关闭</option>
				</select>
			</div>
			<div class="fitem">
				<label>资源类别：</label>
				<!-- <input name="resourceType"> -->
				<select id="resourceType" class="easyui-combobox" name="resourceType" panelHeight="auto" style="width:172px;" required="true">
					<option value="1">按钮</option>
					<option value="0">菜单</option>
				</select>
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

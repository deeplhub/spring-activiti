//zTree start
var setting = {
	async: {
		enable: true,
		url: basePath + '/admin/resource/homeTree',
		autoParam: ["id"],
		otherParam: {
			"type": "checkbox"
		},
		dataFilter: filter
	},
	view: {
		dblClickExpand: true
	},
	check:{
		enable: false,
		chkStyle: "checkbox"
	},
	data: {
		simpleData: {
			enable: true
		}
	},
	callback: {
		onClick: onClick,
		onAsyncSuccess: zTreeOnAsyncSuccess
	},
	
	//自定义参数
	post:function(data){
		$.post(data.url, data.data, function(result){
			successCallback(result);
		}, data.returnType);
	}
}

function filter(treeId, parentNode, childNodes) {
	if (!childNodes)
		return null;
	for (var i = 0, l = childNodes.length; i < l; i++) {
		childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
	}
	return childNodes;
}

function onClick(e, treeId, treeNode) {
	if (treeNode.attributes != "") {
		$("#iframe").attr("src", basePath + treeNode.attributes);
	}
}

//只展开第一个菜单
var isFirst = true;
function zTreeOnAsyncSuccess(event, treeId, treeNode, msg) {
	if (isFirst) {
		//获得树形图对象
		var zTree = $.fn.zTree.getZTreeObj("treeDemo");
		//获取根节点个数,getNodes获取的是根节点的集合
		var nodeList = zTree.getNodes();
		//展开第一个根节点
		zTree.expandNode(nodeList[0], true);
		//当再次点击节点时条件不符合,直接跳出方法
		isFirst = false;
	}
}
//zTree end

//编辑（添加、修改）
var baseUrl = null;
function editGrid(grid, dialog, formId, keyId, url) {
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
}

//执行保存和更新
function saveAndUpdate(fromId) {
	if(baseUrl == null){
		return false;
	}
	$(fromId).form('submit', {
		url: basePath + baseUrl,
		onSubmit: function () {
			return $(this).form('validate');
		},
		success: function(result){
			successCallback(eval('(' + result + ')'));
		}
	});
}

//根据ID删除数据
function remove(grid, url) {
	//获取选中数据
	var row = $(grid).datagrid('getSelected');
	if (row) {
		$.messager.confirm("删除", "您确定删除 [" + row.name + "] 吗?", function (res) {
			if (res) {
				var data = {
					url:basePath + url,
					data:{
						paramId:row.id
					},
					returnType:"json"
				};
				setting.post(data);
			}
		});
	}
}


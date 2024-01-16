/**
 * 根据供应商名模糊查询供应商信息
 */
function searchTakeStock() {

	$('#dg').datagrid('load', {
		takeStockName: $('#number').val()
	});
}

var url;

/**
 * 打开新增供应商窗口
 */
function openTakeStockAddDialog() {
	$('#dlg').dialog({
		title: '添加供应商',
		iconCls: 'add',
		closed: false,
		top: $(window).height() / 4,
		width: 500,
		height: 350,
		onClose: function () {
			$('#name').val('');
			$('#contacts').val('');
			$('#phoneNumber').val('');
			$('#address').val('');
			$('#remarks').val('');
		}
	});

	url = "/takeStock/save";
}

/**
 * 打开修改供应商窗口
 */
function openTakeStockModifyDialog() {
	var selections = $('#dg').datagrid('getSelections');
	if (selections.length < 1) {
		$.messager.alert({
			title: '系统提示',
			msg: '请选择一条您要修改的记录',
			icon: 'error',
			top: $(window).height() / 4
		});
		return;
	}
	//加载数据至表单
	$('#fm').form('load', selections[0]);
	//设置窗口相关属性，并打开
	$('#dlg').dialog({
		title: '修改供应商',
		iconCls: 'update',
		closed: false,
		top: $(window).height() / 4,
		width: 500,
		height: 350,
		onClose: function () {
			$('#name').val('');
			$('#contacts').val('');
			$('#phoneNumber').val('');
			$('#address').val('');
			$('#remarks').val('');
		}
	});

	url = "/takeStock/save?takeStockId=" + selections[0].takeStockId;
}

/**
 * 关闭窗口
 */
function closeDlg() {
	$('#name').val('');
	$('#contacts').val('');
	$('#phoneNumber').val('');
	$('#address').val('');
	$('#remarks').val('');
	$('#dlg').dialog('close');
}

$(function () {
	//数据表格加载完毕后，绑定双击打开修改窗口事件
	$('#dg').datagrid({
		onDblClickRow: function (index, row) {
			//加载数据至表单
			$('#fm').form('load', row);
			$('#dlg').dialog({
				title: '修改供应商',
				iconCls: 'update',
				closed: false,
				top: $(window).height() / 4,
				width: 500,
				height: 350,
				onClose: function () {
					$('#name').val('');
					$('#contacts').val('');
					$('#phoneNumber').val('');
					$('#address').val('');
					$('#remarks').val('');
				}
			});

			url = "/takeStock/save?takeStockId=" + row.takeStockId;
		}
	})
});

/**
 * 保存供应商信息
 */
function saveData() {
	$('#fm').form('submit', {
		url: "/takeStock/save",
		onSubmit: function () {
			//表单验证
			return true;
		},
		success: function (result) {
			var resultJson = eval('(' + result + ')');
			if (resultJson.code === 100) {
				$.messager.alert({
					title: '系统提示',
					msg: '保存成功',
					icon: 'info',
					top: $(window).height() / 4
				});
				$('#dlg').dialog('close');
				$('#dg').datagrid('reload');
			} else {
				$.messager.alert({
					title: '系统提示',
					msg: resultJson.msg,
					icon: 'error',
					top: $(window).height() / 4
				});
			}
		}
	})
}

/**
 * 删除用户信息
 */
function deleteTakeStock() {
	var selections = $('#dg').datagrid('getSelections');
	if (selections.length < 1) {
		$.messager.alert({
			title: '系统提示',
			msg: '请选择您要删除的记录',
			icon: 'error',
			top: $(window).height() / 4
		});
		return;
	}
	$.messager.confirm({
		title: '系统提示',
		msg: '您确定要删除这' + selections.length + '条记录吗？',
		top: $(window).height() / 4,
		fn: function (r) {
			if (r) {
				var idsAr = [];
				for (var i = 0; i < selections.length; i++) {
					idsAr.push(selections[i].takeStockId);
				}
				var ids = idsAr.join(",");
				$.ajax({
					url: '/takeStock/delete',
					dataType: 'json',
					type: 'post',
					data: {
						'ids': ids
					},
					success: function (result) {
						if (result.code === 100) {
							$.messager.alert({
								title: '系统提示',
								msg: '删除成功',
								icon: 'info',
								top: $(window).height() / 4
							});
							$('#dg').datagrid('reload');
						} else {
							$.messager.alert({
								title: '系统提示',
								msg: result.msg,
								icon: 'error',
								top: $(window).height() / 4
							});
						}
					}
				});
			}
		}
	})
}

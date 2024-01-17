function searchTakeStock() {

	$('#dg').datagrid('load', {
        number: $('#number').val()
	});
}

var url;

$(function () {
	//数据表格加载完毕后，绑定双击打开修改窗口事件
    $('#dg').datagrid({
        onClickRow: function (index, row) {
            $('#dg2').datagrid({
                url: '/takeStockList/list',
                queryParams: {
                    takeStockId: row.id
				}
			});
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
                    idsAr.push(selections[i].id);
				}
				var ids = idsAr.join(",");
				$.ajax({
					url: '/takeStock/delete',
					dataType: 'json',
					type: 'post',
					data: {
                        'id': ids
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


/**
 * 下载
 */
function download() {
    var selections = $('#dg').datagrid('getSelections');
    if (selections.length < 1) {
        $.messager.alert({
            title: '系统提示',
            msg: '请选择您要下载的记录',
            icon: 'info',
            top: $(window).height() / 4
        });
        return;
    }
    $.messager.confirm({
        title: '系统提示',
        msg: '您确定要下载这' + selections.length + '条盘点记录吗？',
        top: $(window).height() / 4,
        fn: function (r) {
            if (r) {
                var idsAr = [];
                for (var i = 0; i < selections.length; i++) {
                    idsAr.push(selections[i].id);
                }
                var ids = idsAr.join(",");
                window.location.href = '/takeStock/exportTakeStock?id=' + ids + '';
            }
        }
    })
}


/**
 * 导入
 */
function imports() {
    var selections = $('#dg').datagrid('getSelections');
    if (selections.length < 1) {
        $.messager.alert({
            title: '系统提示',
            msg: '请选择您要导入的记录',
            icon: 'error',
            top: $(window).height() / 4
        });
        return;
    }
    $('#uploadonlineinfo').window('open').dialog('setTitle', '文件上传');
}


function uploadonline() {
    var selections = $('#dg').datagrid('getSelections');
    var idsAr = [];
    for (var i = 0; i < selections.length; i++) {
        idsAr.push(selections[i].id);
    }
    var ids = idsAr.join(",");
    $('#fam').form('submit', {
        url: "/takeStock/importTakeStock?id=" + ids + "",
        onSubmit: function () {

        },
        success: function (result) {
            var resultObj = eval('(' + result + ')');
            if (resultObj.code === 100) {
                let purchaseLists = resultObj.info.join(',');
                $('#uploadonlineinfo').window('close');
                $('#dg').datagrid('reload');
                $('#dg').datagrid({
                    url: '/takeStock/list'
                });
                $.messager.show({
                    title: 'Success',
                    msg: '上传成功'
                });
            } else {
                $.messager.show({
                    title: 'Error',
                    msg: '文件不能为空，请重新选择文件！'
                });
            }
        }
    });
}


/**
 * 打开修改供应商窗口
 */
function update() {
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
        title: '修改盘点信息',
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

    url = "/takeStock/update?id=" + selections[0].supplierId;
}

function ivFmt(value, row) {
    if (value > 0) {
        return '盘盈';
    } else if (value < 0) {
        return '盘亏';
    } else {
        return '无变化';
    }
}

function statusFmt(value, row) {
    if (value > 0) {
        return '已盘点';
    } else {
        return '未盘点';
    }
}

function priceFmt(value, row) {
    return '￥' + value;
}

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

    $('#dg2').datagrid({
        onDblClickRow: function (index, row) {
            update();
        }
    })
});

/**
 * 保存供应商信息
 */
function saveData() {
    $.ajax({
		url: "/takeStock/save",
        dataType: 'json',
        type: 'post',
		success: function (result) {
            console.log(result);
            if (result.code === 100) {
				$.messager.alert({
					title: '系统提示',
					msg: '保存成功',
					icon: 'info',
					top: $(window).height() / 4
				});
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
                $.messager.alert({
                    title: '系统提示',
                    msg: '上传成功',
                    icon: 'info',
                    top: $(window).height() / 4
                });
            } else {
                $.messager.alert({
                    title: '系统提示',
                    msg: '文件不能为空，请重新选择文件！',
                    icon: 'error',
                    top: $(window).height() / 4
                });
            }
        }
    });
}


/**
 * 打开修改供应商窗口
 */
function update() {
    var selections = $('#dg2').datagrid('getSelections');
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
        height: 450,
        onClose: function () {
            $('#countQuantity').val('');
            $('#remarks').val('');
        }
    });

    url = "/takeStockList/save";
}

function saveTakeStockList() {
    $('#fm').form('submit', {
        url: url,
        onSubmit: function () {
            if ($('#countQuantity').val() === null || $('#countQuantity').val() === '') {
                $.messager.alert({
                    title: '系统提示',
                    msg: '请输入盘点数量',
                    icon: 'error',
                    top: $(window).height() / 4
                });

                return false;
            }
            return true;
        },
        success: function (result) {
            var resultObj = eval('(' + result + ')');
            if (resultObj.code === 100) {
                $.messager.alert({
                    title: '系统提示',
                    msg: '保存成功',
                    icon: 'info',
                    top: $(window).height() / 4
                });
                $('#dlg').dialog('close');
                $('#dg2').datagrid('reload');
            } else {
                $.messager.alert({
                    title: '系统提示',
                    msg: resultObj.msg,
                    icon: 'error',
                    top: $(window).height() / 4
                });
            }
        }
    })
}

function closeTakeStockList() {
    $('#countQuantity').val('');
    $('#remarks').val('');
    $('#dlg').dialog('close');
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

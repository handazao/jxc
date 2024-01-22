$(function () {
    // 设置默认查询时间
    $('#etime').datebox('setValue', genTodayStr());
    $('#stime').datebox('setValue', genLastMonthDayStr());

    // 设置下拉框
    $('#supplier').combobox({
        mode: 'remote',
        url: '/supplier/getComboboxList',
        valueField: 'supplierId',
        textField: 'supplierName'
    });

    // 按默认查询条件进行首次查询
    $('#dg').datagrid({
        url: '/purchaseListGoods/list',
        queryParams: {
            sTime: genLastMonthDayStr(),
            eTime: genTodayStr()
        }
    });

    // 为进货单表格绑定单击事件
    $('#dg').datagrid({
        onClickRow: function (index, row) {
            $('#dg2').datagrid({
                url: '/purchaseListGoods/goodsList',
                queryParams: {
                    purchaseListId: row.purchaseListId
                }
            });
        }
    });
});

// 格式化进货金额
function amountPayableFmt(value, row) {
    return '￥' + value;
}

// 格式化付款状态
function stateFmt(value, row) {
    return value === 1 ? '已付' : '未付';
}

//重置搜索条件
function reset() {
    $('#s_purchaseNumber').val('');
    $('#supplier').combobox('setValue', '');
    $('#cc').combobox('setValue', '');
    $('#etime').datebox('setValue', genTodayStr());
    $('#stime').datebox('setValue', genLastMonthDayStr());
}

// 根据条件查询进货单信息
function search() {
    // 每次查询时，先清空进货单商品列表
    $('#dg2').datagrid('loadData', {rows: []});
    var sTime = $('#stime').datebox('getValue');
    var eTime = $('#etime').datebox('getValue');
    if (new Date(Date.parse(sTime)) > new Date(Date.parse(eTime))) {
        $.messager.alert({
            title: '系统提示',
            msg: '开始日期不能大于结束日期',
            icon: 'error',
            top: $(window).height() / 4
        });
        return;
    }
    $('#dg').datagrid({
        url: '/purchaseListGoods/list',
        queryParams: {
            purchaseNumber: $('#s_purchaseNumber').val(),
            supplierId: $('#supplier').combobox('getValue'),
            state: $('#cc').combobox('getValue'),
            sTime: sTime,
            eTime: eTime,
            type: 1
        }
    });
}

/**
 * 删除进货单
 */
function deletePurchaseList() {
    var selections = $('#dg').datagrid('getSelections');
    if (selections.length !== 1) {
        $.messager.alert({
            title: '系统提示',
            msg: '请选择一条要删除的记录',
            icon: 'info',
            top: $(window).height() / 4
        });
        return;
    }
    $.messager.confirm({
        title: '系统提示',
        msg: '<font color="red">删除进货单据将无法恢复，您确定要删除吗？</font>',
        top: $(window).height() / 4,
        fn: function (r) {
            if (r) {
                $.ajax({
                    url: '/purchaseListGoods/delete',
                    dataType: 'json',
                    type: 'post',
                    data: {
                        'purchaseListId': selections[0].purchaseListId
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
                            $('#dg2').datagrid('reload');
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
 * 导入
 */
function imports() {
    $('#uploadonlineinfo').window('open').dialog('setTitle', '文件上传');
}


function uploadonline() {
    $('#fam').form('submit', {
        url: "/purchaseListGoods/importPurchase",
        onSubmit: function () {

        },
        success: function (result) {
            var resultObj = eval('(' + result + ')');
            if (resultObj.code === 100) {
                let purchaseLists = resultObj.info.join(',');
                $('#uploadonlineinfo').window('close');
                $('#dg2').datagrid('reload');
                $('#dg').datagrid({
                    url: '/purchaseListGoods/list',
                    queryParams: {
                        purchaseNumbers: purchaseLists,
                    }
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


function download() {
    window.location.href = '/purchaseListGoods/export';
}

$('#phishingTable')
    .on( 'init.dt', function () {
        $('.te-info-tip').on('mouseover', function () {

            layer.tips($(this).data().info, $(this), {
                tips: [1, '#3595CC'],
                time: 4000
            });
        });
        $('.te-info-tip').on('click', function () {
            window.open( $(this).data().target);
        });
    }).on( 'xhr.dt', function () {  //表格刷新后
    setTimeout(function () {
        $('.te-info-tip').on('mouseover', function() {
            layer.tips($(this).data().info, $(this), {
                tips: [1, '#3595CC'],
                time: 4000
            });
        });
        $('.te-info-tip').on('click', function () {
            window.open( $(this).data().target);
        });

    });
} ).dataTable( {
    search: {
        caseInsensitive: false
    },
    dom: 'Blftipr',
    buttons: [
        {
            text: '新建',
            className: 'btn btn-link',
            action: function ( e, dt, node, config ) {
                $("#phishing_id").val("");
                var pp = $("#add_phishing")[0].reset();
                //$("#exampleModal").modal('show');
                openLayer();
            },
            init: function ( dt, node, config ) {
                // this.disable();
            }
        },
        {
            text: '刷新',
            className: 'btn btn-link',
            action: function ( e, dt, node, config ) {
                dt.ajax.reload();
            }
        }
    ],
    searching:true,
    serverSide:true,
    paging:true,
    processing: true,
    ajax: {
        url: 'template',
        type: "POST"
    },
    columns: [
        { data: "name" ,"width": "15%","orderable":false},
        { data: function(obj){
            if(obj.title == null) {
                return "";
            }
            if (obj.title.length > 15) {
                //小tips
                return  '<a class="te-info-tip" data-info="' + obj.title + '" data-target="' + obj.url + '" >' + obj.title.substring(0,15)+ '...</a>';
            } else {
                return  '<a class="te-info-tip" data-info="' + obj.title + '"   data-target="' + obj.url + '"  >' + obj.title+ '</a>';
            }
            } ,"width": "15%","orderable":false
        },
        { data: function(obj){
                if(obj.url == null) {
                    return "";
                }
                if (obj.url.length > 50) {
                    //小tips
                    return  '<a class="te-info-tip" data-info="' + obj.url + '" data-target="' + obj.url + '" >' + obj.url.substring(0,50)+ '...</a>';
                } else {
                    return  '<a class="te-info-tip" data-info="' + obj.url + '" data-target="' + obj.url + '" >' + obj.url+ '</a>';
                }
            } ,"width": "30%","orderable":false
         },

        { data: function(obj){
                if(obj.keyword == null) {
                    return "";
                }
                if (obj.keyword.length > 30) {
                    //小tips
                    return  '<span class="te-info-tip" data-info="' + obj.keyword + '">' + obj.keyword.substring(0,30)+ '...</span>';
                } else {
                    return obj.keyword;
                }
            } ,"width": "30%","orderable":false
        },
        { data:function (obj) {
            return "<button class='btn btn-link' onclick='editPhishing("+ JSON.stringify(obj)+")'>编辑</button><button class=' btn btn-link'  onclick='delPhishing("+ JSON.stringify(obj)+")'>删除</button>"
        } ,"width": "15%","orderable":false}
    ],
    /*    scrollY: 400,*/
    language:{
        sProcessing: '<img src="'+ basepath+'/static/images/loading.gif" style="width:100px">',
        sLengthMenu: "显示 _MENU_ 项结果",
        sZeroRecords: "没有匹配结果",
        sInfo: "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
        sInfoEmpty: "显示第 0 至 0 项结果，共 0 项",
        sInfoFiltered: "(由 _MAX_ 项结果过滤)",
        sInfoPostFix: "",
        sSearch: "搜索:",
        sUrl: "",
        sEmptyTable: "表中数据为空",
        sLoadingRecords: "载入中...",
        sInfoThousands: ",",
        oPaginate: {
            sFirst: "首页",
            sPrevious: "上页",
            sNext:"下页",
            sLast :"末页"
        }
    }
} );
function openLayer() {
    layer.open({
        type: 1 //此处以iframe举例
        ,title: '新建或编辑仿冒网站模板'
        ,area: ['500px', '700px']
        ,shade: 0
        ,shadeClose:true
        ,offset: ['200px', '200px']
        ,anim:0
        ,maxmin: true
        ,content: $("#exampleModal")
        ,btn: ['保存', '取消'] //只是为了演示
        ,btn1: function(index, layero){
            var id = $("#phishing_id").val().trim();
            var url = $("#phishing_url").val().trim();
            var name = $("#phishing_name").val().trim();
            var title = $("#phishing_title").val().trim();
            var keyword = $("#phishing_keyword").val().replace(/，/ig,',').trim();
            //非空验证。
            if (url && name && title && keyword ) {
                var param = {
                    "id":id,
                    "url":url,
                    "name":name,
                    "title":title,
                    "keyword":keyword
                };
                $.ajax({
                    url:"opt/template",
                    data:param,
                    contentType: 'application/json;charset=utf-8',
                    type:'get',
                    success: function(data){
                        var result = data.result;
                        if (result) {
                            layer.msg('提交成功');
                            //关闭模态框
                            $("#exampleModal").modal('hide');
                            //刷新表格
                            $('#phishingTable').DataTable().draw();
                        } else{
                            layer.msg('提交失败');
                        }
                        layer.closeAll();
                    },
                    error: function (data) {
                        console.log('ajax error handling',data);
                        layer.msg('提交失败');
                        layer.closeAll();
                    }
                });
            } else {
                layer.msg('请检查必填字段');
            }
        }
        ,btn2: function(index, layero){
        }
    });

}

function editPhishing (obj) {
    console.log(obj);
    $("#phishing_id").val(obj.id);
    $("#phishing_url").val(obj.url);
    $("#phishing_name").val(obj.name);
    $("#phishing_title").val(obj.title);
    $("#phishing_keyword").val(obj.keyword);
    // $("#exampleModal").modal('show');
    openLayer();
}
function delPhishing (obj) {
    //询问框
    layer.confirm('确认要删处么？', {
        btn: ['确认','取消'] //按钮
    }, function(){
        $.ajax({
            url:"del/template",
            data:{"id":obj.id},
            contentType: 'application/json;charset=utf-8',
            success: function(data){
                var result = data.result;
                    if (result){
                    layer.msg('删处成功');
                    $('#phishingTable').DataTable().draw();
                } else {
                    layer.msg('删处失败');
                }
            },
            error: function (data) {
                console.log('ajax error handling',data);
                layer.msg('删处失败');
            }
        });
    }, function(){

    });
}



$("#handMovement").on('click',function () {

    var url = $("#phishing_url").val();
    if (!url) {
        layer.msg('手动分词需要填写网址');
    } else {
        //iframe层-父子操作
        layer.open({
            type: 2
            ,shade: 0
            ,offset: 'r'
            ,area: ['1200px', '700px'],
            fixed: false, //不固定
            maxmin: true,
            content: url
        });
    }
});

$("#participle").on('click',function () {
    var url = $("#phishing_url").val();
    if (!url) {
        layer.msg('智能分词需要填写网址');
    } else {
        url = encodeURIComponent( url , "utf-8" );
        $.ajax({
            url:"url/getWord",
            data:{"url":url},
            contentType: 'application/json;charset=utf-8',
            success: function(data){
                var result = data.result;
                if (result) {
                    var text = "";
                    var record = data.record;
                    var title = data.title;
                    var url = data.url;
                    for(var j = 0;j < record.length; j++) {
                        text = text + record[j] + ",";
                    }
                    if (text.length >0 ) {
                        $("#phishing_keyword").val(text);
                    } else {
                        layer.msg('未获取到关键词');
                    }
                    $("#phishing_title").val(title);
                    $("#phishing_url").val(url);
                } else {
                    layer.msg('获取关键词失败');
                }
            },
            error: function (data) {
                console.log('ajax error handling',data);
                layer.msg('提取失败');
            }
        });
    }
});


function  addPhishing(){



}
$("#phishing_url").blur(function(){
    var url = $("#phishing_url").val().trim();
    var id = $("#phishing_id").val().trim();
    if (url && !id) {
        $.ajax({
            url:"check/url",
            data:{"url":url},
            type:'get',
            contentType: 'application/json;charset=utf-8',
            success: function(data){
                var result  = data.reuslt;
                if (result) {
                    var num  = data.num;
                    if(num > 0) {
                        layer.msg('此url已经存在');
                        $("#phishing_url").focus();
                    }
                }
            }
        });
    }
});
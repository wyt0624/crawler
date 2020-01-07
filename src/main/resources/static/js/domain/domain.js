$('#domiantable')
    .on( 'init.dt', function () {
    $('.info-tip').on('click', function() {
        layer.tips($(this).data().info, $(this), {
            tips: [1, '#3595CC'],
            time: 4000
        });
    });
}).on( 'xhr.dt', function () {  //表格刷新后
    setTimeout(function () {
        $('.info-tip').on('click', function() {
            layer.tips($(this).data().info, $(this), {
                tips: [1, '#3595CC'],
                time: 4000
            });
        });
    });
} ).dataTable( {
    searching:true,
    serverSide:true,
    paging:true,
    processing: true,
    ajax: {
        url: 'info',
        data: {catagoryType:catagoryType},
        type: "POST"
    },
    columns: [
        { data: function(obj){
                if(obj.url == null) {
                    return "";
                }
                if (obj.url.length > 30) {
                    //小tips
                    return  '<a class="info-tip" data-info="' + obj.url + '">' + obj.url.substring(0,30)+ '...</a>';
                } else {
                    return obj.url;
                }
            } ,"width": "15%","orderable":false},
        { data: function (obj) {
            if(obj.category == 0){ return "正常" }
            if(obj.category == 1){ return "涉赌" }
            if(obj.category == 2){ return "涉黄" }
            if(obj.category == 9){ return "无效" }
        },"width": "5%","orderable":false},
        { data:  function (obj) {
                if(obj.title == null) {
                    return "";
                }
                if (obj.title.length > 10) {
                    return  '<a class="info-tip" data-info="' + obj.title + '">' + obj.title.substring(0,8) + '...</a>';
                } else {
                    return obj.title;
                }
            } ,"width": "10%"},
        { data: function(obj){
            if(obj.createTime == null) {
                return "";
            }
            else {
                var  time = obj.createTime.split("T").join(" ")
                return time.substr(0,19);
            }
        } ,"width": "8%","orderable":false},
        { data: "ip" ,"width": "8%","orderable":false},
        { data: "address" ,"width": "8%","orderable":false},
        { data: "email" ,"width": "8%","orderable":false},
        { data: "registrar" ,"width": "8%","orderable":false},
        { data: "company" ,"width": "8%","orderable":false},
        { data: function (obj) {
                if(obj.port == null) {
                    return "";
                }
               if (obj.port.length > 15) {
                   return  '<a class="info-tip" data-info="' + obj.port + '">' + obj.port.substring(0,15) +'...</a>';
               } else {
                   return obj.port;
               }
            },"width": "10%","orderable":false},
        { data: "webPhone" ,"width": "8%","orderable":false}
    ],
/*    scrollY: 400,*/
    language:{
        sProcessing: '<img src="'+ basepath+'/static/images/loading.gif" style="width:400px">',
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
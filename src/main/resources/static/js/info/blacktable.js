$('#blacktable')
    .on( 'init.dt', function () {
        $('.info-tip').on('mouseover', function() {
            layer.tips($(this).data().info, $(this), {
                tips: [1, '#3595CC'],
                time: 4000
            });
        });
        $('.info-tip').on('click', function() {
            window.open("//"+$(this).data().target);
        });
    }).on( 'xhr.dt', function () {  //表格刷新后
    setTimeout(function () {
        $('.info-tip').on('mouseover', function() {
            layer.tips($(this).data().info, $(this), {
                tips: [1, '#3595CC'],
                time: 4000
            });
        });
        $('.info-tip').on('click', function() {
            window.open("//"+$(this).data().target);
        });
    });
} ).dataTable( {
    searching:true,
    serverSide:true,
    paging:true,
    processing: true,
    pagingType: 'full_numbers',
    ajax: {
        url: 'blacklist',
        type: "POST"
    },
    columns: [
        { data: "id" ,"width": "10%","orderable":false},
        { data: function(obj){
                if(obj.url == null) {
                    return "";
                }
                if (obj.url.length > 20) {
                    //小tips
                    return  '<a class="info-tip" data-info="' + obj.url + '" data-target="' + obj.url + '">' + obj.url.substring(0,20)+ '...</a>';
                } else {
                    return  '<a class="info-tip" data-info="' + obj.url + '" data-target="' + obj.url + '" >' + obj.url+ '</a>';
                }
            } ,"width": "45%","orderable":false
        },
        { data: function(obj){
                if(obj.domain == null) {
                    return "";
                }
                if (obj.domain.length > 30) {
                    //小tips
                    return  '<a class="info-tip" data-info="' + obj.domain + '"  data-target="' + obj.url + '">' + obj.domain.substring(0,30)+ '...</a>';
                } else {
                    return  '<a class="info-tip" data-info="' + obj.domain + '"  data-target="' + obj.url + '">' + obj.domain+ '</a>';
                }
            } ,"width": "35%","orderable":false
        }
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
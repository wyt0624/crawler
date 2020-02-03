<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
    <title>dns</title>
    <link href="${dn.contextPath}/static/css/bootstrap.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" type="text/css" href="${dn.contextPath}/static/css/jquery.dataTables.css">
    <link href="${dn.contextPath}/static/css/product.css" rel="stylesheet" type="text/css"/>
    <script  src="${dn.contextPath}/static/js/jquery-3.1.1.min.js"></script>
    <script  src="${dn.contextPath}/static/layer/layer.js"></script>
    <script  src="${dn.contextPath}/static/js/popper.min.js"></script>
    <script  src="${dn.contextPath}/static/js/bootstrap.js"></script>
    <script  src="${dn.contextPath}/static/js/echarts.js"></script>
    <script type="text/javascript" src="http://api.map.baidu.com/api?v=3.0&ak=pOXILH8tLQnGVEQGxIMQdjAWuplxjZ95"></script>

    <!-- 表格插件 -->
    <script type="text/javascript" charset="utf8" src="${dn.contextPath}/static/js/jquery.dataTables.min.js"></script>
    <script type="text/javascript" charset="utf8" src="${dn.contextPath}/static/js/dataTables.responsive.min.js"></script>
    <script type="text/javascript" charset="utf8" src="${dn.contextPath}/static/js/dataTables.buttons.min.js"></script>
    <script type="text/javascript" charset="utf8" src="${dn.contextPath}/static/js/buttons.html5.min.js"></script>
    <script type="text/javascript" charset="utf8" src="${dn.contextPath}/static/js/jszip.min.js"></script>

    <script>
        var basepath =  "${dn.contextPath}";
    </script>
</head>
<body>

<!--头部信息-->
<nav class="nav dnsnav">
    <a class="nav-link" id = "home">首页</a>
    <a class="nav-link" id = "domain">域名信息</a>
    <a class="nav-link" id = "white">白名单信息</a>
    <a class="nav-link" id = "template">添加网站模板</a>
    <a class="nav-link" id = "statistics_nav">网站统计</a>
</nav>

<!-- 选择框 -->
<div class="category_tap">
    <form class="form-horizontal" role="form" id="format_id" action="index" method="post">
        <div class="form-group">
            <div class="row">
                <label  class="col-sm-6 control-label">
                    请选择犯罪类型:
                </label>
                <div class="col-sm-6">
                    <select class="form-control"  id = "category" name="category">
                        <option  <#if catagoryType==0> selected="selected" </#if> value = "0">不选择</option>
                        <option  <#if catagoryType==1> selected="selected" </#if> value = "1">涉赌</option>
                        <option  <#if catagoryType==2> selected="selected" </#if> value = "2">涉黄</option>
                    </select>
                </div>
            </div>
        </div>
    </form>
</div>
<!-- 中间主体-->
<div id = "mapinfo" class="mapinfo">
    <!--百度地图-->
    <div id="allmap" class="dnsmap"></div>
</div>
<div class="statistics" id = "statistics">
    <!-- 饼图展示-->
    <div class="pieChart" id="pieChart"></div>
<#--<!-- 柱形图展示&ndash;&gt;-->
    <div class="pillarChart" id = "pillarChart"></div>
</div>
<!-- 域名信息-->
<#include "../domain/domain.ftl">
<!--白名单信息-->
<#include "../info/info.ftl">
<!-- 模板 -->
<#include "../template/template.ftl">
<!--模态框-->
<#include "../template/modal.ftl">
</body>
<script>
    //犯罪类型
    var catagoryType = ${catagoryType};
    $(function(){
        <!--初始化 犯罪选择按钮 -->
        $("#category").bind("change",function () {
            $("#format_id").submit();
        });
        $("#home").on("click",function () {
            $("body,html").animate({'scrollTop': $("#mapinfo").offset().top -44}, 300);
        });
        $("#domain").on("click",function () {
            $("body,html").animate({'scrollTop': $("#domianinfo").offset().top}, 300);
        });
        $("#white").on("click",function () {
            $("body,html").animate({'scrollTop': $("#info-panel").offset().top}, 300);
        });
        $("#template").on("click",function () {
            $("body,html").animate({'scrollTop': $("#template-pennel").offset().top}, 300);
        });
        $("#statistics_nav").on("click",function () {
            statistics();
        });
        //iframe层-父子操作
        statistics()

    });
    function statistics() {
        var tte = layer.open({
            type: 1 //此处以iframe举例
            ,title: 'dns违法数据统计'
            ,area: ['800px', '450px']
            ,shade: 0
            ,shadeClose:true
            ,offset: 'rb'
            ,anim:0
            ,maxmin: true
            ,content: $("#statistics")
        });
        initPieChart();//初始化饼图
        initPillarChart();//初始化柱形图
    }


    //初始化地图
    var mp = new BMap.Map("allmap");
    //设置北京为中心，地图层级为1
    mp.centerAndZoom("河南",2);
    //加载缩放控件
    mp.addControl(new BMap.NavigationControl());
    mp.enableScrollWheelZoom();   //启用滚轮放大缩小，默认禁用
    mp.enableContinuousZoom();    //启用地图惯性拖拽，默认禁用
    //初始化点的样式
    var myIcon = new BMap.Icon("${dn.contextPath}/static/images/icon.png", new BMap.Size(100,127));
    //遍历信息info
    <#list list as info>
        var point = new BMap.Point(${info.lngX},${info.lngY} );
        var text = "${info.address}(${info.num})";
        addMarker(point,text,"${info.address}","${info.num}");
    </#list>

    //创建点。并添加点击事件。
    function addMarker(point,text,address,num) {
        var marker = new BMap.Marker(point,{icon:myIcon});
        var lebel = new BMap.Label(text,{offset:new BMap.Size(10,-22)});
        lebel.setContent(text);
        marker.setLabel(lebel);
        mp.addOverlay(marker);
        //为点点检点击事件。
        marker.addEventListener("click",getAttr);
        function getAttr(){
            loadinfo(address,num)
        }
    }
    //初始化加载的所有信息。
    function loadinfo (address) {
        $("body,html").animate({'scrollTop': $("#domianinfo").offset().top}, 300);
        // $("body,html").scrollTop(870);
        var table = $('#domiantable').DataTable();
        table.search(address);
        table.draw(true);
    }
    //初始化饼图
    function initPieChart(){
        option = {
            title: {
                text: '某站点用户访问来源',
                subtext: '纯属虚构',
                left: 'center'
            },
            tooltip: {
                trigger: 'item',
                formatter: '{a} <br/>{b} : {c} ({d}%)'
            },
            legend: {
                orient: 'vertical',
                left: 'left',
                data: ['直接访问', '邮件营销', '联盟广告', '视频广告', '搜索引擎']
            },
            series: [
                {
                    name: '访问来源',
                    type: 'pie',
                    radius: '55%',
                    center: ['50%', '60%'],
                    data: [
                        {value: 335, name: '直接访问'},
                        {value: 310, name: '邮件营销'},
                        {value: 234, name: '联盟广告'},
                        {value: 135, name: '视频广告'},
                        {value: 1548, name: '搜索引擎'}
                    ],
                    emphasis: {
                        itemStyle: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    }
                }
            ]
        };
        var myChart = echarts.init(document.getElementById('pieChart'));
        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
    }
    //初始化柱形图
    function initPillarChart(){
        var dataAxis = ['点', '击', '柱', '子', '或', '者', '两', '指', '在', '触', '屏', '上', '滑', '动', '能', '够', '自', '动', '缩', '放'];
        var data = [220, 182, 191, 234, 290, 330, 310, 123, 442, 321, 90, 149, 210, 122, 133, 334, 198, 123, 125, 220];
        var yMax = 500;
        var dataShadow = [];

        for (var i = 0; i < data.length; i++) {
            dataShadow.push(yMax);
        }

        option = {
            title: {
                text: '特性示例：渐变色 阴影 点击缩放',
                subtext: 'Feature Sample: Gradient Color, Shadow, Click Zoom'
            },
            xAxis: {
                data: dataAxis,
                axisLabel: {
                    inside: true,
                    textStyle: {
                        color: '#fff'
                    }
                },
                axisTick: {
                    show: false
                },
                axisLine: {
                    show: false
                },
                z: 10
            },
            yAxis: {
                axisLine: {
                    show: false
                },
                axisTick: {
                    show: false
                },
                axisLabel: {
                    textStyle: {
                        color: '#999'
                    }
                }
            },
            dataZoom: [
                {
                    type: 'inside'
                }
            ],
            series: [
                { // For shadow
                    type: 'bar',
                    itemStyle: {
                        color: 'rgba(0,0,0,0.05)'
                    },
                    barGap: '-100%',
                    barCategoryGap: '40%',
                    data: dataShadow,
                    animation: false
                },
                {
                    type: 'bar',
                    itemStyle: {
                        color: new echarts.graphic.LinearGradient(
                                0, 0, 0, 1,
                                [
                                    {offset: 0, color: '#83bff6'},
                                    {offset: 0.5, color: '#188df0'},
                                    {offset: 1, color: '#188df0'}
                                ]
                        )
                    },
                    emphasis: {
                        itemStyle: {
                            color: new echarts.graphic.LinearGradient(
                                    0, 0, 0, 1,
                                    [
                                        {offset: 0, color: '#2378f7'},
                                        {offset: 0.7, color: '#2378f7'},
                                        {offset: 1, color: '#83bff6'}
                                    ]
                            )
                        }
                    },
                    data: data
                }
            ]
        };

// Enable data zoom when user click bar.
        var myChart = echarts.init(document.getElementById('pillarChart'));
        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
        var zoomSize = 6;
        myChart.on('click', function (params) {
            console.log(dataAxis[Math.max(params.dataIndex - zoomSize / 2, 0)]);
            myChart.dispatchAction({
                type: 'dataZoom',
                startValue: dataAxis[Math.max(params.dataIndex - zoomSize / 2, 0)],
                endValue: dataAxis[Math.min(params.dataIndex + zoomSize / 2, data.length - 1)]
            });
        });
    }
</script>
<!--黑灰产表格js-->
<script src="${dn.contextPath}/static/js/info/domain.js"></script>
<!--黑灰产白名单-->
<script src="${dn.contextPath}/static/js/info/whitetable.js"></script>
<!--黑灰产黑名单-->
<script src="${dn.contextPath}/static/js/info/blacktable.js"></script>
<!--黑灰产模板-->
<script src="${dn.contextPath}/static/js/info/template.js"></script>


</html>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
    <title>dns</title>
    <link href="${dn.contextPath}/static/css/bootstrap.css" rel="stylesheet" type="text/css"/>
<#--<link href="${dn.contextPath}/static/css/bootstrap-grid.css" rel="stylesheet" type="text/css"/>-->
<#--<link href="${dn.contextPath}/static/css/bootstrap-reboot.css" rel="stylesheet" type="text/css"/>-->
    <link rel="stylesheet" type="text/css" href="${dn.contextPath}/static/css/jquery.dataTables.css">
    <link href="${dn.contextPath}/static/css/product.css" rel="stylesheet" type="text/css"/>
<#--<script  src="${dn.contextPath}/static/js/jquery-3.1.1.min.js"></script>-->
    <script  src="${dn.contextPath}/static/js/jquery-3.1.1.min.js"></script>
    <script  src="${dn.contextPath}/static/layer/layer.js"></script>
    <script  src="${dn.contextPath}/static/js/bootstrap.bundle.js"></script>
    <script  src="${dn.contextPath}/static/js/popper.min.js"></script>
    <script  src="${dn.contextPath}/static/js/bootstrap.js"></script>
    <script type="text/javascript" src="http://api.map.baidu.com/api?v=3.0&ak=DeszS6hHjpYXQdYZnRAQLorkOQvB2nUn"></script>

    <!-- 表格插件 -->
    <script type="text/javascript" charset="utf8" src="${dn.contextPath}/static/js/jquery.dataTables.js"></script>
    <script>
        var basepath =  "${dn.contextPath}";
    </script>
</head>
<body>

<!--头部信息-->
<nav class="nav dnsnav">
    <a class="nav-link active" id = "home">首页</a>
    <a class="nav-link" id = "domain">域名信息</a>
    <a class="nav-link" id = "white">白名单信息</a>
    <a class="nav-link" id = "template">添加网站模板</a>
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


<!--百度地图-->
<div id="allmap" class="dnsmap"></div>
<!-- 域名信息-->
<#include "../domain/domain.ftl">
<!--白名单信息-->
<#include "../info/info.ftl">
</body>



<script>
    var params; //ajax 传递参数
    var catagoryType = ${catagoryType};
    $(function(){
        $("#category").bind("change",function () {
            $("#format_id").submit();
        });
    });

    //初始化地图
    var mp = new BMap.Map("allmap");
    //设置北京为中心，地图层级为1
    mp.centerAndZoom("河南",6);
    //加载缩放控件
    mp.addControl(new BMap.NavigationControl());
    mp.enableScrollWheelZoom();   //启用滚轮放大缩小，默认禁用
    mp.enableContinuousZoom();    //启用地图惯性拖拽，默认禁用
    //初始化点的样式
    var myIcon = new BMap.Icon("${dn.contextPath}/static/images/icon.png", new BMap.Size(100,127));
    //遍历信息
    <#list list as info>
        var point = new BMap.Point(${info.lngX},${info.lngY} );
        var text = "${info.address}(${info.num})";
        addMarker(point,text,"${info.address}","${info.num}");
    </#list>

    function initMap(){

    }
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
    //加载的所有信息。
    function loadinfo (address) {
        $("body,html").scrollTop(670);
        var table = $('#domiantable').DataTable();
        table.search(address);
        table.draw(true);
    }
</script>
<script src="${dn.contextPath}/static/js/domain/domain.js"></script>
</html>
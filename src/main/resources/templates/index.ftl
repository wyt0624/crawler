<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
    <title>dns</title>
    <link href="${dn.contextPath}/static/css/bootstrap.css" rel="stylesheet" type="text/css"/>
    <link href="${dn.contextPath}/static/css/bootstrap-grid.css" rel="stylesheet" type="text/css"/>
    <link href="${dn.contextPath}/static/css/bootstrap-reboot.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.20/css/jquery.dataTables.css">
    <link href="${dn.contextPath}/static/css/product.css" rel="stylesheet" type="text/css"/>
<#--<script  src="${dn.contextPath}/static/js/jquery-3.1.1.min.js"></script>-->
    <script  src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
    <script type="text/javascript" src="http://api.map.baidu.com/api?v=3.0&ak=DeszS6hHjpYXQdYZnRAQLorkOQvB2nUn"></script>
    <!-- 表格插件 -->
    <script type="text/javascript" charset="utf8" src="https://cdn.datatables.net/1.10.20/js/jquery.dataTables.js"></script>

</head>
<body>
<!--头部信息-->
<nav class="nav dnsnav">
    <a class="nav-link active" href="#">首页</a>
    <a class="nav-link" href="#">域名信息</a>
    <a class="nav-link" href="#">白名单信息</a>
    <a class="nav-link" href="#">添加网站模板</a>
</nav>
<!--百度地图-->
<div id="allmap" class="dnsmap"></div>
<!-- 域名信息-->
<#include "domain/domain.ftl">
<!--白名单信息-->
<#include "info/info.ftl">
</body>


<script>
    var mp = new BMap.Map("allmap");
    mp.centerAndZoom(new BMap.Point(116.3964,39.9093), 10);
    mp.enableScrollWheelZoom();
    mp.addControl(new BMap.NavigationControl());
</script>


</html>
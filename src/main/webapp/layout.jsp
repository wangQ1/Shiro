<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page pageEncoding="utf-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>This is my page</title>
<link rel="stylesheet" type="text/css" href="themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="themes/icon.css">
<script type="text/javascript" src="jquery.min.js"></script>
<script type="text/javascript" src="jquery.easyui.min.js"></script>
	<style type="text/css">
		body{
			font-size: 14px;
			height:100%;
	    }
	</style>
	<script type="text/javascript">
		function urlClick(title, url) {
			//exists方法 判断title='学生管理'的面板是否存在
			var ifExist = $("#myTabs").tabs("exists", title);
			if (!ifExist) {
				//控件对象。控件名（'方法名'， 参数）
				$("#myTabs").tabs("add",//添加一个新选项卡面板，选项参数是一个配置对象
					{
						title:title,//标题
						closable:true,//是否可关闭
						//添加内容     iframe 动态加载网页
						content:'<iframe frameborder=0 width="100%" height="100%" scrolling="no" src="'+url+'"></iframe>'
					}
				);
			}
			$("#myTabs").tabs("select", title);
		}
	</script>
</head>
<body style="margin:1px;padding: 1px">
	<div class="easyui-layout" style="width:1500px;height:750px;">
		<div data-options="region:'north'" style="height:150px; background:url(hei.png)">
			<div style="margin-left: 1100px;margin-top: 120px;">您好&nbsp;<font color=blue>${requestScope.userName}</font>
			&nbsp;&nbsp;<a href="${pageContext.request.contextPath}/login.html" style="text-decoration: none;">退出登录</a></div>
		</div>
		<div data-options="region:'west',split:true" title="导航菜单" style="width:15%;">
			<div class="easyui-accordion">
				<div title="用户管理" style="overflow:auto;padding:10px;">
					<c:forEach var="temp" items="${requestScope.userMenuList}">
						<a href="javascript:urlClick('${temp.menuName}', '${pageContext.request.contextPath}${temp.menuUrl}')" style="text-decoration: none;">
						<img alt="" src="themes/icons/man.png">${temp.menuName}<br/></a>
					</c:forEach>
				</div>
				<div title="系统设置" style="padding:10px;">
				</div>
			</div>
		</div>
		<div data-options="region:'center',iconCls:'icon-ok'">
			<div id="myTabs" class="easyui-tabs" style="width:100%;height:100%">
		    	<div title="欢迎使用" style="padding:10px" ></div>
		  	</div>
		</div>
	</div>
</body>
</html>
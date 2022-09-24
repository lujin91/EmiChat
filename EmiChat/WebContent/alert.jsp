<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.messager.js"></script>
<title>信息提示页面</title>
</head>
<body>
	<div style="width:1200px; margin: 0px auto;">
		<div class="mainDiv">
			<div class="relationWin">
				<div class="searchDiv">
            		<input id="searchInput" type="text" placeholder="查找联系人/群组..."/>
            		<button id="searchBtn"><i>搜索</i></button>
        		</div>
        		<div id="chatListDiv"></div>
			</div>
			
		</div>
	</div>
</body>
<script type="text/javascript">
	$.messager.show(0, '您收到一条新消息', 2000);
</script>
</html>
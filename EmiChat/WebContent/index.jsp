<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.9.1.min.js"></script>
<title>翊米WebChat首页</title>
<style>
	.loading{
		position: fixed;
		top:0;
		left:0;
		width:100%;
		height:100%;
		background:rgba(255,255,255,1);
		display:none;
	}
	.loading img{
		width:140px;
		height:80px;
		position:absolute;
		top:50%;
		left:50%;
		margin-left:-70px;
		margin-top:-40px;
	}
</style>
</head>
<body>
	<c:choose>
		<c:when test="${not empty user}">
			<script type="text/javascript">window.location.href="${pageContext.request.contextPath}/chat/main";</script>
		</c:when>
		<c:otherwise>
			<div class="loginPage">
				<div class="loginBody">
					<div class="loginTitle">Emi WebChat</div>
					<form action="${pageContext.request.contextPath}/user/login" method="post" id="loginForm">
					<input type="hidden" name="remberUrl" value="null"/>
						<div class="item">
							<label for="j_username">用户名：</label> <input type="text"
								id="j_username" name="uname" placeholder="用户名" required="required">
						</div>
						<div class="item">
							<label for="j_password">密码：</label> <input type="password"
								id="j_password" name="password" placeholder="密码" required="required">
						</div>
						<p class="warn">${msg}</p>
						<div class="itemBtnDiv">
							<button type="button" onclick="registerUI()">注册</button>
							<button type="button" id="loginBtn">登陆</button>
						</div>
					</form>
				</div>
				<p class="copyRight">© 2020 翊米科技股份有限公司</p>
			</div>
			<div class="loading">
				<img src="${pageContext.request.contextPath}/images/loading2.gif" alt="">
			</div>
		</c:otherwise>
	</c:choose>
</body>
<script>
	
	$(function(){
		if(Notification.permission != "denied" && Notification.permission != "granted"){
			//Notification.requestPermission();
		}
	});
	
	$("#loginBtn").click(function(){
		
		var valid = true;
		
		$("#loginForm input[required=required]").each(function()
		{
			$(this).next().remove();
			if(isEmpty($(this).val()))
			{
				valid = false;
				$(this).parent().append('<font style="color: red">不允许为空</font>');
			}
		});
		
		if(valid)
		{
			$("#loginForm").submit();	
		}
	});
	
	function isEmpty(value)
	{
		if(value == null || value == "" || $.trim(value).length == 0)
		{
			return true;
		}else{
			return false;
		}
	}
	
	function registerUI(){
		window.location="${pageContext.request.contextPath}/user/registerUI";
	}
</script>
</html>

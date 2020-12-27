<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<div id="headWin">
		<form id="logoutForm" action="${pageContext.request.contextPath}/user/logout" style="margin: 0px; height: 100%">
		<font>Mdp简易的webchat</font>
		<span>欢迎您，${user.uname}&nbsp;&nbsp;<a id="logoutHref" href="javascript:void(0)">退出</a></span>
		</form>
	</div>
</body>

<script type="text/javascript">
	$("#logoutHref").click(function(){
		if(confirm("是否确认退出？")){
			$("#logoutForm").submit();
		}
	});
</script>

</html>
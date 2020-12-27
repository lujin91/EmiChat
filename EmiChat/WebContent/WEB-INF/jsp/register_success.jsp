<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.9.1.min.js"></script>
<title>用户注册成功页面</title>
</head>
<body>
	<div id="showinfo">亲爱的${user.uname}，您已注册成功，3秒后跳转至<a href='${pageContext.request.contextPath}/'>登录页面</a></div>
</body>

<script type="text/javascript">

	var count = 3;
	
	window.setInterval(toLoginUI, 1000);
	
	function toLoginUI(){
		if(count == 0){
			window.location.href="${pageContext.request.contextPath}/";
		}else{
			var content = "亲爱的${user.uname}，您已注册成功，" + --count + "秒后跳转至<a href='${pageContext.request.contextPath}/'>登录页面</a>";
			$("#showinfo").html(content);
		}
	}

</script>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/register.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.9.1.min.js"></script>
<title>用户注册页面</title>
</head>
<body>
	<div class="registerPage">
		<div class="registerBody">
			<div class="registerTitle">Emi WebChat</div>
			<form action="${pageContext.request.contextPath}/user/register" method="post" id="registForm">
				<div class="item">
					<label for="j_username">用户名：</label> <input type="text"
						id="j_username" name="uname" placeholder="用户名" required="required">
				</div>
				<div class="item">
					<label for="j_password">密码：</label> <input type="password"
						id="j_password" name="password" placeholder="密码" required="required">
				</div>
				<div class="item">
					<label for="j_password2">确认密码：</label> <input type="password"
						id="j_password2" name="password2" placeholder="請再次输入密码" required="required">
				</div>
				<div class="item">
					<label for="j_emailAddr">电邮地址：</label> <input type="text"
						id="j_emailAddr" name="emailAddr" placeholder="电邮地址" required="required">
				</div>
				<div class="item">
					<label for="j_nickname">别名/昵称：</label> <input type="text"
						id="j_nickname" name="nickname" placeholder="别名/昵称" required="required">
				</div>
				<div class="item">
					<label for="j_birthday">生日：</label> <input type="date"
						id="j_birthday" name="birthday" placeholder="生日" required="required">
				</div>
				
				<p/>
				
				<div class="itemBtnDiv">
					<button type="reset">清空</button>
					<button id="registerBtn" type="button">注册</button>
				</div>
			</form>
		</div>
		<p class="copyRight">© 2020 翊米科技股份有限公司</p>
	</div>
</body>
<script type="text/javascript">

	$("#registerBtn").click(function(){
		var valid = true;
		$("#registForm input[required=required]").each(function(){
			$(this).next().remove();
			if(isEmpty($(this).val()))
			{
				valid = false;
				$(this).parent().append('<label style="color: red">不允许为空</label>');
			}
		});
		
		if(valid)
		{
			if(validConfirmpwd())
			{
				checkUname();
			}
		}
	});
	
	function validConfirmpwd()
	{
		if($("#j_password2").val() != $("#j_password").val())
		{
			$("#j_password2").next().remove();
			$("#j_password2").parent().append('<label style="color: red">两次输入密码不一致</label>');
			return false;
		}else{
			$("#j_password2").next().remove();
			return true;
		}	
	}

	function isEmpty(value)
	{
		if(value == null || value == "" || $.trim(value).length == 0)
		{
			return true;
		}else{
			return false;
		}
	}
	
	function checkUname()
	{
		$.post(
			"${pageContext.request.contextPath}/user/verifyUname",
			{"username":$("#registForm input[name=uname]").val()},
			function(obj){
				if(obj.repeated == 'true')
				{
					$("#registForm input[name=uname]").next().remove();
					$("#registForm input[name=uname]").parent().append('<label style="color: red">用户名已存在</label>');
					return;
				}
				else
				{
					$("#registForm").submit();
				}
			},
			"json"
		);
	}
	
</script>
</html>
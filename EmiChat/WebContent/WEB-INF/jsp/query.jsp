<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/register.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.9.1.min.js"></script>
<title>查找联系人/群组页面</title>
</head>
<body>
	<form action="${pageContext.request.contextPath}/chat/query" method="post" id="queryForm">
		<div>
			<input type="text" name="chatName" placeholder="输入用户名/群组名" required="required">
			<button id="queryBtn" type="button">查询</button>
		</div>
	</form>
	
	<div style="width: 500px;">
		<table style="border: 1px solid #CCC" class="">
			<tr>
				<td style="border: 1px solid #CCC"><label>贾玉婷</label></td>
				<td style="border: 1px solid #CCC"><label>联系人</label></td>
				<td style="border: 1px solid #CCC"><button value="">加为好友</button></td>
			</tr>
			
		</table>
	</div>
</body>
<script type="text/javascript">

	$("#queryBtn").click(function(){
		var valid = true;
		$("#queryForm input[required=required]").each(function(){
			$(this).siblings("label").remove();
			if(isEmpty($(this).val()))
			{
				valid = false;
				$(this).parent().append('<label style="color: red">不允许为空</label>');
				$(this).val("");
			}
		});
		
		if(valid)
		{
			$("#queryForm").submit();
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
	
</script>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<div class="chatWin">
		<div class="dialogInputWin">
			<div class="chat-menu">
				<ul id="menulist">
				</ul>
			</div>
			<div class="chat-main">
				<div id="chatTabs">
				</div>
				<div class="chatToolsDiv">
					<img alt="" src="">
				</div>
				<div class="inputDiv">
					<textarea id="inputTextArea" rows="" cols="70"></textarea>
				</div>
				<div class="inputBtnDiv">
					<span class="inlineSpan"></span>
					<input id="sendMsgBtn" disabled="disabled" type="button" value="发送" onclick="sendMsg()"/>
					<input id="clearInputBtn" type="button" value="清空" onclick="clearInput()"/>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.messager.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/chat_main.css" />
<title>即时通讯主页面</title>
</head>
<body style=" background-color: rgb(250, 250, 250);">
	<div style="width:1200px; margin: 0px auto;">
		<%@ include file="chatPart/chat_banner.jsp" %>
		<div class="mainDiv">
			<div class="relationWin">
				<div class="searchDiv">
            		<input id="searchInput" type="text" placeholder="查找联系人/群组..."/>
            		<button id="searchBtn"><i>搜索</i></button>
        		</div>
        		<div id="chatListDiv"><%@ include file="chatPart/chat_list.jsp" %></div>
			</div>
			
			<%@ include file="chatPart/chat_input.jsp" %>
		</div>
	</div>
	
	<%-- 
	<jsp:include page="chatPart/head.jsp" />
	<div style="width: 100%; height: 800px;">
		<jsp:include page="chatPart/relation_list.jsp" />
		<jsp:include page="chatPart/dialog_input.jsp" />
	</div>
	--%>
	
</body>
<script type="text/javascript">

	var winfocus;
	$(function(){
		ws_init();
		html_init();
		window.setInterval(reloadPageInfo, 15000);
		window.onblur = function(){
			winfocus = false; 
		}
		window.onfocus = function(){
			winfocus = true; 
		}
	});
	
	
	function ws_init()
	{
		var protocol = window.location.protocol;
		var domain = window.location.host;
		var target = "ws://" + domain + "/EmiChat/chat";
		if(protocol == "https:"){
			target = "wss://" + domain + "/EmiChat/chat";
		}
		if('WebSocket' in window){
			ws = new WebSocket(target);
		}else if('MozWebSocket' in window){
			ws = new MozWebSocket(target);
		}else{
			alert('WebSocket is not support by this browser!');
		}
		
		ws.onopen=function(){
			console.info("WebSocket 通道建立成功");
		}
		
		ws.onmessage=function(event)
		{
			if(event.data.substring(0,5) == "WARN:")
			{
				$.MsgBox.AlertConfirm("警告", "<font style=\"font-weight: bold; color:red;\">" + event.data.substring(5) + "</font>", function(){
					window.location="${pageContext.request.contextPath}/";
				}, "确认");
			}
			else
			{
				var message = JSON.parse(event.data);
				var content = JSON.parse(message.content);
				var chatId;
				var chatType;
				if(isEmpty(message.recvGrpId)){
					chatId = message.sendId;
					chatType = 1;
				}else{
					chatId = message.recvGrpId
					chatType = 2;
				}
				if($("#menu-" + chatId).length <= 0){
					blink($("#" + chatId), 'rgb(255,237,106)', 3);
					if(chatType == 2 && $("#groupTab").is(":hidden")){
						
						blink($(".tab-menu li:nth-child(2)"), 'rgb(255,237,106)', 3);
					}else if(chatType == 1 && $("#personTab").is(":hidden")){
						blink($(".tab-menu li:nth-child(1)"), 'rgb(255,237,106)', 3);
					}
				}else{
					blink($("#menu-" + chatId), 'rgb(255,237,106)', 3);
				}
				
				if($("#chatContent-" + chatId).length > 0){
					var receiveDiv = "<div class=receiveMsgDiv><div class=receiveMsgSpan><font>" + message.sendTime + "&nbsp;&nbsp;" + content.uname + ":</font><br>" + content.text + "</div></div><p>";
					$("#chatContent-" + chatId).append(receiveDiv);
					$("#chatContent-" + chatId).scrollTop($("#chatContent-" + chatId)[0].scrollHeight);
				}
				
				notifyUser(chatId, "[" + content.uname + "] 发来一条消息，请点击去查看。 ", "message");
			}
		}
	}
	
	function reloadPageInfo()
	{
		$.post(
			"${pageContext.request.contextPath}/chat/reloadPageInfo",
			"",
			function(info)
			{
				$("#unreadCnt").html(info["unreads"]);
				if(info["unreads"] > 0 && $("#advice").css("background-color") != "rgb(255, 237, 106)"){
					blink($("#advice"), 'rgb(255,237,106)', 3);
					notifyUser(null, "您有未读的系统通知，请点击去查看。 ", "advice");
				}
				$.each(info["grps"], function (index, item) {
					if($("#" + item.grpId).length <= 0){
						addGrpItem(item);
						blink($("#" + item.grpId), 'rgb(255,237,106)', 3);
					}
	        	});
				
				var preChatId;
				$.each(info["rlts"], function (index, item) {
					if($("#" + item.cid).length <= 0){
						addRelationItem(item);
						blink($("#" + item.cid), 'rgb(255,237,106)', 3);
					}
					
					if(index == 0){
						$("#" + item.cid).prependTo("#personTab");
					}else{
						$("#" + preChatId).after($("#" + item.cid));
					}
					preChatId = item.cid;
					
					if(item.online == true){
						$("#" + item.cid + " img").attr("src", "${pageContext.request.contextPath}/images/online_user.png");
					}else{
						$("#" + item.cid + " img").attr("src", "${pageContext.request.contextPath}/images/offline_user.png");
					}
					
					if(item.blink == true && $("#" + item.cid).css("background-color") != "rgb(255, 237, 106)")
					{
						blink($("#" + item.cid), 'rgb(255,237,106)', 3);
					}
	        	});
				initChatItemBindEvent();
			},
			"json"
		);
	}
	
	function html_init()
	{
		$(".tab-menu li").unbind('click').bind('click', function(){
			var index = $(this).index();
			$(this).css('background','');
			$(".chatItemTab").eq(index).show().siblings().hide();
			$(this).addClass("change").siblings().removeClass("change");
		});
		
		$(".tab-menu li:first-child").click();
		
		$("#searchInput").focus();
		
		$("#inputTextArea").unbind('keypress').bind('keypress', function(){
			if(event.keyCode == 13){
				if($("#sendMsgBtn").attr("disabled"))
				{
					$.MsgBox.Alert("查询提示", "<font style=\"font-weight: bold; color:red;\">請选择一个交谈的对象</font>", "关闭");	
				}else{
					$("#sendMsgBtn").click();
				}
				event.preventDefault();
			}
		});
		
		$("#advice").unbind('click').bind('click', function(){
			$("#advice").css('background','none');
			$.post(
				"${pageContext.request.contextPath}/chat/listAdvices",
				function(map)
				{
					$.MsgBox.advices("系统通知", map, "关闭", null);
					$("#unreadCnt").html("0");
				},
				"json"
			);
		});
		
		$("#grpOper").unbind('click').bind('click', function(){
			$.MsgBox.createGrp("新建群组", "创建", "取消");
		});
		
		$("#searchBtn").unbind('click').bind('click', function() {
			var keyWord = $("#searchInput").val();
			if(isEmpty(keyWord)){
				$.MsgBox.Alert("查询提示", "<font style=\"font-weight: bold; color:red;\">搜索内容不允许为空</font>", "关闭");
			}
			else
			{
				$.post(
					"${pageContext.request.contextPath}/chat/search",
					{"keyWord": keyWord},
					function(obj)
					{
						if(obj.length == 0){
					    	$.MsgBox.Alert("查询提示", "没有找到<font style=\"color:red;\">'" + keyWord + "'</font>相关的联系人/群组", "关闭");
					    }else{
							$.MsgBox.list("查询列表", obj, "关闭", null);
					    }
					},
					"json"
				);
				$("#searchInput").val("");
			}
		});
		
		initChatItemBindEvent();
		
		/* 
			window.onbeforeunload=function(){
			return "确认退出Emi WebChat嗎？";
		}
		
		window.onbeforeunload=function(){
			var n = window.event.screenX-window.screenLeft;
			var b = n > document.documentElement.scrollWidth - 20;
			if(b && window.event.clientY < 0 || window.event.altKey){
				window.event.returnValue="确认退出Emi WebChat嗎？";
			}
		} */
	}
	
	function addGrpItem(grp){
		var grpItem="<a id='" + grp.grpId + "' class='chatItem' href='javascript:void(0);'><img src='${pageContext.request.contextPath}/images/grp.png'/><span>" + grp.grpName + "</span><br><font class='grpItemlist'>成员列表</font></a>";
		$("#groupTab").append(grpItem);
	}
	
	function addRelationItem(relation){
		var relationItem="<a id='" + relation.cid + "' class='chatItem' href='javascript:void(0);'><img src='${pageContext.request.contextPath}/images/offline_user.png' /><span>" + relation.remarkName + "</span></a>";
		$("#personTab").append(relationItem);
	}
	
	function addChatMeunAndTab(chatItemTab, chatId, chatType) //chatItemTab是$("#" + chatId)傳入，説明$("XX")在javascript裏是可以當作參數對象chatItemTab傳入，在使用時chatItemTab直接看作是$("XX")，無需$(chatItemTab)。
	{
		var menuEle="<li id=menu-" + chatId + " chatType=" + chatType + "><font>" + chatItemTab.children("span").text() + "</font><a href='javascript:void(0);'>x</a></li>";
		var tabEle="<div id=chatTab-" + chatId + " class='chat-tab'><font class=chat-tab-title>与" +  chatItemTab.children("span").text()+ "交谈中.. </font><div id=chatContent-" + chatId + " class=chatCntntDiv></div></div>";
		
		$("#menulist").append(menuEle);
		$("#chatTabs").append(tabEle);
		if($("#chatTabs").children().length > 1){
			$("#chatTab-"+chatId).hide();	
		}
		
		$("#sendMsgBtn").removeAttr("disabled");
		
		$("#menulist li").unbind('click').bind('click', function(){
			var index = $(this).index();
			$(this).css("background","#FFF").siblings().css("font-weight","normal");
			$(this).siblings().each(function(idx, menuEle){
				if($(menuEle).css("background-color") != "rgb(255, 237, 106)")
				{
					$(menuEle).css("background","url('../images/chat_menu_bg.gif')");
				}
			});
			$(this).children("font").css("font-weight","bold");
			$(this).siblings().children("font").css("font-weight","normal");
			$(".chat-tab").eq(index).show().siblings().hide();
			$("#chatContent-" + $(this).attr("id").split('-')[1]).scrollTop($("#chatContent-" + $(this).attr("id").split('-')[1])[0].scrollHeight);
			$("#sendMsgBtn").attr("chatType", $(this).attr("chatType"));
			$("#sendMsgBtn").attr("chatId", $(this).attr("id").split('-')[1]);
			$("#" + $(this).attr("id").split('-')[1]).css("background-color","rgb(236, 236, 236)");
			$("#" + $(this).attr("id").split('-')[1]).siblings().each(function(idx, chatEle){
				if($(chatEle).css("background-color") != "rgb(255, 237, 106)")
				{
					$(chatEle).css("background-color","rgb(250, 250, 250)");
				}
			});
		});
		
		$("#menulist li a").unbind('click').bind('click', function(){
			var chatId = $(this).parent("li").attr("id").split('-')[1];
			var prevItem = $(this).parent("li").prev("li");
			var nextItem = $(this).parent("li").next("li");
			var closeShow = false;
			if($("#menu-" + chatId).css("background-color").indexOf("rgb(255, 255, 255)") != -1){
				closeShow = true;
			}
			$("#menu-" + chatId).remove();
			$("#chatTab-" + chatId).remove();
			$("#sendMsgBtn").removeAttr("chatId");
			$("#sendMsgBtn").removeAttr("chatType");
			if($("#menulist li").length > 0)
			{
				if(closeShow){
					if(prevItem.length > 0){
						prevItem.click();
					}else if(nextItem.length > 0){
						nextItem.click();
					}
				}
			}
			else
			{
				$("#sendMsgBtn").attr("disabled","disabled");
			}
		});
	}
	
	function initChatItemBindEvent()
	{
		$(".chatItem").unbind('click').bind('click', function(){
			$(this).css("background-color","rgb(236, 236, 236)");
			$(this).siblings().each(function(idx, chatEle){
				if($(chatEle).css("background-color") != "rgb(255, 237, 106)")
				{
					$(chatEle).css("background-color","rgb(250, 250, 250)");
				}
			});
			var chatId = $(this).attr("id");
			if($("#menu-" + chatId).length > 0){
				$("#menu-" + chatId).click();
			}
		});
		
		$(".chatItem").unbind('dblclick').bind('dblclick', function(){
			var chatId = $(this).attr("id");
			var divId = $(this).parent().attr("id");
			var chatType;
			if($(this).parent().attr("id") == "personTab"){
				chatType = 1;
			}else{
				chatType = 2;
			}
			if($("#menu-" + chatId).length <= 0)
			{
				addChatMeunAndTab($(this), chatId, chatType);
				loadMessage(chatId, -1, chatType);
			}
			$("#menu-" + chatId).click();
		});
		
		$(".grpItemlist").unbind('click').bind('click', function(){
			var groupId = $(this).parent().attr("id");
			$.post(
				"${pageContext.request.contextPath}/chat/loadGrpMembers",
				{"groupId": groupId},
				function(obj)
				{
					$.MsgBox.list("成员列表", obj, "关闭", null);
				},
				"json"
			);
		});
	}
	
	function loadMessage(chatId, lastId, chatType)
	{
		var bottom = lastId == -1 ? true : false;
		$.ajax({
			type: "post",
			url: "${pageContext.request.contextPath}/chat/loadMessages",
			data: {"chatId":chatId, "lastId":lastId, "chatType": chatType},
			success: 
				function(list)
				{
					var oldHeight = $("#chatContent-" + chatId)[0].scrollHeight;
					$.each(list, function (index, message){
						var remarkName = $("#" + chatId + " font").html();
						var contentDiv;
						if(isEmpty(message.content.uname)){
							contentDiv = "<div class=infoMsgDiv><span><font>" + message.content.text + "</font><br></span></div>";
						}
						else if(message.sendId == "${user.uid}"){
							contentDiv = "<div class=speakMsgDiv><div class=speakMsgSpan><font>" + message.sendTime + "&nbsp;&nbsp;" + message.content.uname + "</font><br>" + message.content.text + "</div></div><p>";
						}else{
							contentDiv = "<div class=receiveMsgDiv><div class=receiveMsgSpan><font>" + message.sendTime + "&nbsp;&nbsp;" + message.content.uname + "</font><br>" + message.content.text + "</div></div><p>";
						}
						$("#chatContent-" + chatId).prepend(contentDiv);
						if(index == list.length-1){
							lastId = message.id;
						}
					});
					$("#loadmoreDiv-" + chatId).remove();
					if(list.length == 0){
						var loadmoreDiv="<div id=loadmoreDiv-" + chatId + " class='loadmoreDiv'>已無更多历史消息</div>";
						$("#chatContent-" + chatId).prepend(loadmoreDiv);
					}else{
						var loadmoreDiv="<div id=loadmoreDiv-" + chatId + " class='loadmoreDiv'><font onclick=\"loadMessage('" + chatId + "', '" + lastId + "', '" + chatType + "')\">点击加载历史消息</font></div>";
						$("#chatContent-" + chatId).prepend(loadmoreDiv);
					}
					if(bottom){
						$("#chatContent-" + chatId).scrollTop($("#chatContent-" + chatId)[0].scrollHeight);
					}else{
						var newHeight = $("#chatContent-" + chatId)[0].scrollHeight;
						$("#chatContent-" + chatId).scrollTop($("#chatContent-" + chatId).scrollTop() + newHeight - oldHeight);
					}
				},
			dataType: "json",
			async: true	//此处true 和 false没有不同，因ajax请求所在方法內没有其它需要执行的代码。
		});
	}
	
	function sendMsg()
	{
		$("#inputTextArea img").each(function(){
			if(isEmpty($(this).attr("src"))){
				$(this).remove();
			}else{
				if(this.width > 300){
					$(this).css({
						width: '300px',
						height: 'auto'
					});
				}
				$(this).attr("onclick","showChatImgSize(this)");
			}
		});
		
		if(isEmpty($("#inputTextArea").html())){
			$.MsgBox.Alert("发送提示", "<font style=\"font-weight: bold; color:red;\">发送内容不允许为空</font>", "关闭");
		}
		else
		{
			var existImg = false;
			if($("#inputTextArea img").length > 0)
			{
				existImg = true;
				var imgMap = {};
				$("#inputTextArea img").each(function(){
					var imgIndex = "ImgIdx_" + $(this).index();
					
					var base64ImgData = $(this).attr("src").replace(/data:image\/\w+;base64,/,"");
					imgMap[imgIndex]=base64ImgData;
				});
				var imgMapStr = JSON.stringify(imgMap);
				$.ajax({
					type: "post",
					url: "${pageContext.request.contextPath}/chat/uploadImgs",
					data:{"imgMapStr" : imgMapStr},
					success: 
						function(list)
						{
							$.each(list, function (index, url){
								$("#inputTextArea img").eq(index).attr("src",url);
							});
						},
					dataType: "json",
					async: false	
				});
			}
			if(existImg)
			{
				var fmtContent = $("#inputTextArea").html();
			}
			else
			{
				var fmtContent = "<div><span>" + $("#inputTextArea").html() + "</span></div>";
			}
			
			var content = {"uname":"${user.uname}", "text": fmtContent};
			var now = new Date();
			var chatId = $("#sendMsgBtn").attr("chatId");
			var speakDiv = "<div class=speakMsgDiv><div class=speakMsgSpan><font>" + formatDate(now, 'yyyy-MM-dd hh:mm:ss') + "&nbsp;&nbsp;${user.uname}</font><br>" + fmtContent + "</div></div><p>";
			$("#inputTextArea").html("");
			$("#chatContent-" + chatId).append(speakDiv);
			$("#chatContent-" + chatId).scrollTop($("#chatContent-" + chatId)[0].scrollHeight);
			var chatType = $("#sendMsgBtn").attr("chatType");
			var message;
			if(chatType == 1){
				message = {"sendId":"${user.uid}", "recvId": chatId, "chatType": chatType, "sendTime": formatDate(now, 'yyyy-MM-dd hh:mm:ss'), "content": JSON.stringify(content), "operation":"CHAT"};
			}else{
				message = {"sendId":"${user.uid}", "recvGrpId": chatId, "chatType": chatType, "sendTime": formatDate(now, 'yyyy-MM-dd hh:mm:ss'), "content": JSON.stringify(content), "operation":"CHAT"};
			}
			$("#menu-" + chatId).click();
	    	ws.send(JSON.stringify(message));
		}
	}
	
	function clearInput(){
		$("#inputTextArea").html("");
	}
	
	function showChatImgSize(img){
		
		var _w = parseInt($(window).width());	//获取屏幕宽
		var _h = parseInt($(window).height());	//获取屏幕高
		var realWidth;
		var realHeight;
		var style = "";
		
		$("<img/>").attr("src", $(img).attr("src")).load(function(){
			realWidth = this.width;
			realHeight = this.height;
			
			if(realWidth >= _w * 0.8)
			{
				style += 'width:' + (_w * 0.8) + 'px;';
				if(realHeight * 0.8 >= 800)
				{
					style += 'height:800px;';
				}else{
					style += 'height:' + _h * 0.8 + 'px;';
				}
			}
			else
			{
				if(realHeight >= 800)
				{
					style += 'height:800px;';
				}
			}
			
			$.MsgBox.showImg("图片","<img src='" + $(img).attr("src") + "' style='" + style + "'>", "关闭");
		});
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

	function formatDate(date, fmt){
		var currentDate = new Date(date);
		var o = {
			"M+": currentDate.getMonth() + 1,
			"d+": currentDate.getDate(),
			"h+": currentDate.getHours(),
			"m+": currentDate.getMinutes(),
			"s+": currentDate.getSeconds(),
			"q+": Math.floor((currentDate.getMonth() + 3) / 3),
			"S": currentDate.getMilliseconds()
		};
		if(/(y+)/.test(fmt)){
			fmt = fmt.replace(RegExp.$1, (currentDate.getFullYear() + "").substr(4 - RegExp.$1.length));
		}
		for(var k in o){
			if(new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
		}
		return fmt;			
	}
	
	function blinkTitle(){
		var i = 0;
		var ref = setInterval(function(){
			if(i % 2 == 0){
				document.title = '【您有未读讯息】';
			}else{
				document.title = '即时通讯主页面';
			}
			i++;
			if(i == 2 * 3){
				document.title = '即时通讯主页面';
				clearInterval(ref);
			}
		}, 500);
	}
	
	function blink(element, color, times){
		var current = element.css('background');
		var i = 0;
		var ref = setInterval(function(){
			if(i % 2 == 0){
				element.css('background',color);
			}else{
				element.css('background', current);
			}
			i++;
			if(i == 2 * times){
				element.css('background',color);
				clearInterval(ref);
			}
		}, 500);
	}
	
	function popNotice(chatId, content, type)
	{
		if(Notification.permission == "granted"){
			var noticeImg;
			if(type == "message"){
				noticeImg = "${pageContext.request.contextPath}/images/online_user.png";
			}else{
				noticeImg = "${pageContext.request.contextPath}/images/advice2.png";
			}
			var notification = new Notification("Emi WebChat:", {
				body: content,
				icon: noticeImg,
				tag: 'renotify',
				renotify: true
			});
			notification.onclick = function(){
				if(!isEmpty(chatId)){
					if($("#menu-" + chatId).length > 0){
						$("#menu-" + chatId).click();
						$("#chatContent-" + chatId).scrollTop($("#chatContent-" + chatId)[0].scrollHeight);
					}
				}
				notification.close();
			}
		}
	}
	
	function notifyUser(chatId, content, type)
	{
		if(window.Notification)
		{
			/* if(document.visibilityState === 'hidden'){
				if(Notification.permission == "granted"){
					popNotice(content, type);
				}else if(Notification.permission != "denied"){
					Notification.requestPermission(function (permission){
						popNotice(content, type);
					});
				}
			} */
			
			if(!winfocus){
				if(Notification.permission == "granted"){
					popNotice(chatId, content, type);
				}else if(Notification.permission != "denied"){
					Notification.requestPermission().then(function (permission){
						popNotice(chatId, content, type);
					});
				}
			}
		}
		blinkTitle();
	}
	
	
	(function() {
	    $.MsgBox = {
	        Alert: function(title, msg, btn1) {
	            GenerateHtml("alert", title, msg, btn1, null);
	            btnOk(); //alert只是弹出消息，因此没必要用到回调函数callback
	        },
	        Confirm: function(title, msg, callback, btn1) {
	            GenerateHtml("confirm", title, msg, btn1, btn2);
	            btnOk(callback);
	            btnNo();
	        },
	        AlertConfirm: function(title, msg, callback, btn1) {
	            GenerateHtml("alertConfirm", title, msg, btn1, null);
	            btnOk(callback); 
	        },
	        list: function(title, results, btn1, btn2) {
	            GenerateHtml("list", title, results, btn1, btn2);
	            btnOk(); //list没必要用到回调函数callback
	            btnNo();
	            btnApply();
	        },
	        
	        advices: function(title, results, btn1, btn2) {
	            GenerateHtml("advices", title, results, btn1, btn2);
	            btnOk(); //advices没必要用到回调函数callback
	            btnNo();
	            btnApproved();
	        },
	        
	        createGrp: function(title, btn1, btn2){
	        	GenerateHtml("createGrp", title, btn1, btn2);
	        	btnNoGrp();
	            btnNewGrp();
	        },
	        
	        showImg: function(title, img, btn1){
	        	GenerateHtml("showImg", title, img, btn1, null);
	        	btnNoGrp();
	            btnNewGrp();
	        },
	    }
	    //生成Html
	    var GenerateHtml = function(type, title, object, btn1, btn2) {
	        var _html = "";
	        _html += '<div id="mb_box"></div><div id="mb_con"><span id="mb_tit">' + title + '</span>';
	        if (type == "alert") {
	        	_html += '<a id="mb_ico">x</a><div id="mb_msg">' + object + '</div><div id="mb_btnbox">';
	            _html += '<input id="mb_btn_ok" type="button" value="' + btn1 + '" />';
	        }
	        if (type == "confirm") {
	        	_html += '<a id="mb_ico">x</a><div id="mb_msg">' + object + '</div><div id="mb_btnbox">';
	            _html += '<input id="mb_btn_ok" type="button" value="' + btn1 + '" />';
	            _html += '<input id="mb_btn_no" type="button" value="' + btn2 + '" />';
	        }
	        if (type == "alertConfirm") {
	        	_html += '<a id="mb_ico">x</a><div id="mb_msg">' + object + '</div><div id="mb_btnbox">';
	            _html += '<input id="mb_btn_ok" type="button" value="' + btn1 + '" />';
	        }
	        if (type == "list") {
	        	$.each(object, function (index, item) {
	        		
	       			if(item.chatType == 1){
	       				if(item.exist){
	       					_html += '<div class="mb_item"><font>' + item.chatName + '(已在好友列表中)</font></div>';
	       				}else{
	        				if(item.chatId != "${user.uid}"){
	        					_html += '<div class="mb_item"><font>' + item.chatName + '</font>';
		        				_html += '<button id="apply-' + item.chatId + '" class="applyBtn" value="' + item.chatType + '">加为好友</button></div>';
	        				}else{
	        					_html += '<div class="mb_item"><font>' + item.chatName + '</font></div>';
	        				}
	       				}
	           		}else{
	           			if(item.exist){
	           				_html += '<div class="mb_item"><font>' + item.chatName + '(已在群组中)</font></div>';
	           			}else{
	           				_html += '<div class="mb_item"><font>' + item.chatName + '</font>';
	           				_html += '<button id="apply-' + item.chatId + '" class="applyBtn" value="' + item.chatType + '">申请入组</button></div>';
	           			}
	           		}
	        		
	        		if(index == object.length -1 ){
	        			_html += '<div id="mb_btnbox">';
	        		}
	        	});
	        	
	            _html += '<input id="mb_btn_ok" type="button" value="关闭" />';
	        }
	        if (type == "advices") {
	        	
	        	if(object["unreads"].length + object["reads"].length > 0 )
	        	{
	        		if(object["unreads"].length > 0 ){
	        			_html += '<div style="margin: 15px 0px 0px 0px;"><font style="color:blue;font-weight:bold; font-size:12px; padding: 5px">未读取的系统通知</font></div>';
	        			$.each(object["unreads"], function (index, item) {
		       				_html += '<div class="mb_item"><font name="' + item.content.uname + '">' + item.content.uname + item.content.text + '</font>';
		       				if(item.type == 2 || item.type == 3){
		       					_html += '<button id="approved-' + item.id + '" class="approvedBtn" value="' + item.sendId + '-' + item.recvGrpId + '">同意</button></div>';	
		       				}else{
		       					_html += '<button class="adviceBtn" >已同意</button></div>';	
		       				}
			        	});
	        		}
	        		
	        		if(object["reads"].length > 0 )
		        	{
		              	_html += '<div style="margin: 15px 0px 0px 0px;"><font style="color:gray;font-weight:bold; font-size:12px; padding: 5px">以下為历史通知</font></div>';
		        		$.each(object["reads"], function (index, item) {
		       				_html += '<div class="mb_item"><font name="' + item.content.uname + '">' + item.content.uname + item.content.text + '</font>';
		       				if(item.type == 2 || item.type == 3){
		       					_html += '<button id="approved-' + item.id + '" class="approvedBtn" value="' + item.sendId + '-' + item.recvGrpId + '">同意</button></div>';	
		       				}else{
		       					_html += '<button class="adviceBtn" >已同意</button></div>';	
		       				}
			        	});
		        	}
	        	}
	        	else
	        	{
	        		_html += '<div class="mb_item"><font style=\"color:blue;\">未有任何系统通知</font>';
	        	}
	        	
	            _html += '<div id="mb_btnbox"><input id="mb_btn_ok" type="button" value="关闭" />';
	        }
	        if (type == "createGrp") 
	        {
	        	_html += '<div>';
	        	_html += '<div class="mb_item"><input type="text" id="grp_txt" name="groupName" placeholder="請输入群组名" required="required"></div>';
	        	_html += '<div id="mb_btnbox">';
	        	_html += '<button id="newGrpBtn">创建</button>';
	        	_html += '<button id="noGrpBtn">取消</button>';
	        	_html += '</div>';
	        }
	        if (type == "showImg") {
	        	_html = '<div id="mb_box"></div><div id="mb_img"><span id="mb_tit">' + title + '</span>';
	        	_html += '<div class="mb_item">' + object + '</div>';
	        	_html += '<div id="mb_btnbox">';
	            _html += '<button id="noGrpBtn">关闭</button>';
	            _html += '</div>';
	        }
	        
	        _html += '</div></div>';
	        
	        //必须先将_html添加到body，再设置Css样式
	        $("body").append(_html);
	        //生成Css
	        GenerateCss();
	    }

	    //生成Css
	    var GenerateCss = function() {
	        $("#mb_box").css({
	            width: '100%',
	            height: '100%',
	            zIndex: '99999',
	            position: 'fixed',
	            filter: 'Alpha(opacity=60)',
	            backgroundColor: 'black',
	            top: '0',
	            left: '0',
	            opacity: '0.6'
	        });
	        $("#mb_con").css({
	            zIndex: '999999',
	            width: '400px',
	            position: 'fixed',
	            backgroundColor: 'White',
	            borderRadius: '15px'
	        });
	        $("#mb_img").css({
	            zIndex: '999999',
	            display: 'inline-block', 
	            position: 'fixed',
	            backgroundColor: 'White',
	            borderRadius: '15px'
	        });
	        $("#mb_tit").css({
	            display: 'block',
	            fontSize: '14px',
	            color: '#444',
	            padding: '10px 15px',
	            backgroundColor: '#DDD',
	            borderRadius: '15px 15px 0 0',
	            borderBottom: '3px solid #009BFE',
	            fontWeight: 'bold'
	        });
	        $("#mb_msg").css({
	            padding: '20px',
	            lineHeight: '20px',
	            borderBottom: '1px dashed #DDD',
	            fontSize: '13px'
	        });
	        $(".mb_item").css({
	            padding: '10px 20px',
	            lineHeight: '30px',
	            borderBottom: '1px dashed #DDD',
	            fontSize: '12px'
	        });
	        $("#mb_ico").css({
	            display: 'block',
	            position: 'absolute',
	            right: '10px',
	            top: '9px',
	            border: '1px solid Gray',
	            width: '18px',
	            height: '18px',
	            textAlign: 'center',
	            lineHeight: '16px',
	            cursor: 'pointer',
	            borderRadius: '12px',
	            fontFamily: '微软雅黑'
	        });
	        $("#mb_btnbox").css({
	            margin: '15px 0 10px 0',
	            textAlign: 'center'
	        });
	        
	        $(".applyBtn").css({
	            width: '85px',
	            height: '30px',
	            color: 'white',
	            border: 'none',
	            backgroundColor: '#168bbb',
	            marginRight: '15px',
	            float:'right'
	        });
	        
	        $(".approvedBtn").css({
	            width: '85px',
	            height: '30px',
	            color: 'white',
	            border: 'none',
	            backgroundColor: '#168bbb',
	            marginRight: '15px',
	            float:'right'
	        });
	        
	        $(".adviceBtn").css({
	            width: '85px',
	            height: '30px',
	            color: 'white',
	            border: 'none',
	            backgroundColor: 'gray',
	            marginRight: '15px',
	            float:'right'
	        });
	        
	        $("#mb_btn_ok,#mb_btn_no").css({
	            width: '85px',
	            height: '30px',
	            color: 'white',
	            border: 'none'
	        });
	        $("#mb_btn_ok").css({
	        	backgroundColor: 'red'
	        });
	        $("#mb_btn_no").css({
	            backgroundColor: 'gray',
	            marginLeft: '20px'
	        });
	        
	        $("#grp_txt").css({
	        	width: '65%',
	        	lineHeight: '30px',
	        	fontSize: '14px',
	        	fontFamily: '微软雅黑'
	        });
	        
	        $("#newGrpBtn,#noGrpBtn").css({
	            width: '85px',
	            height: '30px',
	            color: 'white',
	            border: 'none'
	        });
	        
	        $("#newGrpBtn").css({
	        	backgroundColor: '#168bbb',
	        });
	        
	        $("#noGrpBtn").css({
	            backgroundColor: 'gray',
	            marginLeft: '20px'
	        });
	        
	        
	        //右上角关闭按钮hover样式
	        $("#mb_ico").hover(function() {
	            $(this).css({
	                backgroundColor: 'Red',
	                color: 'White'
	            });
	        }, function() {
	            $(this).css({
	                backgroundColor: '#DDD',
	                color: 'black'
	            });
	        });
	        var _widht = document.documentElement.clientWidth; //屏幕宽
	        var _height = document.documentElement.clientHeight; //屏幕高
	        var boxWidth = $("#mb_con").width();
	        var boxHeight = $("#mb_con").height();
	        //让提示框居中
	        $("#mb_con").css({
	            top: (_height - boxHeight) / 2 + "px",
	            left: (_widht - boxWidth) / 2 + "px"
	        });
	        
	        var boxWidth = $("#mb_img").width();
	        var boxHeight = $("#mb_img").height();
	        //让提示框居中
	        $("#mb_img").css({
	            top: (_height - boxHeight) / 2 + "px",
	            left: (_widht - boxWidth) / 2 + "px"
	        });
	    }
	    //定义确定按钮事件
	    var btnOk = function(callback) {
	        $("#mb_btn_ok").click(function() {
	            $("#mb_box,#mb_con").remove();
	            if (typeof(callback) == 'function') {
	                callback();
	            }
	        });
	    }
	    //定义取消按钮事件
	    var btnNo = function() {
	        $("#mb_btn_no,#mb_ico").click(function() {
	            $("#mb_box,#mb_con").remove();
	        });
	    }
	    
	  	//定义申请按钮事件
	    var btnApply = function() {
	    	$(".applyBtn").click(function(){
	    		var chatId = $(this).attr("id").split('-')[1];
		    	var chatType = $(this).attr("value");
		    	var applyBtn = $(this);
	    		$.ajax({
					type: "post",
					url: "${pageContext.request.contextPath}/chat/validApply",
					data:{"sendId":"${user.uid}", "chatId":chatId, "chatType":chatType},
					success: 
						function(obj)
						{
							applyBtn.attr("disabled","disabled");
							applyBtn.css({backgroundColor: 'gray', fontSize: '10px'});
							if(!obj.valid){
								applyBtn.html("<i>已发送过申请，切勿频繁申请<i>");
							}else{
								applyBtn.html("已发送申请");
						    	var now = new Date();
						    	var message;
						    	var content;
						    	if(chatType == 1){
						    		content = {"uname":"${user.uname}", "text": "请求添加您为好友，是否接受？"};
							    	message = {"sendId":"${user.uid}", "recvId": chatId, "chatType": chatType, "sendTime": formatDate(new Date(), 'yyyy-MM-dd hh:mm:ss'), "content": JSON.stringify(content), "operation":"APPLY"};
						    	}else{
						    		content = {"uname":"${user.uname}", "text": "申请加入" + applyBtn.siblings("font").html() + "，是否接受？"};
						    		message = {"sendId":"${user.uid}", "recvGrpId": chatId, "chatType": chatType, "sendTime": formatDate(new Date(), 'yyyy-MM-dd hh:mm:ss'), "content": JSON.stringify(content), "operation":"APPLY"};
						    	}
						    	ws.send(JSON.stringify(message));
							}
						},
					dataType: "json",
					async: true	
				});
	    	});
	    }
	  	
	  	//定义同意按钮事件
	    var btnApproved = function() {
	    	$(".approvedBtn").click(function(){
	    		var chatId = $(this).attr("id").split('-')[1];
		    	$(this).attr("disabled","disabled");
	    		$(this).html("已同意");
	    		$(this).css({backgroundColor: 'gray', fontSize: '10px'});
	    		var applyId = $(this).attr("id").split('-')[1];
	    		var chatId = $(this).attr("value").split('-')[0];
		    	var recvGrpId = $(this).attr("value").split('-')[1];
		    	var chatType;
		    	var content;
		    	var message;
		    	if(isEmpty(recvGrpId))
		    	{
		    		content = {"uname":"${user.uname}", "text": "<i>我已通过您的好友申请，现在可以开始聊天了<i>"};
		    		$.post(
	    				"${pageContext.request.contextPath}/chat/addFriend",
	    				{"applyId":chatId, "approveId":"${user.uid}"},
	    				function(data)
	    				{
	    					if(data == "success")
	    					{
	    						message = {"sendId":"${user.uid}", "recvId": chatId, "chatType": 1, "sendTime": formatDate(new Date(), 'yyyy-MM-dd hh:mm:ss'), "content": JSON.stringify(content), "operation":"APPROVED-" + applyId};
	    						ws.send(JSON.stringify(message));
	    						reloadPageInfo();
	    					}
	    				},
	    				"text"
	    			);
		    	}
		    	else
		    	{
		    		var applyName = $(this).siblings("font").attr("name");
		    		content = {"uname":"", "text": "<i>" + applyName + "已加入群聊<i>"};
		    		$.post(
	    				"${pageContext.request.contextPath}/chat/joinGrp",
	    				{"uid":chatId, "gid":recvGrpId},
	    				function(data)
	    				{
	    					if(data.success)
	    					{
	    						message = {"sendId":"${user.uid}", "recvGrpId": recvGrpId, "chatType": 2, "sendTime": formatDate(new Date(), 'yyyy-MM-dd hh:mm:ss'), "content": JSON.stringify(content), "operation":"APPROVED-" + applyId};
	    						ws.send(JSON.stringify(message));
	    						reloadPageInfo();
	    					}
	    				},
	    				"json"
	    			);
		    	}
	    	});
	    }
	    
	  	//定义取消創建群組按钮事件
	    var btnNoGrp = function() {
	        $("#noGrpBtn").click(function() {
	            $("#mb_box,#mb_con,#mb_img").remove();
	        });
	    }
	    
	  	//定义創建群組按钮事件
	    var btnNewGrp = function() {
	    	$("#newGrpBtn").click(function(){
	    		$("#grp_txt").siblings().remove();
	    		if(isEmpty($("#grp_txt").val())){
	    			$("#grp_txt").parent().append('<label style="color: red">不允许为空</label>');
	    		}
	    		else
	    		{
	    			$.post(
    					"${pageContext.request.contextPath}/group/create",
    					{"groupName": $.trim($("#grp_txt").val())},
    					function(obj)
    					{
    						if(obj.success){
    							$("#mb_box,#mb_con").remove();
    							alert(obj.info);
    							reloadPageInfo();
    						}else{
    							$("#grp_txt").parent().append('<label style="color: red">' + obj.info + '</label>');
    						}
    					},
    					"json"
    				);
	    		}
	    	});
	    }
	    
	})();
</script>
</html>
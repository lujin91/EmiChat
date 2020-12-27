<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.9.1.min.js"></script>
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
					<div id="inputTextArea" contenteditable="true" ></div>
				</div>
				<div class="inputBtnDiv">
					<span class="inlineSpan"></span>
					<input id="sendMsgBtn" type="button" value="发送" onclick="sendMsg()" disabled="disabled" autocomplete="off"/>
					<input id="clearInputBtn" type="button" value="清空" onclick="clearInput()"/>
				</div>
			</div>
		</div>
	</div>
</body>

<script type="text/javascript">
	document.querySelector('#inputTextArea').addEventListener('paste', function(e){
		var cbd = e.clipboardData;
		var ua = window.navigator.userAgent;
		//如果是Safari直接return
		if(!(e.clipboardData && e.clipboardData.items)){
			return ;
		}
		//Mac平台Chrome49版本以下复制Finder中的文件的Bug Hack掉
		if(cbd.items && cbd.items.length === 2 && cbd.items[0].kind === "string" && cbd.items[1].kind === "file" && 
				cbd.types && cbd.types.length === 2 && cbd.types[0] === "text/plain" && cbd.types[1] === "Files" && 
				ua.match(/Macintosh/i) && Number(ua.match(/Chrome\/(\d{2})/i)[1]) < 49){
			return ;
		}
		
		var isFirefox = navigator.userAgent.toUpperCase().indexOf("FIREFOX") > -1 ? true : false;
		
		for(var i = 0; i < cbd.items.length; i++){
			var item = cbd.items[i];
			if(item.kind == "file"){
				var blob = item.getAsFile();
				if(blob.size === 0){
					return;
				}
				var reader = new FileReader();
				var imgs = new Image();
				imgs.file = blob;
				if(isFirefox){
					reader.onload = (function(aImg){
						return function(e){
							aImg.src = e.target.result;
						};
					});
				}else{
					reader.onload = (function(aImg){
						return function(e){
							aImg.src = e.target.result;
						};
					})(imgs);
				}
				reader.readAsDataURL(blob);
				document.querySelector('#inputTextArea').appendChild(imgs);
			}
		}
	}, false);
	
	function insertHtmlAtCaret(childElement){
		var sel, range;
		
		if(window.getSelection){
			sel = window.getSelection();
			if(sel.getRangeAt && sel.rangeCount){
				range = sel.getRangeAt(0);
				range.deleteContents();
				var el = document.createElement("div");
				el.appendChild(childElement);
				var frag = document.createDocumentFragment(), node, lastNode;
				while((node = el.firstchild)){
					lastNode = frag.appendChild(node);
				}
				
				range.insertNode(frag);
				if(lastNode){
					range = range.cloneRange();
					range.setStartAfter(lastNode);
					range.collapse("true")
					sel.removeAllRanges();
					sel.addRange(range);
				}
			}
		}else if (document.selection && document.selection.type != "Control"){}
	}
</script>
</html>
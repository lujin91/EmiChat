<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<div class="relationWin">
	<div class="relationDiv">
		<div class="searchDiv">
            <input id="searchInput" type="text" placeholder="查找联系人/群组..."/>
            <button id="searchBtn"><i>搜索</i></button>
        </div>
		<div class="tab-menu">
			<ul>
				<li><a href="javascript:void(0);">好友</a></li>
				<li><a href="javascript:void(0);">分组</a></li>
			</ul>
		</div>
		<div class="tab-content">
			<div id="personTab" class="chatItemTab">
				<c:forEach items="${relations}" var="rs">
					<a id="${rs.cid}" class="chatItem" href="javascript:void(0);">
						<c:choose>
							<c:when test="${rs.online}">
								<img src="${pageContext.request.contextPath}/images/online_user.png"/>
							</c:when>
							<c:otherwise>
								<img src="${pageContext.request.contextPath}/images/offline_user.png"/>
							</c:otherwise>
						</c:choose>
						<font>${rs.remarkName}</font>
					</a>
				</c:forEach>
			</div>
			<div id="groupTab" class="chatItemTab">
				<c:forEach items="${groups}" var="grp">
					<a id="${grp.gid}" class="chatItem" href="javascript:void(0);">
						<img src="${pageContext.request.contextPath}/images/grp.png"/>
						<font>${grp.groupName}</font>
					</a>
				</c:forEach>
			</div>
		</div>
		<div class="adviceDiv">
			<a id="grpOper" href="javascript:void(0);"><img id="grpImg" src="${pageContext.request.contextPath}/images/grp_icn.png"><font>创建新群</font></a>
			<a id="advice" href="javascript:void(0);"><img id="adviceImg" src="${pageContext.request.contextPath}/images/system_news.png"><font id="adviceContent">通知 (未读: <span id="unreadCnt">${fn:length(advices["unreads"])}</span>)</font></a>
		</div>
	</div>
</div>
</body>
</html>
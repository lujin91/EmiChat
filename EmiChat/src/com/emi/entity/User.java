package com.emi.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import javax.websocket.Session;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.emi.web.websocket.ChatServerEndpoint;

public class User implements Serializable, HttpSessionBindingListener{

	private static final Logger log = LoggerFactory.getLogger(User.class);
	
	private static final long serialVersionUID = 1L;

	private String uid;
	
	private String uname;
	
	private String password;
	
	private String password2;
	
	private String headImg;
	
	private String emailAddr;
	
	private String nickname;
	
	private Date birthday;
	
	private Integer status;
	
	private List<RelationShip> relationList;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public String getEmailAddr() {
		return emailAddr;
	}

	public void setEmailAddr(String emailAddr) {
		this.emailAddr = emailAddr;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public List<RelationShip> getRelationList() {
		return relationList;
	}

	public void setRelationList(List<RelationShip> relationList) {
		this.relationList = relationList;
	}

	@Override
	public String toString() {
		return "User [uid=" + uid + ", uname=" + uname + ", password="
				+ password + ", headImg=" + headImg + ", emailAddr="
				+ emailAddr + ", nickname=" + nickname + ", birthday="
				+ birthday + ", status=" + status + "]";
	}

	@Override
	public void valueBound(HttpSessionBindingEvent event) {
		
		log.info(this.uid + " trigger valueBound()");
		
		HttpSession oldSession = (HttpSession) event.getSession().getServletContext().getAttribute(this.uid);
		event.getSession().getServletContext().setAttribute(this.uid, event.getSession());
		if(oldSession != null)
		{
			Session session = (Session) oldSession.getAttribute("websocketSession");
			if(session.isOpen()){
				ChatServerEndpoint.alert(session, "WARN:您的账号在其它地方登录，您已被迫下线！");
				IOUtils.closeQuietly(session);
			}
			oldSession.invalidate();
		}
	}

	@Override
	public void valueUnbound(HttpSessionBindingEvent event) {
		
		log.info(this.uid + " trigger valueUnbound()");
		
		HttpSession currentSession = (HttpSession) event.getSession().getServletContext().getAttribute(this.uid);
		if(currentSession == event.getSession())
		{
			Session session = ChatServerEndpoint.getClients().get(this.uid);
			if(session != null && session.isOpen()){
				IOUtils.closeQuietly(session);
			}
			ChatServerEndpoint.clearWebSocket(this.uid);
		}
		event.getSession().getServletContext().removeAttribute(this.uid);
	}
}

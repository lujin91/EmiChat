package com.emi.util;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeUtility;

public class MailUtils {

	public static void sendMail(final String address, final String content)
	{
		new Thread(){

			@Override
			public void run() {
				try {
					process(address, content);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	private static void process(String address, String content)
			throws Exception {
		// 1.创建一个程序与邮件服务器会话对象 Session

		Properties props = new Properties();
		//设置发送的协议
		props.setProperty("mail.transport.protocol", "SMTP");
		
		//设置发送邮件的服务器
		props.setProperty("mail.host", "smtp.qq.com");
		props.setProperty("mail.smtp.port", "587");
		props.setProperty("mail.smtp.auth", "true");// 指定验证为true

		// 创建验证器
		Authenticator auth = new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				//设置发送人的帐号和密码
				return new PasswordAuthentication("492977881@qq.com", "hdqrrvmehnuhbgee");
			}
		};

		Session session = Session.getInstance(props, auth);

		// 2.创建一个Message，它相当于是邮件内容
		Message message = new MimeMessage(session);

		//设置发送者
		message.setFrom(new InternetAddress(MimeUtility.encodeText("Emi WebChat") + "<492977881@qq.com>"));

		//设置发送方式与接收者
		message.setRecipient(RecipientType.TO, new InternetAddress(address)); 

		//设置邮件主题
		message.setSubject("用户激活");
		 
		//设置邮件内容
		message.setContent(content, "text/html;charset=utf-8");

		// 3.创建 Transport用于将邮件发送
		Transport.send(message);
	}
}

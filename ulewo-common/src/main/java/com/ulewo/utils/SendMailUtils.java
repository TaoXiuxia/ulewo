package com.ulewo.utils;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMailUtils {

	public static void sendEmail(String sendEmail, String sendEmailPwd, String title, String content,
			String[] toEmailAddress) throws Exception {
		Properties props = new Properties();
		props.setProperty("mail.smtp.auth", "true");
		props.setProperty("mail.transport.protocol", "smtp");
		Session session = Session.getInstance(props);
		session.setDebug(true);
		Message msg = new MimeMessage(session);
		// 发送邮件的地址
		msg.setFrom(new InternetAddress(sendEmail));
		// 设置标题
		msg.setSubject(title);
		// 设置内容
		msg.setContent(content, "text/html;charset=gbk");
		Transport transport = session.getTransport();
		// 设置服务器以及帐号和密码
		transport.connect("smtp.qq.com", 25, sendEmail, sendEmailPwd);
		// 发送到的邮箱地址
		transport.sendMessage(msg, getAddress(toEmailAddress));
		transport.close();
	}

	private static Address[] getAddress(String[] emailAddress) throws Exception {
		Address[] address = new Address[emailAddress.length];
		for (int i = 0; i < address.length; i++) {
			address[i] = new InternetAddress(emailAddress[i]);
		}
		return address;
	}
}

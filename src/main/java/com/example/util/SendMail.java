package com.example.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

public class SendMail {
	private String host; // smtp服务器
	private String user; // 用户名
	private String pwd; // 密码
	private String from; // 发件人地址
	private String to; // 收件人邮箱
	private String subject; // 邮件标题
	private String content;// 邮件内容
	private boolean isAddAttachment = false;
	private File[] attachments;
	private Properties properties = new Properties();

	public SendMail(String to, String subject, String content) {
		InputStream in = SendMail.class
				.getResourceAsStream("/mailserver.properties");
		try {
			properties.load(in);
			this.host = properties.getProperty("mail.smtp.host");
			this.user = properties.getProperty("mail.sender.username");
			this.pwd = properties.getProperty("mail.sender.password");
			this.from = properties.getProperty("mail.sender.from");
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.to = to;
		this.subject = subject;
		this.content = content;
	}

	public SendMail(String to, String subject, String content,
			boolean isAddAttachment,File[] attachments) {
		this(to, subject, content);
		this.isAddAttachment = true;
		this.attachments = attachments;
	}

	public void send() {
		// 用刚刚设置好的props对象构建一个session
		Session session = Session.getDefaultInstance(properties);
		// 有了这句便可以在发送邮件的过程中在console处显示过程信息，供调试使
		// 用（你可以在控制台（console)上看到发送邮件的过程）
		session.setDebug(true);
		// 用session为参数定义消息对象
		MimeMessage message = new MimeMessage(session);
		try {
			// 加载发件人地址
			message.setFrom(new InternetAddress(from));
			// 加载收件人地址
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					to));
			// 加载标题
			message.setSubject(subject);
			// 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
			Multipart multipart = new MimeMultipart();
			// 设置邮件的文本内容
			BodyPart contentPart = new MimeBodyPart();
			contentPart.setText(content);
			multipart.addBodyPart(contentPart);
			if (isAddAttachment) {
				addAttachment(multipart,message);
			}
			// 将multipart对象放到message中
			message.setContent(multipart);
			// 保存邮件
			message.saveChanges();
			// 发送邮件
			Transport transport = session.getTransport("smtp");
			// 连接服务器的邮箱
			transport.connect(host, user, pwd);
			// 把邮件发送出去
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addAttachment(Multipart multipart, MimeMessage message){
		// 添加附件
		BodyPart messageBodyPart = new MimeBodyPart();
		if (attachments.length >= 1) {
			BodyPart attachmentBodyPart = new MimeBodyPart();
			for (File attachment : attachments) {
				DataSource source = new FileDataSource(attachment);
				try {
					attachmentBodyPart.setDataHandler(new DataHandler(source));
					sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
					messageBodyPart.setFileName("=?GBK?B?"
							+ enc.encode(attachment.getName().getBytes())
							+ "?=");
					// MimeUtility.encodeWord可以避免文件名乱码
					attachmentBodyPart.setFileName(MimeUtility
							.encodeWord(attachment.getName()));
					multipart.addBodyPart(attachmentBodyPart);
					// 将multipart对象放到message中
					message.setContent(multipart);
				} catch (MessagingException e) {
					e.printStackTrace();
				} catch (UnsupportedEncodingException ee) {
					ee.printStackTrace();
				}
			}
		}

	}

	public static void main(String[] args) {
		String to = "fukaimingnick@163.com";
		String subject = "邮件测试";
		String contents = "用Java发送邮件";
		File[] attachments = new File[2];
		File file = new File("/Users/fukaiming/Desktop/a.txt");
		File file2 = new File("/Users/fukaiming/Desktop/b.txt");
		attachments[0] = file;
		attachments[1] = file2;
		boolean isAddAttachment = true;
		
		SendMail sm = new SendMail(to, subject, contents,isAddAttachment,attachments);
		sm.send();
	}
}

package com.example.todolist.service;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:email.properties")
public class MailService {
	
	private final JavaMailSender javaMailSender;
	
	@Value("${email.sender}")
	private String senderEmail;
	
	private MimeMessage createMessage(String email) throws MessagingException, UnsupportedEncodingException {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		mimeMessage.addRecipients(Message.RecipientType.TO, email);
		mimeMessage.setSubject("TODO-LIST Check-Email");
		// email과 randomText를 Redis에 저장한다.
		String randomText = UUID.randomUUID().toString().substring(0, 8);
		String content = "<h1>TODO-LIST : CHECK-EMAIL</h1>";
		content += "<div>Authentication TEXT : " + randomText + "</div>";
		mimeMessage.setText(content, "utf-8", "html");
		mimeMessage.setFrom(new InternetAddress(senderEmail, "TODO-LIST ADMIN"));
		return mimeMessage;
	}
	
	public void sendEmail(String email) {
		try {
			MimeMessage message = createMessage(email);
			javaMailSender.send(message);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
}

package com.example.todolist.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@PropertySource("classpath:email.properties")
public class MailConfig {

	@Value("${email.id}")
	private String mailId;
	@Value("${email.pwd}")
	private String mailPwd;
	@Value("${email.port}")
	private int mailPort;
	
	@Bean
	public JavaMailSender javaMailSender() {
		JavaMailSenderImpl javaMailSenderImpl = new JavaMailSenderImpl();
		
		// NAVER
		javaMailSenderImpl.setHost("smpt.naver.com");
		javaMailSenderImpl.setUsername(mailId);
		javaMailSenderImpl.setPassword(mailPwd);
		javaMailSenderImpl.setPort(mailPort);
		javaMailSenderImpl.setJavaMailProperties(getMailProperties());
		
		return javaMailSenderImpl;
	}

	private Properties getMailProperties() {
    	Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.debug", "true");
        properties.setProperty("mail.smtp.ssl.trust","smtp.naver.com");
        properties.setProperty("mail.smtp.ssl.enable","true");
        return properties;
    }
	
}

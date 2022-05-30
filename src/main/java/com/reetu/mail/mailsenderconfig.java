package com.reetu.mail;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

@Component
public class mailsenderconfig {

	     @Bean
	     public JavaMailSender getconfig() {
	    	 JavaMailSenderImpl mailsender=new JavaMailSenderImpl();
	    	 mailsender.setHost("smtp.gmail.com");
	    	 mailsender.setPort(587);
	    	 mailsender.setUsername(mailconstant.ADMIN_EMAIL);
	    	 mailsender.setPassword(mailconstant.ADMIN_PASSWORD);
	    	 
	    	 Properties prop=mailsender.getJavaMailProperties();
	    	 prop.put("mail.smtp.auth", "true");
	    	 prop.put("mail.smtp.starttls.enable", "true");
	    	 return mailsender;
	    	 
	     }
}

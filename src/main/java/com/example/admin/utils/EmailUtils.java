package com.example.admin.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class EmailUtils {

	
	@Autowired
	private JavaMailSender javaMailSender;
	
	
	 @Autowired
	    private ResourceLoader resourceLoader;
	public boolean emailSending(String email, String tempPassword ) throws MessagingException, IOException {
		 boolean flag=false;
		
		 try {
		Resource resource = resourceLoader.getResource("classpath:templates/password-reset.html");
	        String emailContent = new String(Files.readAllBytes(Paths.get(resource.getURI())));

	        // Replace placeholder with the actual temporary password
	        emailContent = emailContent.replace("{{tempPassword}}", tempPassword);

	        // Create email message
	        MimeMessage message = javaMailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message, true);

	        helper.setTo(email);
	        helper.setSubject("IES - Password Reset");
	        helper.setText(emailContent, true); // true -> enables HTML format

	        // Send the email
	       javaMailSender.send(message);
	       flag=true;
	  } catch (IOException e) {
	        throw new RuntimeException("Failed to load email template", e);
	    }
	       return flag;
	}
}

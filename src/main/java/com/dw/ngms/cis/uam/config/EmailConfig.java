package com.dw.ngms.cis.uam.config;

import com.dw.ngms.cis.im.service.ApplicationPropertiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
@Configuration
public class EmailConfig {
	


    @Autowired
    private ApplicationPropertiesService appPropertiesService;
	
	@Bean
    public JavaMailSender getMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();


        mailSender.setHost(appPropertiesService.getProperty("EMAIL_HOST").getKeyValue());
        mailSender.setPort(Integer.valueOf(appPropertiesService.getProperty("EMAIL_PORT").getKeyValue()));
        mailSender.setUsername(appPropertiesService.getProperty("EMAIL_USERNAME").getKeyValue());
        mailSender.setPassword(appPropertiesService.getProperty("EMAIL_PASSWORD").getKeyValue());

        System.out.println("Host is "+mailSender.getHost());
        System.out.println("EMAIL_USERNAME is "+mailSender.getUsername());
        System.out.println("EMAIL_PASSWORD "+mailSender.getPassword());
        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.starttls.enable", "true");
        javaMailProperties.put("mail.smtp.ssl.enable", "false");
        javaMailProperties.put("mail.smtp.ssl.enable", "false");
        javaMailProperties.put("mail.smtp.socketFactory.fallback", "false");
        javaMailProperties.put("mail.smtp.auth", appPropertiesService.getProperty("EMAIL_AUTH").getKeyValue());
        javaMailProperties.put("mail.transport.protocol", "smtp");
        javaMailProperties.put("mail.default-encoding", "UTF-8");
        javaMailProperties.put("mail.debug", "true");
 
        mailSender.setJavaMailProperties(javaMailProperties);
        return mailSender;
    }
 
    
}

package com.bugsfree.emailsender.config;

import lombok.RequiredArgsConstructor;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
@PropertySource("classpath:emailsender.properties")
public class EmailConfigs {

    private final Environment environment;

    @Bean
    public VelocityContext getVelocityContext() {
        return new VelocityContext();
    }

    @Bean
    public VelocityEngine getVelocityEngine() {
        return new VelocityEngine();
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(environment.getProperty("mail.hostname"));
        mailSender.setPort(environment.getProperty("mail.port", Integer.class));

        mailSender.setUsername(environment.getProperty("mail.username"));
        mailSender.setPassword(environment.getProperty("mail.password"));

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", environment.getProperty("mail.properties.mail.smtp.auth"));
        props.put("mail.smtp.starttls.enable", environment.getProperty("mail.properties.mail.smtp.starttls.enable"));
        props.put("mail.debug", "true");

        return mailSender;
    }
}

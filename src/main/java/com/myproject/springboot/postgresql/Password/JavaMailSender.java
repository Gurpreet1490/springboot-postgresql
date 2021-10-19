//package com.myproject.springboot.postgresql.Password;
//
//import org.springframework.context.annotation.Bean;
//
//import java.util.Properties;
//
//public class JavaMailSender {
//
//    @Bean
//    public JavaMailSender getJavaMailSender() {
//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//        mailSender.setHost("smtp.gmail.com");
//        mailSender.setPort(587);
//
//        mailSender.setUsername("preetwebsite14@gmail.com");
//        mailSender.setPassword("@Nokia6680");
//
//        Properties props = mailSender.getJavaMailProperties();
//        props.put("mail.transport.protocol", "smtp");
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.debug", "true");
//
//        return mailSender;
//    }
//}

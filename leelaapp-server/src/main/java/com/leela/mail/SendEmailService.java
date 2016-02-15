package com.leela.mail;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;

import com.leela.configuration.APIConfiguration;
import com.leela.model.SimpleMailMessage;

public class SendEmailService {

    private final APIConfiguration sendEmailConfiguration;

    public SendEmailService(final APIConfiguration sendEmailConfiguration) {
        this.sendEmailConfiguration = sendEmailConfiguration;
    }

    public void send(final SimpleMailMessage email) throws MessagingException {
        try {
            final Properties properties = System.getProperties();
            properties.put("mail.smtp.auth", "true");
            properties.setProperty("mail.smtp.user",
                    sendEmailConfiguration.getHealthCheckEmailAddress());
            properties.setProperty("mail.smtp.password",
                    sendEmailConfiguration.getPassword());
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host",
                    sendEmailConfiguration.getSmtpHost());
            properties.put("mail.smtp.port",
                    sendEmailConfiguration.getSmtpPort());

            final Session session = Session.getInstance(properties,
                    new javax.mail.Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(
                                    sendEmailConfiguration
                                            .getHealthCheckEmailAddress(),
                                    sendEmailConfiguration.getPassword());
                        }
                    });
            Transport.send(MimeMessageBuilder.buildMessage(email, session));
        } catch (final MessagingException mex) {
            throw mex;
        }
    }
}

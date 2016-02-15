package com.leela.mail;

import java.util.ArrayList;
import java.util.List;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.leela.model.SimpleMailMessage;

public class MimeMessageBuilder {

    private static final String DEFAULT_CHARSET = "ISO-8859-1";

    private static final String DEFAULT_SUBTYPE = "html";

    public static MimeMessage buildMessage(
            final SimpleMailMessage simpleMailMessage, final Session session)
                    throws MessagingException {

        if ((simpleMailMessage.getTo() == null
                || simpleMailMessage.getTo().length == 0)
                && (simpleMailMessage.getBcc() == null
                        || simpleMailMessage.getBcc().length == 0)) {
            throw new MessagingException("Email needs one or more recipients");
        }

        final List<InternetAddress> toEmailAddresses = new ArrayList<InternetAddress>();
        if (simpleMailMessage.getTo() != null
                && simpleMailMessage.getTo().length > 0) {
            for (final String toEmailAddress : simpleMailMessage.getTo()) {
                try {
                    toEmailAddresses.add(new InternetAddress(toEmailAddress));
                } catch (final AddressException e) {
                    throw e;
                }
            }
        }

        final MimeMessage message = new MimeMessage(session);
        message.setFrom(simpleMailMessage.getFrom());
        message.addRecipients(Message.RecipientType.TO, toEmailAddresses
                .toArray(new InternetAddress[toEmailAddresses.size()]));

        if (simpleMailMessage.getCc() != null) {
            for (final String cc : simpleMailMessage.getCc()) {
                message.addRecipients(Message.RecipientType.CC, cc);
            }
        }

        if (simpleMailMessage.getBcc() != null) {
            for (final String bcc : simpleMailMessage.getBcc()) {
                message.addRecipients(Message.RecipientType.BCC, bcc);
            }
        }

        message.setSubject(simpleMailMessage.getSubject());

        final String htmlText = simpleMailMessage.getText().replace("\n",
                "<br>");
        message.setText(htmlText, DEFAULT_CHARSET, DEFAULT_SUBTYPE);
        return message;
    }
}

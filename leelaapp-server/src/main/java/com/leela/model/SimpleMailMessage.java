package com.leela.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

public class SimpleMailMessage implements Serializable {

    private static final long serialVersionUID = 7908798321398534117L;

    private String from;

    private String replyTo;

    private String[] to;

    private String[] cc;

    private String[] bcc;

    private Date sentDate;

    private String subject;

    private String text;

    public String getFrom() {
        return from;
    }

    public void setFrom(final String from) {
        this.from = from;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(final String replyTo) {
        this.replyTo = replyTo;
    }

    public String[] getTo() {
        return to;
    }

    public void setTo(final String[] to) {
        this.to = to;
    }

    public String[] getCc() {
        return cc;
    }

    public void setCc(final String[] cc) {
        this.cc = cc;
    }

    public String[] getBcc() {
        return bcc;
    }

    public void setBcc(final String[] bcc) {
        this.bcc = bcc;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(final Date sentDate) {
        this.sentDate = sentDate;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(final String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final SimpleMailMessage that = (SimpleMailMessage) o;

        if (!Arrays.equals(bcc, that.bcc)) {
            return false;
        }
        if (!Arrays.equals(cc, that.cc)) {
            return false;
        }
        if (from != null ? !from.equals(that.from) : that.from != null) {
            return false;
        }
        if (replyTo != null ? !replyTo.equals(that.replyTo)
                : that.replyTo != null) {
            return false;
        }
        if (sentDate != null ? !sentDate.equals(that.sentDate)
                : that.sentDate != null) {
            return false;
        }
        if (subject != null ? !subject.equals(that.subject)
                : that.subject != null) {
            return false;
        }
        if (text != null ? !text.equals(that.text) : that.text != null) {
            return false;
        }
        if (!Arrays.equals(to, that.to)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = from != null ? from.hashCode() : 0;
        result = 31 * result + (replyTo != null ? replyTo.hashCode() : 0);
        result = 31 * result + (to != null ? Arrays.hashCode(to) : 0);
        result = 31 * result + (cc != null ? Arrays.hashCode(cc) : 0);
        result = 31 * result + (bcc != null ? Arrays.hashCode(bcc) : 0);
        result = 31 * result + (sentDate != null ? sentDate.hashCode() : 0);
        result = 31 * result + (subject != null ? subject.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        return result;
    }
}
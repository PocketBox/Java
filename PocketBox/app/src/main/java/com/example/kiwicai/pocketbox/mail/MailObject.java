package com.example.kiwicai.pocketbox.mail;

import java.io.Serializable;
import java.util.Date;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;

/**
 * Created by tao-macbook on 4/4/16.
 */
public class MailObject implements Serializable{
    private String subject;
    private Date date;
    private Address[] sender;
    private Address[] receiver;
    private StringBuffer bodyText = new StringBuffer();

    public MailObject(Message message) {
        try {
            this.subject = message.getSubject();
            this.date = message.getReceivedDate();
            this.sender = message.getFrom();
            this.receiver = message.getAllRecipients();
//            this.content = message.getContent();
            this.getMailContent(message);
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getSubject() {
        return this.subject;
    }

    public Date getDate() {
        return this.date;
    }

    public Address[] getSender() {
        return this.sender;
    }

    public Address[] getReceiver() {
        return this.receiver;
    }

    public String getContent() {
        return this.bodyText.toString();
    }

    private void getMailContent(Part part) throws Exception {
        String contentType = part.getContentType();
        int nameIndex = contentType.indexOf("name");
        boolean conName = false;
        if (nameIndex != -1)
            conName = true;
        if (part.isMimeType("text/plain") && !conName) {
            bodyText.append(part.getContent());
        } else if (part.isMimeType("text/html") && !conName) {
            bodyText.append(part.getContent());
        } else if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            int counts = multipart.getCount();
            for (int i = 0; i < counts; i++) {
                getMailContent(multipart.getBodyPart(i));
            }
        } else if (part.isMimeType("message/rfc822")) {
            getMailContent((Part) part.getContent());
        } else {
        }
    }
}

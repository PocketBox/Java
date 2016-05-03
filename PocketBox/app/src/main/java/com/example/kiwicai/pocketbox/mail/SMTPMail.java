package com.example.kiwicai.pocketbox.mail;

import android.app.Activity;
import android.os.AsyncTask;

import java.util.Date;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Created by tao-macbook on 5/2/16.
 */
public class SMTPMail {

    Activity mainActivity;

    public void sendMail(Activity mainActivity,  String... urls) {
        this.mainActivity = mainActivity;
        new AsyncMail().execute(urls);
    }

    private class AsyncMail extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            return sendMail(params[0], params[1], params[2], params[3], params[4], params[5], params[6]);
        }

        protected void onPostExecute(Boolean result) {
        }

        private Boolean sendMail(String user, String password, String host, String receiver, String sender, String subject, String content) {

            Properties props = new Properties();
            props.put("mail.smtps.auth", "true");
            Session session = Session.getDefaultInstance(props);
            MimeMessage msg = new MimeMessage(session);
            try {
                Transport transport = session.getTransport("smtps");
                transport.connect(host, user, password);    //建立与服务器连接
                msg.setSentDate(new Date());
                InternetAddress fromAddress = new InternetAddress(sender);
                msg.setFrom(fromAddress);
                InternetAddress[] toAddress = new InternetAddress[1];
                toAddress[0] = new InternetAddress(receiver);
                msg.setRecipients(Message.RecipientType.TO, toAddress);
                msg.setSubject(subject, "UTF-8");            //设置邮件标题
                MimeMultipart multi = new MimeMultipart();   //代表整个邮件邮件
                BodyPart textBodyPart = new MimeBodyPart();  //设置正文对象
                textBodyPart.setText(content);                  //设置正文
                multi.addBodyPart(textBodyPart);             //添加正文到邮件
//                for (String path : fileList) {
//                    FileDataSource fds = new FileDataSource(fileInfo.absolutePath);   //获取磁盘文件
//                    BodyPart fileBodyPart = new MimeBodyPart();                       //创建BodyPart
//                    fileBodyPart.setDataHandler(new DataHandler(fds));           //将文件信息封装至BodyPart对象
//                    String fileNameNew = MimeUtility.encodeText(fds.getName(),
//                            "utf-8", null);      //设置文件名称显示编码，解决乱码问题
//                    fileBodyPart.setFileName(fileNameNew);  //设置邮件中显示的附件文件名
//                    multi.addBodyPart(fileBodyPart);        //将附件添加到邮件中
//                }
                msg.setContent(multi);                      //将整个邮件添加到message中
                msg.saveChanges();
                transport.sendMessage(msg, msg.getAllRecipients());  //发送邮件
                transport.close();
            } catch (NoSuchProviderException e) {
                e.printStackTrace();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return true;
        }
    }
}

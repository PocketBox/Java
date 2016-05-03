package com.example.kiwicai.pocketbox.mail;

import android.app.Activity;
import android.os.AsyncTask;

import com.example.kiwicai.pocketbox.ui.MailDefaultBoxFragment;
import com.sun.mail.imap.IMAPStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;


/**
 * Created by tao-macbook on 4/3/16.
 */
public class IMAPMail {

    Activity mainActivity = null;
    MailDefaultBoxFragment emailFragment = null;

//    public void getMails(Context mainActivity, String... urls) {
    public void getMails(Activity mainActivity, MailDefaultBoxFragment emailFragment, String... urls) {
        this.mainActivity = mainActivity;
        this.emailFragment = emailFragment;
        new AsyncMail().execute(urls);
    }

    private class AsyncMail extends AsyncTask<String, Void, List<MailObject>> {

        @Override
        protected List<MailObject> doInBackground(String... params) {
            return getMails(params[0], params[1], params[2], params[3]);
        }

        protected void onPostExecute(List<MailObject> mails) {
            emailFragment.getMailReady(mainActivity, mails);
        }


        private List<MailObject> getMails(String user, String password, String host, String folderChoice) {
            Properties prop = new Properties();
            prop.put("mail.store.protocol", "imaps");
            prop.put("mail.imap.host", host);
            Session session = Session.getInstance(prop);
            // build the store object of the imap protocol
            IMAPStore store;
            try {
                store = (IMAPStore) session.getStore("imaps");
                // connect to the imap server
                store.connect(host, user, password);
                // get the inbox mail
                Folder folder = store.getFolder(folderChoice);
//                Folder[] temp = folder.getStore().getPersonalNamespaces();
                // open the INBOX in write and read mode
                folder.open(Folder.READ_WRITE);
                // get the mail list of the INBOX
                Message[] messages = folder.getMessages();
                List<MailObject> mails = new ArrayList<>();
//                for(Message message: messages) {
//                    mails.add(new MailObject(message));
//                }
                mails.add(new MailObject(messages[0]));
                mails.add(new MailObject(messages[1]));
                mails.add(new MailObject(messages[2]));
//                mails.add(new MailObject(messages[3]));
//                mails.add(new MailObject(messages[4]));
                return mails;
            } catch (NoSuchProviderException e) {
                e.printStackTrace();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

package com.example.kiwicai.pocketbox.ui;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;


import com.example.kiwicai.pocketbox.R;
import com.example.kiwicai.pocketbox.mail.SMTPMail;
import com.example.kiwicai.pocketbox.proxy.LoginConstant;
import com.example.kiwicai.pocketbox.proxy.MailSendConstant;

import java.text.SimpleDateFormat;

/**
 * Created by tao-macbook on 5/1/16.
 */
//public class MailSendActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener, MailSendConstant, LoginConstant {
public class MailSendActivity extends AppCompatActivity implements MailSendConstant, LoginConstant {

    private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

    Activity main = this;
    EditText subject, from, to, content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mail_send_activity);

        subject = (EditText) findViewById(R.id.mail_send_subject);
        from = (EditText) findViewById(R.id.mail_send_from);
        to = (EditText) findViewById(R.id.mail_send_to);
        content = (EditText) findViewById(R.id.mail_send_content);

        Bundle bundle = getIntent().getExtras();
        String method = bundle.getString(METHOD);
        switch(method) {
            case NEW:
                SharedPreferences pref = getSharedPreferences(SHARED_PREFERENCE_EMAIL, MODE_PRIVATE);
                String username = pref.getString(USERNAME, "");
                from.setText(username + "@andrew.cmu.edu");
                break;
            case REPLY:
                subject.setText(bundle.getString(SUBJECT));
                to.setText(bundle.getString(RECEIVER));
                from.setText(bundle.getString(SENDER));
                content.setText(bundle.getString(CONTENT));
                break;
            case FORWARD:
                subject.setText(bundle.getString(SUBJECT));
                from.setText(bundle.getString(SENDER));
                content.setText(bundle.getString(CONTENT));
                break;
            default:
                break;
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.mail_send_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(onMenuItemClick);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mail_send_toolbar_menu, menu);
        return true;
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int id = item.getItemId();
            switch(id) {
                case R.id.mail_send_action_attach:
                    Toast.makeText(main, "attach", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.mail_send_action_send:
                    SharedPreferences pref = getSharedPreferences(SHARED_PREFERENCE_EMAIL, MODE_PRIVATE);
                    String user = pref.getString(USERNAME, "");
                    String password = pref.getString(PASSWORD, "");
                    String host = pref.getString(OUTGOING_SERVER, "");
                    String subjectStr = subject.getText().toString();
                    String fromStr = from.getText().toString();
                    String toStr = to.getText().toString();
                    String contentStr = content.getText().toString();
//                    SendMail sendMail = new SendMail();
//                    sendMail.sendMail(main, user, password, host, toStr, fromStr, subjectStr, contentStr);
                    SMTPMail smtpMail = new SMTPMail();
                    smtpMail.sendMail(main, user, password, host, toStr, fromStr, subjectStr, contentStr);
                    Toast.makeText(main, "send", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            return true;
        }
    };
}

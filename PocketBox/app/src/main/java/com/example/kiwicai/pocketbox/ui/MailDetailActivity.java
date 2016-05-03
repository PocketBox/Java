package com.example.kiwicai.pocketbox.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

import com.example.kiwicai.pocketbox.R;
import com.example.kiwicai.pocketbox.mail.MailObject;
import com.example.kiwicai.pocketbox.proxy.MailDetailConstant;
import com.example.kiwicai.pocketbox.proxy.MailSendConstant;

/**
 * Created by tao-macbook on 5/1/16.
 */
public class MailDetailActivity extends AppCompatActivity implements MailDetailConstant, MailSendConstant {

    private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    private ImageButton reply, forward;
    private Activity main = this;
    private String subjectStr, senderStr, receiverStr, timeStr, contentStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        MailObject message = (MailObject) bundle.getSerializable(EMAIL);
        String mailBox = bundle.getString(MAILBOX);
        setContentView(R.layout.mail_detail_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.mail_detail_toolbar);
        toolbar.setTitle(mailBox);
        setSupportActionBar(toolbar);

        TextView subject = (TextView) findViewById(R.id.mail_detail_subject);
        TextView from = (TextView) findViewById(R.id.mail_detail_from);
        TextView to = (TextView) findViewById(R.id.mail_detail_to);
        TextView time = (TextView) findViewById(R.id.mail_detail_time);
        TextView content = (TextView) findViewById(R.id.mail_detail_content);

        this.subjectStr = message.getSubject();
        this.senderStr = message.getSender()[0].toString();
        this.receiverStr = message.getReceiver()[0].toString();
        this.timeStr = sdf.format(message.getDate());
        this.contentStr = message.getContent();

        subject.setText(this.subjectStr);
        from.setText(this.senderStr);
        to.setText(this.receiverStr);
        time.setText(this.timeStr);
        content.setText(this.contentStr);

        this.reply = (ImageButton) findViewById(R.id.mail_detail_reply);
        this.forward = (ImageButton) findViewById(R.id.mail_detail_forward);

        reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(main, "reply", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(main, MailSendActivity.class);
                Bundle bd = new Bundle();
                bd.putString(METHOD, REPLY);
                String newSubject = "Re: " + subjectStr;
                bd.putString(SUBJECT, newSubject);
                bd.putString(RECEIVER, receiverStr);
                bd.putString(SENDER, senderStr);
                String newContent = "\n\n\n";
                newContent += "---------Original Mail---------\n";
                newContent += "Subject: " + subjectStr + "\n";
                newContent += "Sender: " + senderStr + "\n";
                newContent += "Receiver: " + receiverStr + "\n";
                newContent += "Date: " + timeStr + "\n";
                newContent += contentStr;
                bd.putString(CONTENT, newContent);
                intent.putExtras(bd);
                startActivity(intent);
            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(main, "forward", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(main, MailSendActivity.class);
                Bundle bd = new Bundle();
                bd.putString(METHOD, FORWARD);
                String newSubject = "Fwd: " + subjectStr;
                bd.putString(SUBJECT, newSubject);
                bd.putString(SENDER, senderStr);
                String newContent = "\n\n\n";
                newContent += "---------Original Mail---------\n";
                newContent += "Subject: " + subjectStr + "\n";
                newContent += "Sender: " + senderStr + "\n";
                newContent += "Receiver: " + receiverStr + "\n";
                newContent += "Date: " + timeStr + "\n";
                newContent += contentStr;
                bd.putString(CONTENT, newContent);
                intent.putExtras(bd);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mail_detail_toolbar_menu, menu);
        return true;
    }
}

package com.example.kiwicai.pocketbox.mail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import com.example.kiwicai.pocketbox.R;

/**
 * Created by tao-macbook on 4/4/16.
 */
public class MailAdapter extends ArrayAdapter<MailObject> {

    private int resourceID;

    private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

    public MailAdapter(Context context, int textViewResourceId, List<MailObject> objects) {
        super(context, textViewResourceId, objects);
        resourceID = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MailObject mail = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceID, null);
        TextView subject = (TextView) view.findViewById(R.id.mail_list_item_subject);
        TextView sender = (TextView) view.findViewById(R.id.mail_list_item_sender);
        TextView time = (TextView) view.findViewById(R.id.mail_list_item_time);
        TextView content = (TextView) view.findViewById(R.id.mail_list_item_content);
        String subjectTemp = mail.getSubject();
        if(subjectTemp == null || subjectTemp.equals("")) {
            subject.setText("(No Subject)");
        }
        else {
            subject.setText(subjectTemp);
        }
        sender.setText(mail.getSender()[0].toString());
        time.setText(sdf.format(mail.getDate()));
        content.setText(mail.getContent().toString());
        return view;
    }
}

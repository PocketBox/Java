package com.example.kiwicai.pocketbox.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kiwicai.pocketbox.R;
import com.example.kiwicai.pocketbox.mail.IMAPMail;
import com.example.kiwicai.pocketbox.mail.MailAdapter;
import com.example.kiwicai.pocketbox.mail.MailObject;
import com.example.kiwicai.pocketbox.proxy.LoginConstant;
import com.example.kiwicai.pocketbox.proxy.MailDetailConstant;

import java.util.List;



/**
 * Created by tao-macbook on 4/8/16.
 */
public class MailDefaultBoxFragment extends Fragment implements LoginConstant, MailDetailConstant {

    TabLayout boxTabLayout;
    ViewPager boxViewPager;
    TextView mailLength;
    ListView mailListView;
    String mailBox;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mail_main_content_fragment_lists, container, false);;

        Bundle bd = getArguments();
        String username = bd.getString(USERNAME);
        String password = bd.getString(PASSWORD);
        String incoming_server = bd.getString(INCOMING_SERVER);
        String outgoing_server = bd.getString(OUTGOING_SERVER);
        String folder_choice = bd.getString(FOLDER_CHOICE);

        boxTabLayout = (TabLayout) view.findViewById(R.id.mail_box_tab);
//        boxViewPager = (ViewPager) view.findViewById(R.id.mail_box_view_pager);
//        boxTabLayout.addTab(boxTabLayout.newTab().setText("Folder 1"));

        mailLength = (TextView) view.findViewById(R.id.mail_fragment_box_analyst);
        mailListView = (ListView) view.findViewById(R.id.mail_fragment_box_email_list);

        IMAPMail imapMail = new IMAPMail();
//        imapMail.getMails(getActivity(), this, "ecexiaoyangtuo", "xiaoyangtuo123", "imap.gmail.com", "box");
        if(INBOX.equals(folder_choice)) {
            imapMail.getMails(getActivity(), this, username, password, incoming_server, "INBOX");
            mailBox = INBOX;
        }
        else if(OUTBOX.equals(folder_choice)){
            imapMail.getMails(getActivity(), this, username, password, incoming_server, "INBOX.Sent");
            mailBox = OUTBOX;
        }
        else if(DRAFT.equals(folder_choice)) {
            imapMail.getMails(getActivity(), this, username, password, incoming_server, "INBOX.Drafts");
            mailBox = DRAFT;
        }
        else if(TRASH.equals(folder_choice)) {
            imapMail.getMails(getActivity(), this, username, password, incoming_server, "INBOX.Trash");
            mailBox = TRASH;
        }
        return  view;
    }



    public void getMailReady(final Activity mainActivity, final List<MailObject> mails) {
        Integer length = -1;
        if(mails != null) {
            length = mails.size();
        }
        mailLength.setText(String.format("Totally %d mails", length));
        MailAdapter adapter = new MailAdapter(getActivity(), R.layout.mail_main_content_fragment_lists_item, mails);
        mailListView.setAdapter(adapter);
        mailListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MailObject mail = mails.get(position);
//                Toast.makeText(mainActivity, mail.getSubject(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mainActivity, MailDetailActivity.class);
                Bundle bd = new Bundle();
                bd.putSerializable(EMAIL, mail);
                bd.putString(MAILBOX, mailBox);
                intent.putExtras(bd);
                startActivity(intent);

//                Toolbar toolbar = (Toolbar) mainActivity.findViewById(R.id.mail_main_toolbar);
//                toolbar.getMenu().clear();
//                toolbar.inflateMenu(R.menu.mail_detail_toolbar_menu);
            }
        });
    }
}

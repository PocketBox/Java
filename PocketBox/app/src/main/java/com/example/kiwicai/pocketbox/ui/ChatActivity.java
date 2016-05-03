package com.example.kiwicai.pocketbox.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.kiwicai.pocketbox.R;
import com.example.kiwicai.pocketbox.exception.PocketBoxException;

/**
 * Created by hongbao on 2016/4/4.
 */
public class ChatActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_chatroom);
        Intent intent = getIntent();
    }

    public void jumpToMain(View view) {
        Intent intent = new Intent(this, ChatActivity.class);
        try {
            if (intent == null) {
                throw new PocketBoxException(3);
            }
        } catch (PocketBoxException e) {
            e.fix(e.getErrno());
            e.createExceptionLog(ChatActivity.this);
//            return;
        }
        startActivity(intent);
    }
}

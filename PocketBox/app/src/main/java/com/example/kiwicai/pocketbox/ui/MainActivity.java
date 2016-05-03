package com.example.kiwicai.pocketbox.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.kiwicai.pocketbox.R;
import com.example.kiwicai.pocketbox.exception.PocketBoxException;

/**
 * Created by kiwicai on 4/3/16.
 */
public class MainActivity extends AppCompatActivity {
    String userName;
    RadioGroup rg;
    RadioButton b1;
    RadioButton b2;
    RadioButton b3;
    RadioButton b4;
    int fragmentNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        try {
            if (intent == null) {
                throw new PocketBoxException(3);
            }
        } catch (PocketBoxException e) {
            e.fix(e.getErrno());
            e.createExceptionLog(MainActivity.this);
        }

        setRadioButton();

        if (intent.getStringExtra("from") != null && intent.getStringExtra("from").equals("edit")) {
            b4.setChecked(true);
        } else if (intent.getStringExtra("from") != null && intent.getStringExtra("from").equals("chat")) {
            b1.setChecked(true);
        } else {
            b1.setChecked(true);
        }
    }

    @Override
    protected void onResume() {
            super.onResume();
    }


    private void setRadioButton() {
        b1 = (RadioButton) findViewById(R.id.rb_activity);
        b2 = (RadioButton) findViewById(R.id.rb_email);
        b3 = (RadioButton) findViewById(R.id.rb_chat);
        b4 = (RadioButton) findViewById(R.id.rb_setting);

        rg = (RadioGroup) findViewById(R.id.rg_group);

        Drawable d1 = ResourcesCompat.getDrawable(getResources(), R.drawable.tab_menu_activity, null);
        d1.setBounds(0, 0, 140, 140);
        b1.setCompoundDrawables(null, d1, null, null);

        Drawable d2 = ResourcesCompat.getDrawable(getResources(), R.drawable.tab_menu_email, null);
        d2.setBounds(0, 0, 140, 140);
        b2.setCompoundDrawables(null, d2, null, null);

        Drawable d3 = ResourcesCompat.getDrawable(getResources(), R.drawable.tab_menu_chat, null);
        d3.setBounds(0, 0, 140, 140);
        b3.setCompoundDrawables(null, d3, null, null);

        Drawable d4 = ResourcesCompat.getDrawable(getResources(), R.drawable.tab_menu_setting, null);
        d4.setBounds(0, 0, 140, 140);
        b4.setCompoundDrawables(null, d4, null, null);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch(checkedId) {
                    case R.id.rb_activity:
                        fragmentNum = 1;
                        ActivitiesFragment actFragment = new ActivitiesFragment();
                        transaction.replace(R.id.different_activities, actFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        break;
                    case R.id.rb_email:
                        fragmentNum = 2;
                        Intent intent = new Intent(MainActivity.this, MailMainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.rb_chat:
                        fragmentNum = 3;
                        Intent intent2 = new Intent(MainActivity.this, SimpleChatActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.rb_setting:
                        fragmentNum = 4;
                        PersonalFragment personalFragment = new PersonalFragment();
                        transaction.replace(R.id.different_activities, personalFragment);
                        transaction.commit();
                        break;
                    default:
                        break;
                }
            }

        });

        //b4.setChecked(true);
    }


}

package com.example.kiwicai.pocketbox.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kiwicai.pocketbox.R;
import com.example.kiwicai.pocketbox.proxy.LoginConstant;

/**
 * Created by tao-macbook on 4/5/16.
 */
public class MailSetUpActivity extends AppCompatActivity implements LoginConstant {
    private EditText editUsername, editPassword, editIncoming, editOutgoing;
    private CheckBox checkSavePassword, checkAutoLogin;

    private Activity main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mail_login_activity);

        main = this;

        editUsername = (EditText) findViewById(R.id.login_edit_user);
        editPassword = (EditText) findViewById(R.id.login_edit_password);
        editIncoming = (EditText) findViewById(R.id.login_edit_incoming_server);
        editOutgoing = (EditText) findViewById(R.id.login_edit_outgoing_server);
        checkSavePassword = (CheckBox) findViewById(R.id.login_check_saving_password);
        checkAutoLogin = (CheckBox) findViewById(R.id.login_check_login_automatically);

        editUsername.setText("jiyangt");
        editPassword.setText("JerryTZ.11.14");
        editIncoming.setText("cyrus.andrew.cmu.edu");
        editOutgoing.setText("smtp.andrew.cmu.edu");

        checkAutoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkAutoLogin.isChecked()) {
                    checkSavePassword.setChecked(true);
                }
            }
        });

        checkSavePassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!checkSavePassword.isChecked()) {
                    checkAutoLogin.setChecked(false);
                }
            }
        });

        if(savedInstanceState != null) {
            editUsername.setText(savedInstanceState.getString(USERNAME));
            editPassword.setText(savedInstanceState.getString(PASSWORD));
            editIncoming.setText(savedInstanceState.getString(INCOMING_SERVER));
            editOutgoing.setText(savedInstanceState.getString(OUTGOING_SERVER));
            checkAutoLogin.setChecked(savedInstanceState.getBoolean(AUTO_LOGIN, false));
            checkSavePassword.setChecked(savedInstanceState.getBoolean(SAVE_PASSWORD, false));
            String exception = savedInstanceState.getString(EXCEPTION);
            if(exception == null || !exception.equals("")) {
                Toast.makeText(this, exception, Toast.LENGTH_SHORT).show();
            }
        }

        Button submitButton = (Button) findViewById(R.id.login_button_login);
        submitButton.setOnClickListener(this.submit);

    }

    View.OnClickListener submit = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            String username = editUsername.getText().toString();
            String password = editPassword.getText().toString();
            String incoming = editIncoming.getText().toString();
            String outgoing = editOutgoing.getText().toString();
            boolean save_password = checkSavePassword.isChecked();
            boolean auto_login = checkAutoLogin.isChecked();
            SharedPreferences.Editor editor = getSharedPreferences(SHARED_PREFERENCE_EMAIL, MODE_PRIVATE).edit();
            editor.putString(USERNAME, username);
            editor.putString(INCOMING_SERVER, incoming);
            editor.putString(OUTGOING_SERVER, outgoing);
            editor.putBoolean(SAVE_PASSWORD, save_password);
            editor.putBoolean(AUTO_LOGIN, auto_login);
            editor.putBoolean(IS_LOGIN, true);
            if(save_password) {
                editor.putString(PASSWORD, password);
            }
            else {
                editor.putString(PASSWORD, "");
            }
            editor.apply();

            Intent intent = new Intent(main, MailMainActivity.class);
            Bundle bd = new Bundle();
            bd.putString(USERNAME, username);
            bd.putString(PASSWORD, password);
            bd.putString(INCOMING_SERVER, incoming);
            bd.putString(OUTGOING_SERVER, outgoing);
            intent.putExtras(bd);
            startActivity(intent);

        }
    };
}

package com.example.kiwicai.pocketbox.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.kiwicai.pocketbox.R;
import com.example.kiwicai.pocketbox.exception.PocketBoxException;
import com.example.kiwicai.pocketbox.util.PocketConstants;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by kiwicai on 4/3/16.
 */
public class RegisterActivity extends AppCompatActivity implements PocketConstants {

    private EditText userNameEditor;
    private EditText pwdEditor;
    private EditText pwdConfirmEditor;
    private EditText emailEditor;
    private EditText addrEditor;
    private Spinner identitySpinner;
    private String identity;
    private String userName;
    private String pwd;
    private String pwdConfirm;
    private String email;
    private String addr;

    private static final String REGISTER_SUCCESS = "Register succeed!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Intent intent = getIntent(); //need?
        userNameEditor = (EditText)findViewById(R.id.userEditText);
        pwdEditor = (EditText)findViewById(R.id.pwdEditText);
        pwdConfirmEditor = (EditText)findViewById(R.id.pwdConfirmEditText);
        emailEditor = (EditText)findViewById(R.id.emailEditText);
        addrEditor = (EditText)findViewById(R.id.addrEitText);
        identitySpinner  = (Spinner) findViewById(R.id.identitySpinner);
        identitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                identity = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void jumpToMain(View view) {
        store();

    }

    public void store(){

        userName = userNameEditor.getText().toString();
        if (userName.equals("") || userName.matches("\\s+")) {
            Toast.makeText(RegisterActivity.this, "User Name can not be empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        pwd = pwdEditor.getText().toString();
        if (pwd.equals("")) {
            Toast.makeText(RegisterActivity.this, "Password can not be empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        pwdConfirm = pwdConfirmEditor.getText().toString();
        if (!pwd.equals(pwdConfirm)) {
            Toast.makeText(RegisterActivity.this, "Password doesn't match!", Toast.LENGTH_SHORT).show();
            return;
        }
        email = emailEditor.getText().toString();
        if (email.equals("") || email.matches("\\s+")) {
            Toast.makeText(RegisterActivity.this, "Email can not be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        addr = addrEditor.getText().toString();
        if (addr.equals("") || addr.matches("\\s+")) {
            Toast.makeText(RegisterActivity.this, "Address can not be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        new Register().execute(email, pwd, userName, addr, identity);

    }


    private class Register extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            String result = null;
            try {
                URL requestURL = new URL(SERVER_URL + "register");

                connection = (HttpURLConnection) requestURL.openConnection();
                connection.setDoOutput(true); //for post

                //post:
                DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                //out.writeBytes("name=caicai");
                System.out.println("99999999999999999999999email: " + strings[0] + " " + strings[1] + " " + strings[2] + " " + strings[3] + " " + strings[4]);
                out.writeBytes("email=" + strings[0] + "&pwd=" + strings[1] + "&userName=" + strings[2] + "&addr=" + strings[3] + "&identity=" + strings[4]);
                InputStream in = null;
//                while (true) {
//                    try {
                        in = connection.getInputStream();
//                        break;
//                    } catch (FileNotFoundException e) {
//                        System.out.println("bad request!!!!!");
//                    }
//                }
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                System.out.println("444444444444444444444");
                String oneLine = "";
                if ((oneLine = br.readLine()) != null) {
                    result = oneLine;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                connection.disconnect();
            }

            System.out.println("WTFWTFWTFWTFWTF" + result);
            //return result;
            return result;
        }

        protected void onPostExecute(String result) {
            Toast.makeText(RegisterActivity.this, result, Toast.LENGTH_LONG).show();
            if (result.equals(REGISTER_SUCCESS)) {

                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);

                try {
                    if (intent == null) {
                        throw new PocketBoxException(3);
                    }
                } catch (PocketBoxException e) {
                    e.fix(e.getErrno());
                    e.createExceptionLog(RegisterActivity.this);

                }
                storeUnique();
                System.out.println("jumpjumpjump!!!!!!");
                startActivity(intent);
                System.out.println("jumpjumpjump2222222222222!!!!!!");
            }
        }

        private void storeUnique() {
            SharedPreferences.Editor sharedEditor = getSharedPreferences("login", MODE_PRIVATE).edit();
            sharedEditor.putString("userName", userName);
            sharedEditor.putString("email", email);
            sharedEditor.commit();
        }

    }

}

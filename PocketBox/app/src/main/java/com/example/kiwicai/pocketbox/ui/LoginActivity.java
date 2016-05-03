package com.example.kiwicai.pocketbox.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

public class LoginActivity extends AppCompatActivity implements PocketConstants {
    private static final String LOGIN_SUCCESS = "From server: Login succeed!";
    private static final String LOGIN_FAIL = "From server: Wrong email or password!";
    private String email;
    private String userName;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mediaPlayer = MediaPlayer.create(this, R.raw.enter);
        mediaPlayer.start();


    }

    public void loginPage(View view) {
        EditText emailEdit = (EditText)findViewById(R.id.emailEditText);
        EditText pwdEdit = (EditText)findViewById(R.id.pwdEditText);
        email = emailEdit.getText().toString();
        String pwd = pwdEdit.getText().toString();


       new LoginRequest().execute(email, pwd);

    }

    public void registerPage(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);

        try {
            if (intent == null) {
                throw new PocketBoxException(2);
            }
        } catch (PocketBoxException e) {
            e.fix(e.getErrno());
            e.createExceptionLog(LoginActivity.this);
//            return;
        }
        startActivity(intent);
    }

    private class LoginRequest extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String response) {
            String result = null;
            if (response.equals(LOGIN_FAIL)) {
                result = response;
            } else {
                result = LOGIN_SUCCESS;
                userName = response;
                storeUnique();
            }
            Toast.makeText(LoginActivity.this, result, Toast.LENGTH_LONG).show();

            if (result.equals(LOGIN_SUCCESS)) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                try {
                    if (intent == null) {
                        throw new PocketBoxException(3);
                    }
                } catch (PocketBoxException e) {
                    e.fix(e.getErrno());
                    e.createExceptionLog(LoginActivity.this);

                }
                SharedPreferences.Editor sharedEditor = getSharedPreferences("login", MODE_PRIVATE).edit();
                sharedEditor.putString("email", email);
                sharedEditor.commit();

                startActivity(intent);
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            System.out.println("email : " + strings[0]);
            System.out.println("pwd : " + strings[1]);

            HttpURLConnection connection = null;
            String result = null;
            try {
                URL requestURL = new URL(SERVER_URL + "login");

                connection = (HttpURLConnection) requestURL.openConnection();
                connection.setDoOutput(true); //for post

                //post:
                DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                System.out.println("99999999999999999999999email: " + strings[0] + " " + strings[1]);
                out.writeBytes("email=" + strings[0] + "&pwd=" + strings[1]);

                InputStream in = null;

                in = connection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(in));
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

            return result;
        }
    }
    private void storeUnique() {
        SharedPreferences.Editor sharedEditor = getSharedPreferences("login", MODE_PRIVATE).edit();
        sharedEditor.putString("userName", userName);
        sharedEditor.putString("email", email);
        sharedEditor.commit();
    }
}

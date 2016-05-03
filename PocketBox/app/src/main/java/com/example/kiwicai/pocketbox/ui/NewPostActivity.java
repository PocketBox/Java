package com.example.kiwicai.pocketbox.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kiwicai on 4/25/16.
 */
public class NewPostActivity extends AppCompatActivity implements PocketConstants {
    private EditText titleEditor;
    private EditText textEditor;
    private String title;
    private String text;
    private String time;
    private String postId;
    private String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        titleEditor = (EditText) findViewById(R.id.title);
        textEditor = (EditText) findViewById(R.id.text);
        userName = getOwnUserName();
    }

    private String getOwnUserName() {
        SharedPreferences pref = getSharedPreferences("login", MODE_PRIVATE);
        String name = pref.getString("userName", "");
        System.out.println("Get this login user's user name: " + name);
        return name;
    }

    public void postNew (View view) {
        title = titleEditor.getText().toString();
        text = textEditor.getText().toString();
        if (title.equals("") || title.matches("\\s+")) {
            Toast.makeText(NewPostActivity.this, "Title can not be empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (text.equals("") || text.matches("\\s+")) {
            Toast.makeText(NewPostActivity.this, "Text can not be empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy-HH:mm:ss");
        time = df.format(new Date());
        System.out.println("1111111111111111111111111111postNew: " + title + " " + text + " " + time);
        new postRequest().execute(title, text, time, userName);
    }

    private class postRequest extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String postId) {
            Toast.makeText(NewPostActivity.this, postId, Toast.LENGTH_LONG).show();

            Intent intent = new Intent(NewPostActivity.this, ViewPostActivity.class);

            try {
                if (intent == null) {
                    throw new PocketBoxException(3);
                }
            } catch (PocketBoxException e) {
                e.fix(e.getErrno());
                e.createExceptionLog(NewPostActivity.this);

            }

            Bundle bundle = new Bundle();
            bundle.putString("postId", postId);
            bundle.putString("title", title);
            bundle.putString("time", time);
            bundle.putString("text", text);
            bundle.putString("userName", userName);
            System.out.println("2888888888888888postId: " + postId + " title: " + title + " time: " + time + " text: " + text);
            intent.putExtras(bundle);
            startActivity(intent);
            System.out.println("transmit bundle succeed!");
            finish();

        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;

            try {
                URL requestURL = new URL(SERVER_URL + "newPost");

                connection = (HttpURLConnection) requestURL.openConnection();
                connection.setDoOutput(true); //for post

                //post:
                DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                System.out.println("99999999999999999999999title: " + strings[0] + " " + strings[1] + " " + strings[2] + " " + strings[3]);
                out.writeBytes("title=" + strings[0] + "&text=" + strings[1] + "&time=" + strings[2] + "&userName=" + strings[3]);

                InputStream in = null;

                in = connection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                System.out.println("444444444444444444444");
                String oneLine = "";
                if ((oneLine = br.readLine()) != null) {

                    postId = oneLine;
                    System.out.println("88888888888888888postid: " + postId);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                connection.disconnect();
            }
            return postId;
        }

    }

}

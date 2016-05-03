package com.example.kiwicai.pocketbox.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kiwicai.pocketbox.R;
import com.example.kiwicai.pocketbox.util.PocketConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kiwicai on 4/25/16.
 */
public class ViewPostActivity extends AppCompatActivity implements PocketConstants {
    private Intent intent;
    private TextView viewTitle;
    private TextView viewTime;
    private TextView viewText;
    private TextView viewUserName;
    private EditText replyEditor;
    private String reply;
    private String title;
    private String time;
    private String text;
    private String postId;
    private String userName;
    private String onePost;

    private List<String> replies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("View this new post!!!!!!!!");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);
        viewTitle = (TextView) findViewById(R.id.titleView);
        viewUserName = (TextView) findViewById(R.id.userNameView);
        viewTime = (TextView) findViewById(R.id.timeView);
        viewText = (TextView) findViewById(R.id.textView);

        if ((intent = getIntent()) != null) {
            Bundle bd = intent.getExtras();
            title = bd.getString("title"); //
            userName = bd.getString("userName"); //
            postId = bd.getString("postId"); //
            time = bd.getString("time");
            System.out.println("this post's time: " + time);
            text = bd.getString("text");
            System.out.println("this post's text: " + text);
            viewTitle.setText("Title: " + title);
            viewUserName.setText("Author: " + userName);
            if (time == null && text == null) {
                System.out.println("time and text are null!");
                new RequestTextAndTime().execute(postId);

            } else {
                System.out.println("time and text are not null!");
                viewTime.setText("Posted Time: " + time);
                viewText.setText("Text: " + "\n" + text);
            }

            //System.out.println("266666666666666666postId: " + postId + " title: " + title + " userName: " + userName + " time: " + time + " text: " + text);

        }
    }
    private class RequestTextAndTime extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(final String textAndTime) {
            parseJson(textAndTime);
            viewTime.setText("Posted Time: " + time);
            viewText.setText("Text: " + "\n" + text);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    ViewPostActivity.this, android.R.layout.simple_list_item_1, replies);
            ListView listView = (ListView) findViewById(R.id.replyList);
            listView.setAdapter(adapter);
        }
        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            try {
                URL requestURL = new URL(SERVER_URL + "onePost");

                connection = (HttpURLConnection) requestURL.openConnection();
                connection.setDoOutput(true); //for post
                DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                out.writeBytes("postId=" + strings[0]);
                InputStream in = null;

                in = connection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                System.out.println("444444444444444444444");
                String oneLine = "";
                if ((oneLine = br.readLine()) != null) {
                    System.out.println("2222222222222222222222oneLine: " + oneLine);
                    onePost = oneLine;
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                connection.disconnect();
            }
            return onePost;
        }
    }

    private void parseJson(String post) {
        replies.clear();
        try {
            JSONArray jsonArray = new JSONArray(post);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (i == 0) {
                    this.text = jsonObject.getString("postText");
                    this.time = jsonObject.getString("postDate");
                } else {
                    replies.add(jsonObject.getString("reply") + "\n" + "Replied by: " + jsonObject.getString("userName"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseJsonReply(String post) {
        replies.clear();
        try {
            JSONArray jsonArray = new JSONArray(post);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                replies.add(jsonObject.getString("reply") + "\n" + "Replied by: " + jsonObject.getString("userName"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reply(View view) {
        replyEditor = (EditText)findViewById(R.id.reply);
        reply = replyEditor.getText().toString();
        if (reply.equals("") || reply.matches("\\s+")) {
            Toast.makeText(ViewPostActivity.this, "Reply can not be empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        new replyStore().execute(postId, reply);

    }

    private class replyStore extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(final String allReply) {
            System.out.println("allreply111111111111111111: " + allReply);
            parseJsonReply(allReply);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    ViewPostActivity.this, android.R.layout.simple_list_item_1, replies);
            ListView listView = (ListView) findViewById(R.id.replyList);
            listView.setAdapter(adapter);
            replyEditor.setText("");
        }
        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            String allReply = "";
            try {
                URL requestURL = new URL(SERVER_URL + "storeReply");

                connection = (HttpURLConnection) requestURL.openConnection();
                connection.setDoOutput(true); //for post
                DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                String ownUserName = getOwnUserName();
                out.writeBytes("postId=" + strings[0] + "&reply=" + strings[1] + "&userName=" + ownUserName);
                InputStream in = null;

                in = connection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                System.out.println("444444444444444444444");
                String oneLine = "";
                if ((oneLine = br.readLine()) != null) {
                    System.out.println("2222222222222222222222oneLine: " + oneLine);
                    allReply = oneLine;
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                connection.disconnect();
            }
            return allReply;
        }
    }
    private String getOwnUserName() {
        SharedPreferences pref = getSharedPreferences("login", MODE_PRIVATE);
        String name = pref.getString("userName", "");
        System.out.println("Get this login user's user name: " + name);
        return name;
    }
}

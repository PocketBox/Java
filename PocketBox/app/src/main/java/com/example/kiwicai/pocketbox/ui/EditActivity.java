package com.example.kiwicai.pocketbox.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kiwicai.pocketbox.R;
import com.example.kiwicai.pocketbox.util.PocketConstants;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kiwicai on 4/30/16.
 */
public class EditActivity extends AppCompatActivity implements PocketConstants {
    private String email;
    private TextView emailView;
    private EditText userNameEditor;
    private EditText addressEditor;
    private Spinner identitySpinner;
    private Button submitButton;
    private String newUserName;
    private String newAddress;
    private String newIdentity;

    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        Bundle bd = intent.getExtras();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        getViews();

        email = bd.getString("email");
        emailView.setText("email: " + email);
        userNameEditor.setText(bd.getString("userName"));
        addressEditor.setText(bd.getString("address"));
        identitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                newIdentity = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newUserName = userNameEditor.getText().toString();
                newAddress = addressEditor.getText().toString();
                if (newUserName.equals("") || newUserName.matches("\\s+")) {
                    Toast.makeText(EditActivity.this,"User name can not be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (newAddress.equals("") || newAddress.matches("\\s+")) {
                    Toast.makeText(EditActivity.this,"Address can not be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                new Edit().execute(email);
                finish();

            }
        });
    }

    private void getViews() {
        emailView = (TextView) findViewById(R.id.showEmail);
        userNameEditor = (EditText) findViewById(R.id.userNameEditor);
        addressEditor = (EditText) findViewById(R.id.addressEditor);
        identitySpinner = (Spinner) findViewById(R.id.identityEditSpinner);
        submitButton = (Button) findViewById(R.id.submit);
    }

    private class Edit extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(final String posts) {
            Toast.makeText(EditActivity.this, "Edit succeed!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(EditActivity.this, MainActivity.class);
            intent.putExtra("from", "edit");
            startActivity(intent);
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            String result = null;
            try {
                URL requestURL = new URL(SERVER_URL + "edit");
                connection = (HttpURLConnection) requestURL.openConnection();
                connection.setDoOutput(true); //for post
                DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                out.writeBytes("email=" + strings[0] + "&userName=" + newUserName + "&address="  + newAddress + "&identity=" + newIdentity);
                InputStream in = null;
                in = connection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                System.out.println("444444444444444444444");
                String oneLine = "";
                if ((oneLine = br.readLine()) != null) {
                    System.out.println("2222222222222222222222oneLine: " + oneLine);
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

}

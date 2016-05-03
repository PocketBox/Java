package com.example.kiwicai.pocketbox.ui;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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

/**
 * Created by kiwicai on 4/3/16.
 */
public class PersonalFragment extends Fragment implements PocketConstants {
    private TextView personalInfo;
    private Button editButton;
    private String email;
    private String userName;
    private String address;
    private String identity;
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_personal, container, false);
        personalInfo = (TextView) view.findViewById(R.id.personalInfo);
        editButton = (Button) view.findViewById(R.id.editInfoButton);
        getEmail();
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditActivity.class);
                Bundle bd = new Bundle();
                bd.putString("email", email);
                bd.putString("userName", userName);
                bd.putString("address", address);
                bd.putString("identity", identity);
                intent.putExtras(bd);
                startActivity(intent);
            }
        });
        new getInfo().execute(email);
        return view;
    }

    private void getEmail() {
        SharedPreferences pref = getActivity().getSharedPreferences("login", getActivity().MODE_PRIVATE);
        email = pref.getString("email", "");
    }

    private void parseJson(String info) {
        try {
            JSONArray jsonArray = new JSONArray(info);

            JSONObject jsonObject = jsonArray.getJSONObject(0);
            userName = jsonObject.getString("userName");
            address = jsonObject.getString("address");
            identity = jsonObject.getString("identity");
            String output = "\nUser Name: " + userName + "\n" + "Email: " + email + "\n" + "Address: " + address + "\n" + "Identity: " + identity + "\n";
            personalInfo.setText(output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class getInfo extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(final String info) {
            parseJson(info);
        }
        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            String info = null;
            try {
                URL requestURL = new URL(SERVER_URL + "getInfo");

                connection = (HttpURLConnection) requestURL.openConnection();
                connection.setDoOutput(true); //for post
                DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                out.writeBytes("email=" + strings[0]);
                InputStream in = null;

                in = connection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                System.out.println("444444444444444444444");
                String oneLine = "";
                if ((oneLine = br.readLine()) != null) {
                    System.out.println("2222222222222222222222info: " + oneLine);
                    info = oneLine;
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                connection.disconnect();
            }
            return info;
        }
    }

}

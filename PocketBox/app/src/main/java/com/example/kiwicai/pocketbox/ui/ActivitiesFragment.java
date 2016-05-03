package com.example.kiwicai.pocketbox.ui;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.kiwicai.pocketbox.R;
import com.example.kiwicai.pocketbox.adapter.PostAdapter;
import com.example.kiwicai.pocketbox.model.PostQuickView;
import com.example.kiwicai.pocketbox.util.PocketConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kiwicai on 5/2/16.
 */
public class ActivitiesFragment extends Fragment implements PocketConstants {
    private List<PostQuickView> quickViews = new ArrayList<>();
    String posts;
    ListView listView;
    Button newButton;
    ImageButton refresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activities, container, false);
        newButton = (Button) view.findViewById(R.id.newPost2);
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewPostActivity.class);
                startActivity(intent);
            }
        });
        refresh = (ImageButton) view.findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new showAll().execute("");
            }
        });

        new showAll().execute("");

        return view;
    }

    private class showAll extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(final String posts) {
            Toast.makeText(getActivity(), "Get all posts succeed!", Toast.LENGTH_LONG).show();

            if (posts != null && !posts.equals("")) {
                System.out.println("start to parse json");
                quickViews.clear(); //always clear before update!!!!
                parseJson(posts);
                System.out.println("end to parse json");


                PostAdapter adapter = new PostAdapter(
                        getActivity(), R.layout.post_item, quickViews);
                listView = (ListView) getActivity().findViewById(R.id.postList2);
                if (adapter == null) {
                    System.out.println("22222222222");
                }
                if (listView == null) {
                    System.out.println("kao2222222list");
                }
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        PostQuickView oneQuickView = quickViews.get(position);
                        String postId = oneQuickView.getId();
                        String title = oneQuickView.getTitle();
                        String userName = oneQuickView.getUserName();
                        Intent intent = new Intent(getActivity(), ViewPostActivity.class);
                        Bundle bd = new Bundle();
                        bd.putString("postId", postId);
                        bd.putString("title", title);
                        bd.putString("userName", userName);
                        intent.putExtras(bd);
                        startActivity(intent);
                    }
                });


            }

        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            try {
                URL requestURL = new URL(SERVER_URL + "activities");

                connection = (HttpURLConnection) requestURL.openConnection();
                connection.setDoOutput(true); //for post
                System.out.println("1111111111111111111111111111111all");
                InputStream in = null;

                in = connection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                System.out.println("444444444444444444444");
                String oneLine = "";
                if ((oneLine = br.readLine()) != null) {
                    System.out.println("2222222222222222222222oneLine: " + oneLine);
                    posts = oneLine;
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                connection.disconnect();
            }
            System.out.println("111111111111111111111111111111allpo: " + posts);
            return posts;
        }

    }

    private void parseJson(String posts) {
        try {
            JSONArray jsonArray = new JSONArray(posts);
            System.out.println("jsonArray's length: " + jsonArray.length());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("postId");
                System.out.println("1111111111111111show all: " + id);
                String title = jsonObject.getString("title");
                String userName = jsonObject.getString("userName");
                //String text = jsonObject.getString("postText");
                PostQuickView quickView = new PostQuickView(title, String.valueOf(id), userName);
                quickViews.add(quickView);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

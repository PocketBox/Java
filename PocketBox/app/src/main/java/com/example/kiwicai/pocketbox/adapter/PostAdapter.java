package com.example.kiwicai.pocketbox.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.kiwicai.pocketbox.R;
import com.example.kiwicai.pocketbox.model.PostQuickView;

import java.util.List;

/**
 * Created by kiwicai on 4/29/16.
 */
public class PostAdapter extends ArrayAdapter<PostQuickView> {
    private int resourceId;
    public PostAdapter(Context context, int listResourceId, List<PostQuickView> posts) {
        super(context, listResourceId, posts);
        resourceId = listResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PostQuickView post = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView title = (TextView) view.findViewById(R.id.postTitle);
        TextView userName = (TextView) view.findViewById(R.id.userName);
        title.setText(post.getTitle());
        System.out.println("23333333333get title: " + post.getTitle());
        userName.setText(post.getUserName());
        System.out.println("23333333333get UserName: " + post.getUserName());
        System.out.println("23333333333get Id: " + post.getId());
        return view;
    }

}

package com.example.kiwicai.pocketbox.model;

/**
 * Created by kiwicai on 4/29/16.
 */
public class PostQuickView {
    private String title;
    private String id;
    private String userName;

    public PostQuickView(String title, String id, String userName) {
        this.title = title;
        this.id = id;
        this.userName = userName;
    }

    public String getTitle() {
        return title;
    }
    public String getId() {
        return id;
    }
    public String getUserName() {
        return userName;
    }



}

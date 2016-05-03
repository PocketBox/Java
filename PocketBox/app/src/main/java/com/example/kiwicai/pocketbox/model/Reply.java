package com.example.kiwicai.pocketbox.model;

/**
 * Created by kiwicai on 4/29/16.
 */
public class Reply {
    private String userName;
    private String reply;
    private String time;
    public Reply(String userName, String time, String reply) {
        this.userName = userName;
        this.reply = reply;
        this.time = time;
    }
    public String getUserName() {
        return userName;
    }
    public String getReply() {
        return reply;
    }
    public String getTime() {
        return time;
    }

}

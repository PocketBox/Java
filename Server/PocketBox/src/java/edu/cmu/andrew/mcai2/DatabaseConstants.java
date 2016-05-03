/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.cmu.andrew.mcai2;

/**
 *
 * @author kiwicai
 */
public interface DatabaseConstants {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_NAME = "pocketbox_db";
    static final String URL = "jdbc:mysql://localhost:3306/";
    static final String DB_USER = "root";
    static final String DB_PWD = "";
//    private static final String URL = "jdbc:mysql://z12itfj4c1vgopf8.cbetxkdyhwsb.us-east-1.rds.amazonaws.com:3306/";
//    private static final String DB_USER = "o0n21lignhusnvrq";
//    private static final String DB_PWD = "un7qt0d4z97t4pt8";
    static final String USER_INFO_TABLE = "userinfo";
    static final String REGISTER_SUCCESS = "Register succeed!";
    static final String REGISTER_FAIL = "Register fail! Duplicate email!";
    static final String LOGIN_SUCCESS = "From server: Login succeed!";
    static final String LOGIN_FAIL = "From server: Wrong email or password!";
    
    static final String ACT_POST_TABLE = "actPosts";
    static final String REPLY_TABLE = "reply";
    
}

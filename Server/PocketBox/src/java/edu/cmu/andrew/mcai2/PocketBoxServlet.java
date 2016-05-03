/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.cmu.andrew.mcai2;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import com.heroku.sdk.jdbc.DatabaseUrl;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
//import java.util.logging.Level;
//import java.util.logging.Logger;

/**
 *
 * @author kiwicai
 */
@WebServlet(name = "PocketBoxServlet", urlPatterns = {"/PocketBoxServlet/*"})
public class PocketBoxServlet extends HttpServlet implements DatabaseConstants {
    private static Connection connection;
    //private static final String JDBC_DRIVER = ""org.postgresql.Driver"";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet PocketBoxServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>POST Servlet PocketBoxServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        processRequest(request, response);
        //response.setContentType("text/html;charset=UTF-8");
        System.out.println("233333333333333333333333333333333");       
        String name = request.getParameter("name");
        System.out.println("name: " + name);
        //PrintWriter out = response.getWriter();
        try (PrintWriter out = response.getWriter()) {
            out.write("response success name:" + name);
            out.flush();
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String[] route = (request.getPathInfo()).split("/");
        System.out.println("route: " + Arrays.toString(route) + route.length);
        switch (route[1]) {
            case "register" :
                registerDB(request, response);
                break;
            case "login" : 
                loginDB(request, response);
                break;
            case "activities" :
                viewActPosts(request, response);
                break;
            case "newPost" :
                newPost(request, response);
                break;
            case "onePost" :
                onePost(request, response);
                break;
            case "storeReply" :
                storeReply(request, response);
                break;
            case "getInfo" :
                getInfo(request, response);
                break;
            case "edit" :
                edit(request, response);
                break;
        }
        //registerDB(request, response);
        
        System.out.println("fuckfuckfuck");
    }
//
//    /**
//     * Returns a short description of the servlet.
//     *
//     * @return a String containing servlet description
//     */
//    @Override
//    public String getServletInfo() {
//        return "Short description";
//    }// </editor-fold>
    private static int initializeConnection() {
        try {
            Class.forName(JDBC_DRIVER);
            //connection = DatabaseUrl.extract().getConnection();
            
            connection = DriverManager.getConnection(URL, DB_USER, DB_PWD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }
    
    private static void createAndUseDatabase (Statement stmt) throws SQLException {
           stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME + ";");          
           stmt.executeUpdate("USE " + DB_NAME + ";");
           stmt.executeUpdate
               ("CREATE TABLE IF NOT EXISTS " + USER_INFO_TABLE + " (\n" +
               "  `email` varchar(30) NOT NULL,\n" +
               "  `password` text,\n" +
               "  `userName` text,\n" +
               "  `address` text,\n" +
               "  `identity` varchar(20),\n" +
               "  PRIMARY KEY (`email`)\n" +
               ");");        
    }
    
    private static void createActPostTable (Statement stmt) throws SQLException {
           //stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME + ";");          
           stmt.executeUpdate("USE " + DB_NAME + ";");
           stmt.executeUpdate
               ("CREATE TABLE IF NOT EXISTS " + ACT_POST_TABLE + " (\n" +
               "  `postId` INT NOT NULL AUTO_INCREMENT,\n" +       
               "  `postDate` varchar(30) NOT NULL,\n" +
               "  `title` text,\n" +
               "  `postText` text,\n" +  
               "  `userName` text,\n" +        
               "  PRIMARY KEY (`postId`)\n" +
               ");");                       
//               ("CREATE TABLE IF NOT EXISTS " + ACT_POST_TABLE + " (\n" +
//               "  `postId` INT NOT NULL,\n" +       
//               "  `postDate` varchar(30) NOT NULL,\n" +
//               "  `email` text,\n" +
//               "  `userName` text,\n" +
//               "  `title` text,\n" +
//               "  `postText` text,\n" +        
//               "  `hashTag` text,\n" +
//               "  `favorCount` INT,\n" +
//               "  PRIMARY KEY (`postId`)\n" +
//               ");");        
    }    
    
    private static void registerDB(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException{
        System.out.println("wtf");
        //String name = request.getParameter("name");
        //System.out.println("post :name: " + name);
        PrintWriter out = response.getWriter();
        //response.setContentType("text/html;charset=UTF-8");
        String email = request.getParameter("email");
        System.out.println("email: " + email);
        String pwd = request.getParameter("pwd");  
        System.out.println("pwd: " + pwd);
        String userName = request.getParameter("userName");
        System.out.println("userName: " + userName);
        String addr = request.getParameter("addr");
        System.out.println("addr: " + addr);
        String identity = request.getParameter("identity");
        System.out.println("identity: " + identity);
        
        int result = initializeConnection();
        if (result == -1) {
         out.write("db_getconnection_failed");
         out.flush();
         return;
        }    
        try {           
           Statement stmt = connection.createStatement();
           createAndUseDatabase(stmt);

           String s3 = "INSERT INTO " + USER_INFO_TABLE + " VALUES ('"+ email + "', '" + pwd + "', '" + userName + "', '" + addr + "', '" + identity + "');";
           System.out.println(s3);
           stmt.executeUpdate(s3);
                      
//           ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS cnt FROM userinfo;");
//           int rowCount = 0;
//           if (rs.next()) {
//                rowCount = rs.getInt("cnt");
//                System.out.println("Total number of lines in userinfo is: " + rowCount);
//            }
            out.write(REGISTER_SUCCESS);
            out.flush();
            
       } catch (SQLException e) {
        e.printStackTrace();
        out.write(REGISTER_FAIL);
        out.flush();
       } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        
    }
    private static void loginDB(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException{
        PrintWriter out = response.getWriter();
        String email = request.getParameter("email");
        System.out.println("email: " + email);
        String pwd = request.getParameter("pwd");  
        System.out.println("pwd: " + pwd);
        
        int result = initializeConnection();
        if (result == -1) {
         out.write("db_getconnection_failed");
         out.flush();
         return;
        } 
        
        int recordNum = 0;
        try {
            Statement stmt = connection.createStatement();
            
            createAndUseDatabase(stmt);
            
            String query = "SELECT COUNT(*) AS num FROM " + USER_INFO_TABLE + " WHERE email = binary '" + email + "' AND password = binary '" + pwd + "';";
            System.out.println("queryLogin: " + query);
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                recordNum = rs.getInt("num");
            }
            System.out.println("recordNUm: "  +recordNum);
            if (recordNum == 1) {
                String userName = null;
                String getUserName = "SELECT userName AS name FROM " + USER_INFO_TABLE + " WHERE email = binary '" + email + "';";
                System.out.println("getUserName: " + getUserName);
                ResultSet rs2 = stmt.executeQuery(getUserName);
                if (rs2.next()) {
                    userName = rs2.getString("name");
                }
                out.write(userName);
                out.flush();                
            } else {
                out.write(LOGIN_FAIL);
                out.flush(); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        
    }
    
    private static void viewActPosts (HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException{
        
        PrintWriter out = response.getWriter();
        int result = initializeConnection();
        if (result == -1) {
            out.write("db_getconnection_failed");
            out.flush();
            return;
        } 
        
        try {
            System.out.println("Begin View all posts!!!!!!!!!!");
            Statement stmt = connection.createStatement();
            
            createActPostTable(stmt);
            
            String showPosts = "SELECT * FROM " + ACT_POST_TABLE + ";";
            ResultSet rs = stmt.executeQuery(showPosts);
            List<JSONObject> results = new ArrayList<>();
            System.out.println("Create table and select all!!!!!!!!!!");
            while (rs.next()) {
                int postId = rs.getInt("postId");
                String postDate = rs.getString("postDate");
                String title = rs.getString("title");
                String postText = rs.getString("postText");
                String userName = rs.getString("userName");
                //String hashTag = rs.getString("hashTag");
                //int favorCount = rs.getInt("favorCount");
                System.out.println("userName: " + userName + " postId: " + postId +  " postDate: " + postDate + " title: " + title + "postText: " + postText);
                JSONObject oneRow = new JSONObject();
                try {
                    oneRow.put("postId", postId);
                    oneRow.put("userName", userName);
                    oneRow.put("title", title);
                    System.out.println("ONE JSON OBJECT: " + oneRow.toString());
                } catch (JSONException ex) {
                    Logger.getLogger(PocketBoxServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                results.add(oneRow);
            }
            
            if (results.isEmpty()) {
                System.out.println("No posts!!!!!!!!!!!!");
                out.write("");
                out.flush();
                return;
            }
            
            JSONArray resultJson = new JSONArray(results);
            System.out.println("JSONARRAY: " + resultJson.toString());
            out.write(resultJson.toString());
            out.flush();
            
        } catch (SQLException e) {
            
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
            
    }
    
    private static void newPost (HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException{
        PrintWriter out = response.getWriter();
        String title = request.getParameter("title");
        System.out.println("title: " + title);
        String text = request.getParameter("text");  
        System.out.println("text: " + text);
        String time = request.getParameter("time");  
        System.out.println("time: " + time);  
        String userName = request.getParameter("userName");  
        System.out.println("userName: " + userName); 
        
        int result = initializeConnection();
        if (result == -1) {
         out.write("db_getconnection_failed");
         out.flush();
         return;
        } 
        
        int postId = 0;
        try {
            Statement stmt = connection.createStatement();
            createReplyTable(stmt);
            stmt.executeUpdate("USE " + DB_NAME + ";");
            String insert = "INSERT INTO " + ACT_POST_TABLE + " (postDate, title, postText, userName) VALUES ('" + time + "', '" + title + "' , '" + text + "', '" + userName + "')";
            System.out.println("insert query: " + insert);
            stmt.executeUpdate(insert);
            
            String getId = "SELECT MAX(postId) AS maxId FROM " + ACT_POST_TABLE + ";";
            System.out.println("MAXID query: " + getId);
            ResultSet rs = stmt.executeQuery(getId);
            if (rs.next()) {
                postId = rs.getInt("maxId");
                System.out.println("this new post's id: " + postId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        
        out.write(String.valueOf(postId));
        out.flush();

    }
    
    private static void onePost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException{
        //PrintWriter out = response.getWriter();
        String postId = request.getParameter("postId");
        int result = initializeConnection();
//        if (result == -1) {
//            out.write("db_getconnection_failed");
//            out.flush();
//            return;
//        } 
        
        try {
            Statement stmt = connection.createStatement();
            
            //createActPostTable(stmt);
            
            stmt.executeUpdate("USE " + DB_NAME + ";");
            String showPosts = "SELECT * FROM " + ACT_POST_TABLE + " WHERE postId = " + postId + ";";
            System.out.println(showPosts);
            ResultSet rs = stmt.executeQuery(showPosts);
            List<JSONObject> results = new ArrayList<>();
            if (rs.next()) {
                String postDate = rs.getString("postDate");
                System.out.println("postDate: " + postDate);
                String postText = rs.getString("postText");
                System.out.println("postText: " + postText);
                String userName = rs.getString("userName");
                System.out.println("postDate: " + postDate + "postText: " + postText);
                JSONObject oneRow = new JSONObject();
                try {
                    oneRow.put("postDate", postDate);
                    oneRow.put("postText", postText);
                    System.out.println("ONE JSON OBJECT: " + oneRow.toString());
                } catch (JSONException ex) {
                    Logger.getLogger(PocketBoxServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                results.add(oneRow);
            }            
            
            allReply(postId, stmt, results, response);
            
        } catch (SQLException e) {
            
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        
    }
    
    private static void allReply (String postId, Statement stmt, List<JSONObject> results, HttpServletResponse response) throws SQLException, IOException {
            String showReply = "SELECT * FROM " + REPLY_TABLE + " WHERE postId = " + postId + ";";
            System.out.println("show reply query: " + showReply);
            ResultSet rsReply = stmt.executeQuery(showReply);
            System.out.println("23333333333333333");
            while (rsReply.next()) {
                System.out.println("enter result set reply");
                String reply = rsReply.getString("reply");
                System.out.println("reply: " + reply);
                String userName = rsReply.getString("userName");
                System.out.println("userName: " + userName);                
                JSONObject oneReply = new JSONObject();
                try {
                    oneReply.put("reply", reply);
                    oneReply.put("userName", userName);
                    System.out.println("ONE JSON REPLY: " + oneReply.toString());
                } catch (JSONException ex) {
                    Logger.getLogger(PocketBoxServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                results.add(oneReply);
            }
            JSONArray resultJson = new JSONArray(results);
            System.out.println("JSONARRAY: " + resultJson.toString());
            PrintWriter out = response.getWriter();
            out.write(resultJson.toString());
            out.flush();
    }
    private static void storeReply (HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException{
        //PrintWriter out = response.getWriter();
        String postId = request.getParameter("postId");  
        System.out.println("postId: " + postId);
        String reply = request.getParameter("reply");  
        System.out.println("reply: " + reply);  
        String userName = request.getParameter("userName");  
        System.out.println("userName: " + userName);
        
        int result = initializeConnection();
//        if (result == -1) {
//         out.write("db_getconnection_failed");
//         out.flush();
//         return;
//        } 
        int replyId = 0;
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("USE " + DB_NAME + ";");
            String insert = "INSERT INTO " + REPLY_TABLE + " (postId, reply, userName) VALUES (" + postId + ", '" + reply + "', '" + userName + "')";
            System.out.println("insert query: " + insert);
            stmt.executeUpdate(insert);
            
            List<JSONObject> results = new ArrayList<>();
            
            allReply(postId, stmt, results, response);
            
//            String getId = "SELECT MAX(replyId) AS maxId FROM " + REPLY_TABLE + ";";
//            System.out.println("MAXID query: " + getId);
//            ResultSet rs = stmt.executeQuery(getId);
//            
//            if (rs.next()) {
//                replyId = rs.getInt("maxId");
//                System.out.println("this new reply's id: " + replyId);
//            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private static void createReplyTable (Statement stmt) throws SQLException {
           //stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME + ";");          
           stmt.executeUpdate("USE " + DB_NAME + ";");
           stmt.executeUpdate
               ("CREATE TABLE IF NOT EXISTS " + REPLY_TABLE + " (\n" +
               "  `replyId` INT NOT NULL AUTO_INCREMENT,\n" +
               "  `postId` INT,\n" +       
               "  `reply` text,\n" + 
               "  `userName` text,\n" +        
               "  PRIMARY KEY (`replyId`)\n" +
               ");");     
    }
    private static void getInfo (HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException{
        //PrintWriter out = response.getWriter();
        String email = request.getParameter("email");  
        System.out.println("email: " + email); 
        
        int result = initializeConnection();
//        if (result == -1) {
//         out.write("db_getconnection_failed");
//         out.flush();
//         return;
//        } 
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("USE " + DB_NAME + ";");
            String select = "SELECT * FROM " + USER_INFO_TABLE + " WHERE email = '" + email + "';";
            System.out.println("select query: " + select);
            ResultSet rs = stmt.executeQuery(select);
            
            List<JSONObject> results = new ArrayList<>();
            if (rs.next()) {
                System.out.println("enter result set reply");
                String userName = rs.getString("userName");
                System.out.println("userName: " + userName);
                String address = rs.getString("address");
                System.out.println("address: " + address); 
                String identity = rs.getString("identity");
                System.out.println("identity: " + identity);               
                JSONObject info = new JSONObject();
                try {
                    info.put("userName", userName);
                    info.put("address", address);
                    info.put("identity", identity);
                    System.out.println("ONE JSON REPLY: " + info.toString());
                } catch (JSONException ex) {
                    Logger.getLogger(PocketBoxServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                results.add(info);
            }
            JSONArray resultJson = new JSONArray(results);
            System.out.println("JSONARRAY: " + resultJson.toString());
            PrintWriter out = response.getWriter();
            out.write(resultJson.toString());
            out.flush();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
     private static void edit (HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException{
        String email = request.getParameter("email");  
        System.out.println("email: " + email);         
        String userName = request.getParameter("userName");  
        System.out.println("userName: " + userName); 
        String address = request.getParameter("address");  
        System.out.println("address: " + address);   
        String identity = request.getParameter("identity");  
        System.out.println("identity: " + identity);        
        
        int result = initializeConnection();
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("USE " + DB_NAME + ";");
            String update = "UPDATE " + USER_INFO_TABLE + " SET userName = '" + userName + "', address = '" + address + "', identity = '" + identity + "' WHERE email = '" + email + "';";
            System.out.println("update query: " + update);
            stmt.executeUpdate(update);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        PrintWriter out = response.getWriter();
        out.write("EDIT SUCCESS");
        out.flush();
    }
}

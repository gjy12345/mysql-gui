package com.gjy.mysqlgui.util;

import com.gjy.mysqlgui.model.User;
import org.junit.Test;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

public class Dbutil {
    private static String USERNAME;
    private static String PASSWORD;
    private static int PORT;
    public static String getUSERNAME() {
        return USERNAME;
    }

    private static String DRIVER="com.mysql.jdbc.Driver";
    private static String URL="jdbc:mysql://127.0.0.1:3306/mysql?characterEncoding=utf-8";
    private static String IP;
    static{
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null,"加载驱动失败");
            System.exit(0);
        }
    }
    public static void setUser(String user,String pwd,int port,String ip){
        USERNAME=user;
        PASSWORD=pwd;
        PORT=port;
        IP=ip;
    }
    //测试
    public static boolean dbConnection(String user,String pwd,String port,String ip){
        try {
//            System.out.println("jdbc:mysql://"+ip+":"+port+"/mysql?characterEncoding=utf-8");
            DriverManager.getConnection("jdbc:mysql://"+ip+":"+port+"/mysql?characterEncoding=utf-8",user,pwd).close();
            User u=new User();
            u.setIp(ip);
            u.setUsername(user);
            u.setPassword(pwd);
            u.setPort(port);
            Sysutil.saveUser(u);
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,e.getMessage());
        }
        return false;
    }
    public static Connection getConnection(String user,String pwd,int port) throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://"+IP+":"+port+"/mysql?characterEncoding=utf-8&allowMultiQueries=true",user,pwd);
    }
    public static Connection getConnection(String dbName,String user,String pwd,int port) throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://"+IP+":"+port+"/"+dbName+"?characterEncoding=utf-8&allowMultiQueries=true",user,pwd);
    }
    public static Connection getConnection(String database) throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://"+IP+":"+PORT+"/"+database+"?characterEncoding=utf-8&allowMultiQueries=true",USERNAME,PASSWORD);
    }
    public static ArrayList<String> getDatabases() throws SQLException {
        ArrayList<String> dataBases=new ArrayList<>();
        Connection connection;
        connection=getConnection(USERNAME,PASSWORD,PORT);
        Statement statement=connection.createStatement();
        ResultSet rs=statement.executeQuery("show databases;");
        while(rs.next()){
            dataBases.add(rs.getString(1));
        }
        connection.close();
        return dataBases;
    }
    @Test
    public void test() throws SQLException {
        new Dbutil();
        USERNAME="root";
        PASSWORD="956115488";
        PORT=3306;
        IP="127.0.0.1";
        getDatabases();
    }
    public static void close(Connection connection){
        if(connection!=null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

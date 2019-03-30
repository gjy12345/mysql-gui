package com.gjy.mysqlgui.model;

public class User {
    private String username;
    private String password;
    private String port;
    private String ip;

    public User(){
        this.ip="127.0.0.1";
        this.port="3306";
        this.username="";
        this.password="";
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}

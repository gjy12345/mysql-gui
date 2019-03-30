package com.gjy.mysqlgui.util;

import com.gjy.mysqlgui.model.User;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Properties;

public class Sysutil {
    private static int SCREENWIDTH;
    private static int SCREENHEIGHT;
    private static String OSNAME;
    private static String FONTPATH;
    private static String RESPATH;
    private static String JARPATH;
    private static String USERINFOPATH;
    public static String getFONTPATH() {
        return FONTPATH;
    }
    static {
        Dimension screensize   =   Toolkit.getDefaultToolkit().getScreenSize();
        SCREENWIDTH = (int)screensize.getWidth();
        SCREENHEIGHT = (int)screensize.getHeight();
        Properties props=System.getProperties();
        OSNAME= props.getProperty("os.name");
        if(OSNAME.equals("Linux")){
            if(SCREENWIDTH>SCREENHEIGHT*3){
                SCREENWIDTH/=2;
            }
        }
        FONTPATH="/com/gjy/mysqlgui/MSYH/MSYH.TTF";
        RESPATH="/com/gjy/mysqlgui/res";
        JARPATH=getProjectPath();
        USERINFOPATH=JARPATH+"user.properties";
        File f=new File(USERINFOPATH);
        if(!f.exists()){
            try {
                if(!f.createNewFile()){
                    JOptionPane.showMessageDialog(null,"配置文件建立失败 检查文件夹权限："+f.getAbsolutePath());
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null,"配置文件建立失败 检查文件夹权限："+f.getAbsolutePath());
            }
        }
    }
    public static String getProjectPath() {

        java.net.URL url = Sysutil.class.getProtectionDomain().getCodeSource().getLocation();
        String filePath = null;
        try {
            filePath = java.net.URLDecoder.decode(url.getPath(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (filePath.endsWith(".jar"))
            filePath = filePath.substring(0, filePath.lastIndexOf("/") + 1);
        java.io.File file = new java.io.File(filePath);
        filePath = file.getAbsolutePath();
        return filePath;
    }
    public static String getRESPATH() {
        return RESPATH;
    }

    public static int getSCREENWIDTH() {
        return SCREENWIDTH;
    }

    public static int getSCREENHEIGHT() {
        return SCREENHEIGHT;
    }

    /**
     * @param jFrame
     * @param ratio
     * 百分比设置居中并按屏幕尺寸的百分比设置窗口大小
     */
    public static void setWindowCenter(JFrame jFrame,double ratio){
        if(jFrame==null){
            throw new NullPointerException("Sysutile.setWindowCenter:jframe==null");
        }
        //百分比
        int width= (int) (SCREENWIDTH*ratio);
        int height= (int) (SCREENHEIGHT*ratio);
        jFrame.setBounds(getSCREENWIDTH()/2-width/2,getSCREENHEIGHT()/2-height/2,width,height);
    }
    public static void setWindowCenter(JFrame jFrame,int width,int height){
        if(jFrame==null){
            throw new NullPointerException("Sysutile.setWindowCenter:jframe==null");
        }
        jFrame.setBounds(getSCREENWIDTH()/2-width/2,getSCREENHEIGHT()/2-height/2,width,height);
    }
    public static User getUser(){
        User user=new User();
        FileInputStream fileInputStream;
        try {
            fileInputStream=new FileInputStream(new File(USERINFOPATH));
        } catch (FileNotFoundException e) {
            return user;
        }
        Properties userProperties=new Properties();
        try {
            userProperties.load(fileInputStream);
            user.setUsername(userProperties.getProperty("username"));
            user.setPassword(userProperties.getProperty("password"));
            user.setIp(userProperties.getProperty("ip"));
            user.setPort(userProperties.getProperty("port"));
        } catch (IOException e) {
//            e.printStackTrace();
            JOptionPane.showMessageDialog(null,e.getMessage());
        }
        return user;
    }
    public static void saveUser(User user){
        new Thread(){
            @Override
            public void run() {
                super.run();
                FileOutputStream fileOutputStream;
                FileInputStream fileInputStream;
                try {
                    fileInputStream=new FileInputStream(new File(USERINFOPATH));
                    fileOutputStream=new FileOutputStream(new File(USERINFOPATH));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return;
                }
                Properties userProperties=new Properties();
                try {
                    userProperties.load(fileInputStream);
                    userProperties.setProperty("username",user.getUsername());
                    userProperties.setProperty("password",user.getPassword());
                    userProperties.setProperty("ip",user.getIp());
                    userProperties.setProperty("port",user.getPort());
                    userProperties.store(fileOutputStream,"1");
                    fileOutputStream.close();
                } catch (IOException e) {
//                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null,e.getMessage());
                }
            }
        }.start();
    }
    public static void main(String[] args){
        User user=new User();
        user.setUsername("root");
        user.setPassword("956115488");
        user.setIp("127.0.0.1");
        user.setPort("3306");
        Sysutil.saveUser(user);
    }
}

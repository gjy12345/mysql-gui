package com.gjy.mysqlgui.interfaces;

import com.gjy.mysqlgui.model.User;
import com.gjy.mysqlgui.util.Dbutil;
import com.gjy.mysqlgui.util.Sysutil;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import javax.swing.*;
import java.awt.*;

public class Login extends BaseJframe{
    private JTextField username;
    private JPasswordField password;
    private JTextField port;
    private JButton submit;
    private JTextField ip;
    private static User user;
    static {
        user=Sysutil.getUser();
    }
    public Login(){
        super();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Sysutil.setWindowCenter(this,300,280);
        this.setControl();
        this.setResizable(false);
        submit.addActionListener(e->submit());
        this.setVisible(true);
    }
    private void submit() {
        String user=username.getText().trim();
        String pwd=new String(password.getPassword());
        String pt=port.getText().trim();
        String ipstr=ip.getText().trim();
        if(user.isEmpty()||pwd.isEmpty()||pt.isEmpty()||ipstr.isEmpty()){
            JOptionPane.showMessageDialog(this,"请填写全部输入框");
            return;
        }
        if(pt.matches("\\d+")){
            Dbutil.setUser(user,pwd,Integer.valueOf(pt),ipstr);
            if(Dbutil.dbConnection(user,pwd,pt,ipstr)){
                this.dispose();
                new MainInterface();
            }
        }
    }
    private void setControl(){
        this.setLayout(new BorderLayout());
        JPanel back=new JPanel();
        back.setLayout(null);
        this.add(back);
        username=new JTextField();
        back.add(username);
        password=new JPasswordField();
        port=new JTextField();
        submit=new JButton("连接");
        ip=new JTextField();
        ip.setText(user.getIp());
        port.setText(user.getPort());
        username.setText(user.getUsername());
        password.setText(user.getPassword());
        back.add(addControl(new JLabel("mysql-gui"),120,10,60,20));
        back.add(addControl(new JLabel("账号:"),60,50,60,18));
        back.add(addControl(username,100,50,120,20));
        back.add(addControl(new JLabel("密码:"),60,80,60,18));
        back.add(addControl(password,100,80,120,20));
        back.add(addControl(new JLabel("地址:"),60,110,60,18));
        back.add(addControl(ip,100,110,120,20));
        back.add(addControl(new JLabel("端口:"),60,140,60,18));
        back.add(addControl(port,100,140,60,20));
        back.add(addControl(submit,120,180,60,30));
    }
    private Component addControl(Component component,int x,int y,int width,int height){
        component.setBounds(x,y,width,height);
        return component;
    }
    public static void main(String[] args){
        try {
            BeautyEyeLNFHelper.frameBorderStyle = org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
            org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
            UIManager.put("RootPane.setupButtonVisible",false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Login();
    }
}

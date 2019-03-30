package com.gjy.mysqlgui.interfaces;

import com.gjy.mysqlgui.Threads.GetResult;
import com.gjy.mysqlgui.util.Sysutil;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SearchInterface extends BaseJframe {
    private ArrayList<String> databases;
    private JComboBox<String> db;
    private JPanel topPanel;
    private JPanel bottomPanel;
    private JTextArea sqlInput;
    private String nowDb;
    private JButton submit;
    private GetResult getResultThread=null;
    private String sql;

    public void setNowDb(String nowDb) {
        this.nowDb = nowDb;
        if(this.nowDb!=null&&this.db!=null){
            this.db.setSelectedItem(this.nowDb);
        }
    }

    public SearchInterface(ArrayList<String> databases, String nowDb){
        Sysutil.setWindowCenter(this,0.3);
        this.nowDb=nowDb;
        this.databases=databases;
        this.splitTopBottom();
        this.setVisible(true);
        sqlInput.requestFocus();
        this.setTitle("查询");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//修改为关闭隐藏
    }
    private void splitTopBottom(){
        JSplitPane js=new JSplitPane();
        topPanel=new JPanel();
        bottomPanel=new JPanel();
        js.setOrientation(JSplitPane.VERTICAL_SPLIT);
        js.setEnabled(false);
        js.setTopComponent(topPanel);
        js.setBottomComponent(bottomPanel);
        js.setDividerSize(1);
        js.setDividerLocation((int)(this.getHeight()*0.15));
        this.setContentPane(js);
        this.setDb();
        this.setBottomPanel();
    }
    private void setDb(){
        db=new JComboBox<>();
        for(String dbName:databases){
            db.addItem(dbName);
        }
        if(nowDb!=null)
            db.setSelectedItem(nowDb);
        else
            db.setSelectedIndex(0);
        topPanel.setLayout(new BoxLayout(topPanel,BoxLayout.X_AXIS));
        topPanel.add(new JLabel("当前数据库:"));
        topPanel.add(db);
        submit=new JButton("运行");
        topPanel.add(submit);
        submit.addActionListener(e -> {doSubmit();});
    }
    private void doSubmit(){
        sql=sqlInput.getText().trim();
        if(getResultThread==null){
            if(sql.isEmpty()){
               JOptionPane.showMessageDialog(this,"请输入sql语句！");
               return;
            }
            sqlInput.setEnabled(false);
            getResultThread=new GetResult(sql,(String)db.getSelectedItem());
            getResultThread.start();
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    int t=0;
                    while(getResultThread.isAlive()){
                        try {
                            submit.setText(String.valueOf(t/10.0));
                            Thread.sleep(100);
                            t++;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    ArrayList<JFrame> resultInterface=getResultThread.getResultInterface();
                    if(resultInterface!=null){
                        for(JFrame result:resultInterface){
                            result.setVisible(true);
                        }
                    }
                    getResultThread=null;
                    submit.setText("运行");
                    sqlInput.setEnabled(true);
                }
            }.start();
        }else{
            getResultThread.interrupt();
        }
    }
    private void setBottomPanel(){
        bottomPanel.setLayout(new BorderLayout());
        sqlInput=new JTextArea();
        bottomPanel.add(sqlInput);
    }
}

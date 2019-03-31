package com.gjy.mysqlgui.Threads;

import com.gjy.mysqlgui.dao.TableDaoImp;
import com.gjy.mysqlgui.interfaces.ResultInterface;
import com.gjy.mysqlgui.util.Dbutil;
import com.gjy.mysqlgui.util.Sysutil;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class GetResult extends Thread {
    private String sql;
    private String dbName;
    private ArrayList<JFrame> resultInterface;
    public GetResult(String sql,String dbName){
        this.sql=sql;
        this.dbName=dbName;
    }
    public ArrayList<JFrame> getResultInterface(){
        return resultInterface;
    }
    @Override
    public void run() {
        super.run();
        Connection connection=null;
        JFrame info=new JFrame();
        Sysutil.setWindowCenter(info,0.4);
        info.setLayout(new BorderLayout());
        info.setTitle("结果");
        JTextArea jTextArea=new JTextArea();
        JScrollPane js=new JScrollPane(jTextArea);
        js.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        js.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        info.add(js);
        try {
            resultInterface=new ArrayList<>();
            connection=Dbutil.getConnection(dbName);
            Statement statement=connection.createStatement();
            boolean result=statement.execute(sql);
            ResultSet rs;
            while(true) {
                if(result) {
                    rs=statement.getResultSet();
                    ResultSetMetaData rmd=rs.getMetaData();
                    ArrayList<String[]> rows;
                    String head[]=new String[rmd.getColumnCount()];
                    for(int i=1;i<=rmd.getColumnCount();i++){
                        head[i-1]=rmd.getColumnLabel(i);//修改获取表标题不一致
                    }
                    rows=new TableDaoImp().getRows(head,rs);
                    resultInterface.add(new ResultInterface(head,rows,"共"+rows.size()+"结果"));
                }else {
                    int count=statement.getUpdateCount();
                    if(count!=-1){
                        jTextArea.append(count+"行受影响"+"\n");
                        if(!info.isVisible()){
                            info.setVisible(true);
                        }
                    }
                    else
                        break;
                }
                result=statement.getMoreResults();//获取下一个结果 返回结果集 true 条数 false 当返回false 并且 结果集为-1 结束
            }
        } catch (SQLException e) {
            if(!info.isVisible()){
                info.setVisible(true);
            }
            jTextArea.append(e.getMessage()+"\n");
        }
        finally {
            Dbutil.close(connection);
        }
    }
}

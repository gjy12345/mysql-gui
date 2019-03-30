package com.gjy.mysqlgui.Threads;

import com.gjy.mysqlgui.dao.TableDaoImp;
import com.gjy.mysqlgui.util.Dbutil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class GetTable extends Thread {
    private String dbName;
    private String tbName;
    private JPanel tbPanel;
    private JTable jTable;

    public GetTable(String dbName, String tbName, JPanel tbPanel) {
        this.dbName = dbName;
        this.tbName = tbName;
        this.tbPanel = tbPanel;
    }
    @Override
    public void run() {
        super.run();
        tbPanel.removeAll();
        Connection connection=null;
        try {
            connection= Dbutil.getConnection(dbName);
            PreparedStatement ps=connection.prepareStatement("select * from "+tbName+" limit 0,100;");
//            System.out.println("table:"+tbName);
//            ps.setString(1,tbName);
            ResultSet rs=ps.executeQuery();
            ResultSetMetaData rmd=rs.getMetaData();
            String title[]=new String[rmd.getColumnCount()];
            for(int i=1,count=rmd.getColumnCount();i<=count;i++){
                title[i-1]=rmd.getColumnLabel(i);
            }
            DefaultTableModel dftm=new DefaultTableModel();
            jTable=new JTable(dftm);
            dftm.setColumnIdentifiers(title);
            JTableHeader head=jTable.getTableHeader();
            tbPanel.add(BorderLayout.CENTER,jTable);
            tbPanel.add(BorderLayout.NORTH,head);
            ArrayList<String[]> rows=new TableDaoImp().getRows(title,rs);
            if(rows!=null){
                for(String[] row:rows){
                    dftm.addRow(row);
                }
            }
            tbPanel.revalidate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,e.getMessage());
        }finally {
            if(connection!=null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

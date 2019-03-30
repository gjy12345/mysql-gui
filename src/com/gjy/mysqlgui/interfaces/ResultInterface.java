package com.gjy.mysqlgui.interfaces;

import com.gjy.mysqlgui.util.Sysutil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class ResultInterface extends BaseJframe {
    private String[] head;
    private List<String[]> rows;
    private JTable jTable;
    public ResultInterface(String[] head, List<String[]> rows,String tbName){
        Sysutil.setWindowCenter(this,0.6);
        this.head=head;
        this.rows=rows;
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setTitle(tbName);
        this.init();
//        this.setVisible(true);
    }
    private void init(){
        DefaultTableModel dftm=new DefaultTableModel();
        JPanel j=new JPanel(new BorderLayout());
        jTable=new JTable(dftm);
        dftm.setColumnIdentifiers(head);
        JTableHeader head=jTable.getTableHeader();
        JScrollPane js=new JScrollPane(j);
        js.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        js.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        j.add(BorderLayout.CENTER,jTable);
        j.add(BorderLayout.NORTH,head);
        for(String[] row:rows){
            dftm.addRow(row);
        }
        this.add(js);
    }
}

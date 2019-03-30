package com.gjy.mysqlgui.dao;

import com.gjy.mysqlgui.util.Dbutil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class TableDaoImp implements TableDao {
    @Override
    public ArrayList<String> getTables(String dbName) throws SQLException {
        Connection connection= Dbutil.getConnection(dbName);
        Statement statement=connection.createStatement();
        ResultSet rs=statement.executeQuery("show tables;");
        ArrayList<String> tables=new ArrayList<>();
        while (rs.next()){
            tables.add(rs.getString(1));
        }
        connection.close();
        return tables;
    }

    @Override
    public ArrayList<String[]> getRows(String[] head,ResultSet resultSet) throws SQLException {
        ArrayList<String[]> arrayList=new ArrayList<>();
        String row[];
        while (resultSet.next()){
            row=new String[head.length];
            for(int i=0;i<head.length;i++){
                row[i]=resultSet.getString(head[i]);
            }
            arrayList.add(row);
        }
        return arrayList;
    }
}

package com.gjy.mysqlgui.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public interface TableDao {
    ArrayList<String> getTables(String dbName) throws SQLException;
    ArrayList<String[]> getRows(String[] head, ResultSet resultSet) throws SQLException;
}

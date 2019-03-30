package com.gjy.mysqlgui.Threads;
import com.gjy.mysqlgui.dao.TableDao;
import com.gjy.mysqlgui.dao.TableDaoImp;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.sql.SQLException;
import java.util.ArrayList;

public class GetTables extends Thread {
    private JTree jTree;
    private DefaultMutableTreeNode node;
    private TreePath path;
    public GetTables(JTree jTree, DefaultMutableTreeNode node, TreePath path){
        this.jTree=jTree;
        this.node=node;
        this.path=path;
    }
    @Override
    public void run() {
        super.run();
        TableDao tableDao=new TableDaoImp();
        try {
            ArrayList<String> tables= tableDao.getTables(node.toString());
            for(String table:tables){
                node.insert(new DefaultMutableTreeNode(table),node.getChildCount());
            }
            jTree.revalidate();
            jTree.expandPath(path);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,e.getMessage());
        }
    }
}

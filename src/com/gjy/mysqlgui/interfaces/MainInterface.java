package com.gjy.mysqlgui.interfaces;
import com.gjy.mysqlgui.Threads.GetTable;
import com.gjy.mysqlgui.Threads.GetTables;
import com.gjy.mysqlgui.util.Dbutil;
import com.gjy.mysqlgui.util.Sysutil;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainInterface extends BaseJframe {
    private JPanel topPanel;
    private JPanel bottomPanel;
    private JPanel bottomPanelLeft;
    private JPanel bottomPanelRight;
    private JPanel dataBasesPane;
    private JTree tree;
    private DefaultTreeModel dataBaseTreeModel;
    private DefaultMutableTreeNode root;
    private JPopupMenu menu;
    private JPopupMenu m;
    private JPanel tableResultPane;
    private ArrayList<String> dbs;
    private SearchInterface searchJframe=null;
    private String nowDb;
    public MainInterface(){
        Sysutil.setWindowCenter(this,0.7);
        m=new JPopupMenu();		//创建菜单
        m.add(new JMenuItem("刷新"));
        menu=new JPopupMenu();		//创建菜单
        menu.add(new JMenuItem("刷新"));
        menu.add(new JMenuItem("查询"));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("mysql:"+ Dbutil.getUSERNAME());
        this.cutUpAndDown();
        this.setTopPanel();
        this.setVisible(true);
        this.setBottomPanel();
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                bottomPanelLeft.setBounds(0,0,(int)(0.15*bottomPanel.getWidth()),bottomPanel.getHeight());
                bottomPanelRight.setBounds((int)(0.15*bottomPanel.getWidth()),0,
                        bottomPanel.getWidth()-(int)(0.15*bottomPanel.getWidth()),bottomPanel.getHeight());
                MainInterface.this.revalidate();
            }
        });
        this.setDateBasesPane();
    }
    private void setTopPanel(){
        //上部导航栏
        topPanel.setLayout(new BoxLayout(topPanel,BoxLayout.X_AXIS));
        JButton search=new JButton("查询");
        search.addActionListener(e -> {
            if(searchJframe==null){
                System.out.println(nowDb);
                searchJframe=new SearchInterface(dbs,nowDb);
            }else {
                System.out.println(nowDb);
                searchJframe.setNowDb(nowDb);
                searchJframe.setVisible(true);
            }
        });
        topPanel.add(search);
        JButton re=new JButton("刷新");
        re.addActionListener(e -> {setDateBasesPane();});
        topPanel.add(re);
    }
    private void setBottomPanel(){
        //下部主页
        bottomPanel.setLayout(null);
        bottomPanelLeft=new JPanel(new BorderLayout());
        bottomPanelLeft.setBorder(BorderFactory.createLineBorder(Color.black,1));
        bottomPanelLeft.setBounds(0,0,(int)(0.15*bottomPanel.getWidth()),bottomPanel.getHeight());
        bottomPanel.add(bottomPanelLeft);
        bottomPanelRight=new JPanel(new BorderLayout());
        bottomPanelRight.setBorder(BorderFactory.createLineBorder(Color.black,1));
        bottomPanelRight.setBounds((int)(0.15*bottomPanel.getWidth()),0,
                bottomPanel.getWidth()-(int)(0.15*bottomPanel.getWidth()),bottomPanel.getHeight());
        bottomPanel.add(bottomPanelRight);
        //右结果
        tableResultPane=new JPanel(new BorderLayout());
//        tableResultPane.setBackground(Color.cyan);
        JScrollPane jsTable=new JScrollPane(tableResultPane);
        jsTable.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jsTable.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        bottomPanelRight.add(jsTable);
        dataBasesPane=new JPanel();
        JScrollPane jScrollPane=new JScrollPane(dataBasesPane);
        jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        bottomPanelLeft.add(jScrollPane);
        dataBasesPane.setLayout(new BorderLayout());
        this.revalidate();
//        this.setDateBasesPane();
    }
    private void setDateBasesPane(){
        dataBasesPane.removeAll();
        root=new DefaultMutableTreeNode("数据库");
        dataBaseTreeModel = new DefaultTreeModel(root);
        try {
            dbs=Dbutil.getDatabases();
            for(String database:dbs){
                dataBaseTreeModel.insertNodeInto(new DefaultMutableTreeNode(database),root,root.getChildCount());
            }
            tree=new JTree(dataBaseTreeModel);
            dataBasesPane.add(tree);
            tree.setEditable(false);
            setTreeListen(tree);
            this.revalidate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,e.getMessage());
        }
    }
    private void setTreeListen(JTree tree){
        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 如果在这棵树上点击了2次,即双击
                if (e.getSource() == tree && e.getClickCount() == 2) {
                    // 按照鼠标点击的坐标点获取路径
                    TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
                    if (selPath != null)// 谨防空指针异常!双击空白处是会这样
                    {
                        if(selPath.getPathCount()==2){
//                            System.out.println(selPath);// 输出路径看一下
                            // 获取这个路径上的最后一个组件,也就是双击的地方
                            DefaultMutableTreeNode node = (DefaultMutableTreeNode) selPath.getLastPathComponent();
                            nowDb=node.toString();
//                            System.out.println(nowDb);
//                            System.out.println(node.toString());// 输出这个组件toString()的字符串看一下
                            if(node.getChildCount()>0){
                                System.out.println("已加载");
                                return;
                            }
                            new GetTables(tree,node,selPath).start();
                        }else if(selPath.getPathCount()==3){
//                            String tb=selPath.getPathComponent(1).toString()+"  "+selPath.getPathComponent(2).toString();
//                            System.out.println(tb);
                            new GetTable(selPath.getPathComponent(1).toString()
                                    ,selPath.getPathComponent(2).toString(),tableResultPane).start();

                        }
                    }
                }

            }
        });
    }
    private void cutUpAndDown(){
        JSplitPane jSplitPane=new JSplitPane();
        jSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        topPanel=new JPanel();
        bottomPanel=new JPanel();
        jSplitPane.setEnabled(false);
        jSplitPane.setTopComponent(topPanel);
        jSplitPane.setBottomComponent(bottomPanel);
        jSplitPane.setDividerSize(1);
        jSplitPane.setDividerLocation((int)(this.getHeight()*0.05));
        this.setContentPane(jSplitPane);
    }
    public static void main(String[] args){
        Dbutil.setUser("root","956115488",3306,"127.0.0.1");
        new MainInterface();
    }
}

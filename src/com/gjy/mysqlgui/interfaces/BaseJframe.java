package com.gjy.mysqlgui.interfaces;

import com.gjy.mysqlgui.util.Sysutil;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class BaseJframe extends JFrame {
    private static Font FONT;
    static {
        try {
            InputStream fileInputStream=BaseJframe.class.getResourceAsStream(Sysutil.getFONTPATH());
            BufferedInputStream bis=new BufferedInputStream(fileInputStream);
            FONT=Font.createFont(Font.TRUETYPE_FONT,bis);
            float f=12f;
            FONT=FONT.deriveFont(f);
            System.out.println(FONT.getSize());
            bis.close();
        } catch (FontFormatException | IOException e) {
            JOptionPane.showMessageDialog(null,e.getMessage());
        }
    }
    public BaseJframe(){
        UIManager.put("Table.font",FONT);
        UIManager.put("Label.font", FONT);
        UIManager.put("Button.font",FONT);
        //JTableHeader
        UIManager.put("TableHeader.font",FONT);
        UIManager.put("Tree.font",FONT);
        UIManager.put("Table.font",FONT);
        UIManager.put("ComboBox.font",FONT);
        UIManager.put("TextArea.font",FONT);
        UIManager.put("Frame.font",FONT);
        UIManager.put("TextField.font",FONT);
    }
}

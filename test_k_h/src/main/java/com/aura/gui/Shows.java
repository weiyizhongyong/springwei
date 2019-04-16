package com.aura.gui;

import javax.swing.*;
import java.io.*;

public class Shows  extends JFrame{
    static  JTextArea ss;
    JFrame j1;
    Shows(){
        ss = new JTextArea();
        j1 = new JFrame("敏感词汇文本显示窗口");
        String ss1 = filein();
        j1.add(ss);
        j1.setSize(500,400);
        j1.setVisible(true);
    }

    private String filein() {
        String f= "D:/sensitive.txt";
        String str = "";
        int i =0;
        try {
            //BufferedReader br = new BufferedReader(new FileReader(f));
            InputStreamReader reader = new InputStreamReader(new FileInputStream(new File(f)), "GBK");
            BufferedReader br = new BufferedReader(reader);
            while ((str =br.readLine())!=null){

                //str = new String(str.getBytes("GBK"),"UTF-8");//将读取出来的GBK格式的代码转换成UTF-8
                i++;
                str = str + "\n";
                ss.append(str);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str ;
    }
}

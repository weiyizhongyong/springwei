package com.aura.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class TestJMenu extends JFrame implements ActionListener {
    JTextArea ita = new JTextArea();
    TestJMenu(){
        this.setSize(400,300);
        JMenuBar jmb = new JMenuBar();
        this.setTitle("分析程序");
        //创建菜单栏
        JMenu imFile = new JMenu("文件");
        JMenuItem imNew= new JMenuItem("新建");
        JMenuItem imiOpen = new JMenuItem("打开");
        imiOpen.addActionListener(this);
        imFile.add(imNew);
        imFile.add(imiOpen);

        JMenu imFenxi = new JMenu("分析");
        JMenuItem jmiSure= new JMenuItem("确定");
        JMenuItem jmiCancel = new JMenuItem("取消");
        imFenxi.add(jmiSure);
        imFenxi.add(jmiCancel);

        JMenu jmHelp = new JMenu("帮助");
        jmb.add(imFile);
        jmb.add(imFenxi);
        jmb.add(jmHelp);
        this.setJMenuBar(jmb);
        this.getContentPane().add(ita);
        this.setVisible(true);
        //设置事件监听，如果点击了分析中的确定，表名要进行分析测试
        jmiSure.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String filterText = ita.getText();
                try {
                    ita.setText(Fenx.getFilterText(filterText));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser jc = new JFileChooser();
        if(jc.showOpenDialog(this) != 1){
            try{
                File file = jc.getSelectedFile();
                FileInputStream fis = new FileInputStream(file);
                byte[] buf = new byte[10 * 1024];
                int len = fis.read(buf);
                ita.append(new String(buf,0,len));
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        MinGanCiHui m = new MinGanCiHui();
        TestJMenu imFrame = new TestJMenu();
        //imFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}

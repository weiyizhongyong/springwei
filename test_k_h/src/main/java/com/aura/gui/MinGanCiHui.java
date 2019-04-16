package com.aura.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MinGanCiHui {
    public static void main(String[] args) {
        JFrame f1 = new JFrame("敏感词汇分析");
        f1.setBackground(Color.lightGray);
        f1.setBounds(100,100,500,400);
        f1.setLayout(new GridLayout(2,1));
        Panel p1 = new Panel();
        f1.add(p1);
        Panel p2 = new Panel();
        f1.add(p2);
        f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel l2 = new JLabel("敏感词汇保存在sensitive.txt文件中，查看敏感词汇请点击：");
        p1.add(l2);
        JButton b1 = new JButton("我要查看");
        p1.add(b1);
        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Shows ss = new Shows();
            }
        });
        JLabel l1 = new JLabel("请输入含有敏感词的文本文件，点确定进入：");
        p2.add(l1);
        JButton b2 = new JButton("确定");
        p2.add(b2);
        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                TestJMenu imFrame = new TestJMenu();
            }
        });
        f1.setVisible(true);



    }
}

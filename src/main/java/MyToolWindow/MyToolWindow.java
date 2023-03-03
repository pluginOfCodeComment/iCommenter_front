package MyToolWindow;

import application.Context;
import application.formatComment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyToolWindow {
    private Context context;
    private JPanel window;
    private JButton Feedback2;
    private JButton Apply1;
    private JButton Feedback1;
    private JButton Apply2;
    private JButton Apply3;
    private JButton Feedback3;
    private JTextArea comment1;
    private JTextArea comment2;
    private JTextArea comment3;
    private JLabel model1;
    private JLabel model2;
    private JLabel model3;
    private JLabel model4;

    private JScrollPane 滚动面板;

    private JTextArea comment4;
    private JButton Apply4;
    private JButton Feedback4;
    private JPanel window1;
    private JScrollPane scoll1;
    private JScrollPane scoll2;
    private JScrollPane scoll3;
    private JScrollPane scoll4;
    private JSeparator sep1;
    private JSeparator sep2;
    private JSeparator sep3;

    //    private float dval;//未改变尺寸时的长度

    //获取注释内容
    public void setComment(formatComment[] fc) {
        comment1.setText("Code comment 1: " + fc[0].getComment());
        comment2.setText("Code comment 2: " + fc[1].getComment());
        comment3.setText("Code comment 3: " + fc[2].getComment());
        comment4.setText("Code comment 4: " + fc[3].getComment());
        comment1.setBorder(null);
        comment2.setBorder(null);
        comment3.setBorder(null);
        comment4.setBorder(null);
    }

    public MyToolWindow(Context c) {
        this.context = c;
        TextBorderUtlis Border = new TextBorderUtlis(new Color(71,71,71),1,false);
        scoll1.setBorder(Border);
        scoll2.setBorder(Border);
        scoll3.setBorder(Border);
        scoll4.setBorder(Border);

        Apply1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                context.insert(0);
                JOptionPane.showMessageDialog(window,"success");
            }
        });
        Apply2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                context.insert(1);
                JOptionPane.showMessageDialog(window,"success");

            }
        });
        Apply3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                context.insert(2);
                JOptionPane.showMessageDialog(window,"success");
            }
        });
        Apply4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                context.insert(3);
                JOptionPane.showMessageDialog(window,"success");
            }
        });
        Feedback1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                FeedBack.show();
            }

        });
        Feedback2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                FeedBack.show();
            }
        });
        Feedback3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                FeedBack.show();
            }
        });
        Feedback4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                FeedBack.show();
            }
        });
    }

    public JComponent getContent() {
        return window1;
    }


}

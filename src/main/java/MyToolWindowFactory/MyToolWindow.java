package MyToolWindowFactory;

import application.Context;

import javax.swing.*;
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
    private JLabel comment1;
    private JLabel comment2;
    private JLabel comment3;

    //获取注释内容
    public void setComment1() {
        comment1.setText("Code comment 1: This is code comment 1.(get comment)");
    }
    public void setComment2() {
        comment2.setText("Code comment 2: This is code comment 2.(get comment)");
    }
    public void setComment3() {
        comment3.setText("Code comment 3: This is code comment 3.(get comment)");
    }

    public MyToolWindow(Context c) {
        this.context = c;
        Feedback2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                FeedBack.show();
            }
        });

        Apply1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                context.insert("#this is comment 1\n");
                JOptionPane.showMessageDialog(window,"success");
            }
        });

        Feedback1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                FeedBack.show();
            }
        });

        Apply2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                context.insert("#this is comment 2\n");
                JOptionPane.showMessageDialog(window,"success");

            }
        });

        Apply3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                context.insert("#this is comment 3\n");
                JOptionPane.showMessageDialog(window,"success");
            }
        });

        Feedback3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                FeedBack.show();
            }
        });
    }

    public void show() {
        this.setComment1();
        this.setComment2();
        this.setComment3();
        JFrame frame = new JFrame("ToolWindow");
        frame.setContentPane(this.window);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400,500);
        frame.setVisible(true);
//        setComment1();

    }

//    private void createUIComponents() {
//        // TODO: place custom component creation code here
//    }
}

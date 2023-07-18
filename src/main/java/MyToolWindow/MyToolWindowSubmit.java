package MyToolWindow;

import UploadAndDownload.MyClient;
import application.Context;
import application.Comment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Arrays;

public class MyToolWindowSubmit {
    private static int feedback_id=-1;
    private int[] score = new int[3];
    private Context context;

    private JTextArea comment;
    private JRadioButton a1;
    private JRadioButton a2;
    private JRadioButton a3;
    private JRadioButton a4;
    private JRadioButton a5;
    private JRadioButton b1;
    private JRadioButton b2;
    private JRadioButton b3;
    private JRadioButton b4;
    private JRadioButton b5;
    private JRadioButton c1;
    private JRadioButton c2;
    private JRadioButton c3;
    private JRadioButton c4;
    private JRadioButton c5;
    private JTextArea commentOfUser;
    private JScrollPane scrollOfComment;
    private JPanel window;
    private JTextArea tips;
    private JTextArea Q1;
    private JTextArea Q2;
    private JTextArea Q3;
    private JButton Apply;
    private JButton submit;
    private JButton feedback;
    private JScrollPane tipsScroll;
    private JScrollPane usrComment;
    private JScrollPane Q1Scr;
    private JPanel Q1Ans;
    private JScrollPane Q2scr;
    private JPanel Q2Ans;
    private JScrollPane Q3Scr;
    private JPanel Q3Ans;

    //获取注释内容
    public void setComment(Comment fc) {
        comment.setText("Code comment : " + fc.getComment());
    }

    public MyToolWindowSubmit(Context c) {
        tips.setText("Please indicate the level to which you agree with the following statements:"+"\n"+"(1: Strongly disagree 2: Disagree 3: Neutral 4: Agree 5: Strongly Agree)");
        Q1.setText("1.Independent of other factors, I feel that the summary is accurate.");
        Q2.setText("2.The summary is missing important information, and that can hinder the understanding of the method.");
        Q3.setText("3.The summary contains a lot of unnecessary information.");
        comment.setBorder(null);
        feedback.setContentAreaFilled(false);
        feedback.setBorderPainted(false);
        feedback.setPreferredSize(new Dimension(20,2));
//        feedback.setBorder(null);
//        feedback.setTransferHandler(null);
        this.context = c;
        TextBorderUtlis Border = new TextBorderUtlis(new Color(71,71,71),1,false);
        scrollOfComment.setBorder(Border);
//        scrollOfFeedback.setBorder(Border);
        a1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                score[0]=1;
            }
        });
        a2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                score[0]=2;
            }
        });
        a3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                score[0]=3;
            }
        });
        a4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                score[0]=4;
            }
        });
        a5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                score[0]=5;
            }
        });
        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                score[1]=1;
            }
        });
        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                score[1]=2;
            }
        });
        b3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                score[1]=3;
            }
        });
        b4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                score[1]=4;
            }
        });
        b5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                score[1]=5;
            }
        });
        c1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                score[2]=1;
            }
        });
        c2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                score[2]=2;
            }
        });
        c3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                score[2]=3;
            }
        });
        c4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                score[2]=4;
            }
        });
        c5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                score[2]=5;
            }
        });
        Apply.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                context.insert();
                JOptionPane.showMessageDialog(window,"success");
            }
        });
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                for(int i = 0;i<3;i++){
                    if(score[i] == 0){
                        JOptionPane.showMessageDialog(window,"You have not scored yet, please score for us.");
                        return;
                    }
                }
                MyClient myClient = new MyClient("127.0.0.1",6666);
                try {
                    System.out.println("feedback");
                    myClient.sendRequest("feedback:"+ Arrays.toString(score)+getUsrComment()+"ehqpeui@!#!!#DQWW1"+Context.getcode()+"ehqpeui@!#!!#DQWW1"+Context.getStrComment()+"ehqpeui@!#!!#DQWW1"+Context.getCorrected_comment());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String res = null;
                try {
                    res = myClient.receive();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //feedback_id = Integer.parseInt(res);
                System.out.println("receive:"+res);
                try {
                    myClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                JOptionPane.showMessageDialog(window,"success");
            }
        });
        feedback.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Suggestions.main(null);
            }
        });
//        commentOfUser.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusGained(FocusEvent e) {
//                super.focusGained(e);
//                if(!Objects.equals(commentOfUser.getText(), "Please write your comment here.")){
//                    commentOfUser.setText("");
//                }
//            }
//            @Override
//            public void focusLost(FocusEvent e) {
//                super.focusLost(e);
//                if(!Objects.equals(commentOfUser.getText(), "")){
//                    commentOfUser.setText("Please write your comment here.");
//                }
//            }
//        });
        commentOfUser.addFocusListener(new JTextAreaHintListener(commentOfUser,"Please write your comment here."));
    }

    public String getUsrComment(){
        return commentOfUser.getText();
    }//获取用户的反馈
    public JComponent getContent() {
        return window;
    }
    public static int getID(){
        return feedback_id;
    }

}

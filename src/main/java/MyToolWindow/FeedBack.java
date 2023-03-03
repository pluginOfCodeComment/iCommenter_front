package MyToolWindow;

import UploadAndDownload.MyClient;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class FeedBack {
    private JPanel panel1;
    private JButton yesSubmitButton;
    private JTextArea pleaseAddYourFeedbackTextArea;
    private JRadioButton a1RadioButton;
    private JRadioButton a2RadioButton;
    private JRadioButton a3RadioButton;
    private JRadioButton a4RadioButton;
    private JRadioButton a5RadioButton;
    private int score;
    private String feedback;
    static JFrame frame = new JFrame("FeedBack");

    public FeedBack() {
        this.score = -1;
        this.feedback = null;
        yesSubmitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(score == 0){
                    JOptionPane.showMessageDialog(panel1,"You have not scored the plug-in yet, please score for us.");
                    return;
                }
                Object[] options ={ "OK", "Cancel" };
                int tmp = JOptionPane.showOptionDialog(null, "Are you sure to submit？", "提示",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if(tmp==0){
                    JOptionPane.showMessageDialog(panel1,"Your feedback has been submitted.");
                    feedback = getFeedback();
                    frame.dispose();
                    MyClient myClient = new MyClient("127.0.0.1",6666);
                    try {
                        myClient.sendRequest("score:"+ String.valueOf(score));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String res = null;
                    try {
                        res = myClient.receive();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("receive:"+res);
                    MyClient myClient1 = new MyClient("127.0.0.1",6666);
                    try {
                        myClient1.sendRequest("feedback:"+feedback);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String res1 = null;
                    try {
                        res1 = myClient1.receive();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("receive:"+res1);
                }
            }
        });
        a1RadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {score = 1;}
        });

        a2RadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                score = 2;
            }
        });
        a3RadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                score = 3;
            }
        });
        a4RadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                score = 4;
            }
        });
        a5RadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                score = 5;
            }
        });
    }

    public JComponent getContent() {
        return panel1;
    }

    //得到反馈
    public String getFeedback(){//feedback存在哪里，尚未处理
        return pleaseAddYourFeedbackTextArea.getText();
    }

    public static void show() {
        frame.setContentPane(new FeedBack().panel1);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(350,400);
        frame.setLocation(500,200);
        frame.setVisible(true);
    }
}

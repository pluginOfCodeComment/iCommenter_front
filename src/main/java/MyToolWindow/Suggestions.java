package MyToolWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Suggestions {
    private JPanel panel1;
    private JRadioButton a2;
    private JRadioButton a3;
    private JRadioButton a4;
    private JRadioButton a1;
    private JRadioButton b1;
    private JRadioButton b5;
    private JRadioButton b2;
    private JRadioButton b3;
    private JRadioButton b4;
    private JRadioButton c1;
    private JRadioButton c2;
    private JRadioButton c3;
    private JRadioButton c4;
    private JRadioButton c5;
    private JTextArea feedback;
    private JRadioButton a5;
    private JButton submitButton;
    private int[] scoreForPlugin = new int[3];
    static  JFrame frame = new JFrame("Feedback");


    public Suggestions() {
        a1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                scoreForPlugin[0] = 1;
            }
        });
        a2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                scoreForPlugin[0] = 2;
            }
        });
        a3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                scoreForPlugin[0] = 3;
            }
        });
        a4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                scoreForPlugin[0] = 4;
            }
        });
        a5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                scoreForPlugin[0] = 5;
            }
        });
        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                scoreForPlugin[1] = 1;
            }
        });
        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                scoreForPlugin[1] = 2;
            }
        });
        b3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                scoreForPlugin[1] = 3;
            }
        });
        b4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                scoreForPlugin[1] = 4;
            }
        });
        b5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                scoreForPlugin[1] = 5;
            }
        });
        c1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                scoreForPlugin[2] = 1;
            }
        });
        c2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                scoreForPlugin[2] = 2;
            }
        });
        c3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                scoreForPlugin[2] = 3;
            }
        });
        c4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                scoreForPlugin[2] = 4;
            }
        });
        c5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                scoreForPlugin[2] = 5;
            }
        });

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                for(int i = 0;i<3;i++){
                    if(scoreForPlugin[i] == 0){
                        JOptionPane.showMessageDialog(panel1,"You have not scored yet, please score for us.");
                        return;
                    }
                }
                frame.dispose();
                JOptionPane.showMessageDialog(panel1,"success");
            }
        });
    }

    public static void main(String[] args) {
        frame.setContentPane(new Suggestions().panel1);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(350,400);
        frame.setLocation(500,200);
        frame.setVisible(true);
    }
    public JComponent getContent() {
        return panel1;
    }

}

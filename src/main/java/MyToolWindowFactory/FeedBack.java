package MyToolWindowFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FeedBack {
    private JPanel panel1;
    private JButton yesSubmitButton;
    private JTextArea pleaseAddYourFeedbackTextArea;
//    private JTextArea pleaseAddYourFeedbackTextArea;
//    pleaseAddYourFeedbackTextArea.getText();
    public FeedBack() {
        yesSubmitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(panel1,"Your feedback has been submitted, thanks for your suggestions.");
                getFeedback();
            }
        });
    }

    public String getFeedback(){//feedback存在哪里，尚未处理
        return pleaseAddYourFeedbackTextArea.getText();
    }


    public static void show() {
        JFrame frame = new JFrame("FeedBack");
        frame.setContentPane(new FeedBack().panel1);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(300,400);
        frame.setVisible(true);
    }

}

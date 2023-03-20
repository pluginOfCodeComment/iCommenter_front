package MyToolWindow;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class JTextAreaHintListener implements FocusListener {
    private String hintText;
    private JTextArea textArea;

    public JTextAreaHintListener(JTextArea jTextArea,String hintText){
        this.textArea = jTextArea;
        this.hintText = hintText;
        jTextArea.setText(hintText);//默认显示
    }
    @Override
    public void focusGained(FocusEvent e){
        String tmp = textArea.getText();
        if(tmp.equals(hintText)){
            textArea.setText("");
        }
    }
    @Override
    public void focusLost(FocusEvent e){
        String tmp = textArea.getText();
        if(tmp.equals("")){
            textArea.setText(hintText);
        }
    }
}

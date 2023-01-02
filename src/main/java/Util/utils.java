package Util;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.editor.VisualPosition;

import java.util.Objects;

public class utils {
    public static int getTsize(String text){
        if(Objects.equals(text, "")){
            return -1;
        }
        int sum = 0;
        for(int i = 0; text.charAt(i) == 32 && i < text.length(); i++){
            sum++;
        }
        return sum / 4;
    }

    public boolean check(Document document, SelectionModel selectionModel){
        VisualPosition startPosition = selectionModel.getSelectionStartPosition();
        VisualPosition endPosition = selectionModel.getSelectionEndPosition();
        String s = selectionModel.getSelectedText();
        if(s == null || startPosition == null || endPosition == null){
            return false;
        }

        String[] text = document.getText().split("\n");
        String[] code = selectionModel.getSelectedText().split("\n");

        int start,end;
        for(start = startPosition.line; text[start].equals(""); start++);
        for(end = endPosition.line;text[end].equals("");end--);
        int beg,fin;
        for(beg = 0; code[beg].equals(""); beg++);
        for(fin = code.length - 1; code[fin].equals(""); fin--);


        //第一行一定要有def
        if(!code[beg].contains("def")){
            return false;
        }

        //最后一行不完整
        if(!code[fin].equals(text[end])){
            return false;
        }

        //通过缩进判断
        int check_line;
        for(check_line = end + 1;text[check_line].equals("") && check_line < text.length; check_line++);
        if(check_line == text.length - 1){
            return true;
        }
//        测试时用：
//        int a = getTsize(text[check_line]);
//        int b = getTsize(text[beg]);
        return getTsize(text[check_line]) == getTsize(code[beg]);
    }
}

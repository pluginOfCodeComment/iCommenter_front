package application;

import Util.utils;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.editor.VisualPosition;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.TextRange;


public class Context {
    /** 选中的代码文本 **/
    private String code;
    /** 工程对象 **/
    private Project project;
    /** 文件 **/
    private Document document;
    /** 编辑器 **/
    private Editor editor;
    /** 文件中的选中部分 **/
    private SelectionModel selectionModel;
    /** 相关数据 **/
    private int function_begin;
    private int function_end;
    private int comment_begin;
    private int comment_end;

    /** 是否已有代码注释 **/
    private boolean hasCodeComment;

    public Context(Project p,Editor e){
        project = p;
        editor = e;
        document = e.getDocument();
        selectionModel = e.getSelectionModel();
        code = null;
        function_begin = function_end = -1;
        comment_begin = comment_end = -1;
        hasCodeComment = false;
    }

    public String getFunction(){
        String function_name = null;
        VisualPosition startPosition = selectionModel.getSelectionStartPosition();
        VisualPosition endPosition = selectionModel.getSelectionEndPosition();
        int start = startPosition.line;
        int end = endPosition.line;
        boolean isContain = false;
        boolean isFind = false;
        int compare = 0;
        String[] text = document.getText().split("\n");
        for(int i = start; i <= end; i++){
            if(text[i].contains("def")){
                if(!isFind && !isContain){
                    function_begin = i;
                    isContain = true;
                    isFind = true;
                    compare = utils.getTsize(text[function_begin]);
                }else{
                    return null;
                }
            }
            if(!isContain && !text[i].equals("")){
                isContain = true;
            }
            if(isFind && i != function_begin && utils.getTsize(text[i]) == compare){
                function_end = i - 1;
                break;
            }
        }
        if(function_begin == -1){
            int i;
            for(i = start - 1; i >= 0 && !text[i].contains("def");i--);
            function_begin = i;
        }
        if(function_end == -1){
            int i = end == function_begin ? end + 1 : end;
            compare = utils.getTsize(text[function_begin]);
            for(; i < text.length && compare != utils.getTsize(text[i]);i++);
            function_end = i - 1;
        }
        String[] tmp = text[function_begin].split(" ");
        int k;
        for (k = 0; k < tmp.length && !tmp[k].equals("def"); k++) {}
        function_name = tmp[k + 1].split("\\(")[0];
        return function_name;
    }

    public boolean checkComment(){
        boolean hasCom = false;
        String[] text = document.getText().split("\n");
        int compare = utils.getTsize(text[function_begin]);
        for(int i = function_begin - 1; i >= 0; i--){
            if(text[i].contains("#")){
                if(!hasCom){
                    hasCom = true;
                    comment_begin = i;
                    comment_end = i;
                }else{
                    comment_begin = i;
                }
            }else if(!text[i].equals("") && utils.getTsize(text[i]) != compare){
                hasCodeComment = hasCom;
                return hasCom;
            }
        }
        hasCodeComment = hasCom;
        return hasCom;
    }

    public void getBody(){
        int insertOffset = document.getLineStartOffset(function_begin);
        TextRange textRange = new TextRange(insertOffset, document.getLineEndOffset(function_end));
        code = document.getText(textRange);
        //test
        System.out.println(function_begin);
        System.out.println(function_end);
        System.out.println(code);
        System.out.println(comment_begin);
        System.out.println(comment_end);
    }

    public void insert(){
        int insertOffset = document.getLineStartOffset(function_begin);
        Runnable runnable;
        int k = 0;
        if(hasCodeComment){
            k = Messages.showYesNoDialog(project,"是否要替换掉原来的注释","提示","是","否",Messages.getQuestionIcon());
            if(k == 0){
                runnable = () -> document.replaceString(document.getLineStartOffset(comment_begin),document.getLineEndOffset(comment_end),"#this is new code comment");
            }else{
                runnable = () -> document.insertString(insertOffset,"#this is code comment\n");
            }
        }else{
            runnable = () -> document.insertString(insertOffset,"#this is code comment\n");
        }
        WriteCommandAction.runWriteCommandAction(project,runnable);
    }
}

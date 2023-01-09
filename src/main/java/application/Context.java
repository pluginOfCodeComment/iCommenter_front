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

    public boolean isHasCodeComment() {
        return hasCodeComment;
    }

    public String getFunctionName(){
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
        //test
        System.out.println(function_begin);
        System.out.println(function_end);
        return function_name;
    }

    public void checkComment(){
        boolean hasCom = false;
        String[] text = document.getText().split("\n");
        int compare = utils.getTsize(text[function_begin]);
        for(int i = function_begin - 1; i >= 0; i--){
            if(text[i].equals("'''") || text[i].equals("\"\"\"") || text[i].contains("#")){
                if(!hasCom){
                    hasCom = true;
                    comment_begin = i;
                    comment_end = function_begin - 1;
                }else{
                    comment_begin = i;
                }
            }else if(!text[i].equals("") && utils.getTsize(text[i]) != compare){
                hasCodeComment = hasCom;
                return;
            }
        }
        hasCodeComment = hasCom;
        //test
        System.out.println(comment_begin);
        System.out.println(comment_end);
    }

    public void getBody(){
        int insertOffset = document.getLineStartOffset(function_begin);
        TextRange textRange = new TextRange(insertOffset, document.getLineEndOffset(function_end));
        code = document.getText(textRange);
        //test
        System.out.println(code);
    }

    public void insert(String s){
        int insertOffset = document.getLineStartOffset(function_begin);
        Runnable runnable;
        int k = 0;
        if(hasCodeComment){
            k = Messages.showYesNoDialog(project,"是否要替换掉原来的注释","提示","是","否",Messages.getQuestionIcon());
            if(k == 0){
                int firstOffset = document.getLineStartOffset(comment_begin);
                int endOffset = document.getLineEndOffset(comment_end) + 1;
                runnable = () -> document.replaceString(firstOffset,endOffset,s);
                comment_end = comment_begin;
                function_end -= function_begin - comment_begin;
                function_begin = comment_begin + 1;
            }else{
                runnable = () -> document.insertString(insertOffset,s);
                comment_end++;
                function_begin++;
                function_end++;
            }
        }else{
            runnable = () -> document.insertString(insertOffset,s);
            comment_begin = comment_end = function_begin;
            function_begin++;
            function_end++;
            hasCodeComment = true;
        }
        WriteCommandAction.runWriteCommandAction(project,runnable);
        //update();
    }

    public void update(){
        this.comment_begin = -1;
        this.comment_end = -1;
        checkComment();
    }
}

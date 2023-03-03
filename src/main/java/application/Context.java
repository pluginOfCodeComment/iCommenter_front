package application;

import UploadAndDownload.MyClient;
import Util.Symbol;
import Util.utils;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.editor.VisualPosition;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;


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
    private String name;
    private int model_id;

    //包括'''
    private int comment_begin;
    private int comment_end;//function_end - 1
    /** 缩进量 */
    private int indent;
    private int comment_format;

    /** 是否已有代码注释 **/
    private boolean hasCodeComment;

    /** 返回的代码注释 **/
    public formatComment[] comments;

    public Context(Project p,Editor e){
        project = p;
        editor = e;
        document = e.getDocument();
        selectionModel = e.getSelectionModel();

        code = null;
        function_begin = function_end = -1;
        comment_begin = comment_end = -1;
        hasCodeComment = false;
        comments = new formatComment[4];
        indent = 0;
        name = null;
        model_id = -1;
        comment_format = 0;
        project.getMessageBus().connect().subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER,
                new FileEditorManagerListener() {
                    private boolean state = true;
                    @Override
                    public void selectionChanged(@NotNull FileEditorManagerEvent event) {
                        if(!state){return;}
                        System.out.println("selection change");
                        state = false;
                        check();
                    }
                    @Override
                    public void fileClosed(@NotNull FileEditorManager source, @NotNull VirtualFile file){
                        if(!state){return;}
                        System.out.println("closed");
                        state = false;
                        check();
                    }

                });
    }

    public Project getProject() {
        return project;
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

        indent = compare;
        name = function_name;
        return function_name;
    }

    public void checkComment(){
        boolean hasCom = false;
        String[] text = document.getText().split("\n");
        int compare = utils.getTsize(text[function_begin]);
        String type = null;
        for(int i = function_begin - 1; i >= 0; i--){
//            if(!hasCom && (text[i].contains("'''") || text[i].contains("\"\"\"") || text[i].contains("#"))){
//                hasCom = true;
//                comment_begin = i;
//                comment_end = function_begin - 1;
//                type = text[i].contains("'''") ? "'''" : text[i].contains("\"\"\"") ? "\"\"\"" : "#";
//                //todo:一行注释的问题，考虑用正则表达式实现
//            }else if(hasCom && text[i].contains(type)){
//                hasCodeComment = hasCom;
//                //test
//                System.out.println("comment:" + comment_begin);
//                System.out.println(comment_end);
//                return;
//            }else if(!text[i].equals("") && utils.getTsize(text[i]) != compare){
//                hasCodeComment = hasCom;
//                //test
//                System.out.println("comment:" + comment_begin);
//                System.out.println(comment_end);
//                return;
//            }
            if(text[i].contains("'''") || text[i].contains("\"\"\"") || text[i].contains("#")){
                if(!hasCom){
                    hasCom = true;
                    comment_begin = i;
                    comment_end = function_begin - 1;
                }else{
                    comment_begin = i;
                }
            }else if(!text[i].equals("") && utils.getTsize(text[i]) != compare){
                hasCodeComment = hasCom;
                //test
                System.out.println("comment:" + comment_begin);
                System.out.println(comment_end);
                return;
            }
        }
        hasCodeComment = hasCom;
        //test
        System.out.println("comment" + comment_begin);
        System.out.println(comment_end);
    }

    public void getBody() throws IOException {
        int insertOffset = document.getLineStartOffset(function_begin);
        TextRange textRange = new TextRange(insertOffset, document.getLineEndOffset(function_end));
        code = document.getText(textRange);
        //test
        System.out.println(code);

        ProgressManager.getInstance().run(
                new Task.Modal(project, "Generating Comment", true) {
                    @Override
                    public void run(@NotNull ProgressIndicator indicator) {

                        indicator.setFraction(0.1);
                        try {
                            Thread.sleep(700);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                });

        //return comments from different models
        MyClient myClient = new MyClient("127.0.0.1",6666);
        myClient.sendRequest(code);
        String res = myClient.receive();
        System.out.println("receive_num:"+res);
        String res_comment = myClient.receive();
        System.out.println("receive:"+ Arrays.toString(res_comment.split(",,")));
        String[] receive = res_comment.split(",,");
        comments[0] = new formatComment(receive[0],0,indent);
        comments[1] = new formatComment(receive[1],1,indent);
        comments[2] = new formatComment(receive[2],2,indent);
        comments[3] = new formatComment(receive[3],3,indent);

//        comments[0] = new formatComment("this is comment 1 this is comment 1 this is comment 1 this is comment 1 this is comment 1",1,indent);
//        comments[1] = new formatComment("this is comment 2",1,indent);
//        comments[2] = new formatComment("this is comment 3 this is comment 3 this is comment 3",2,indent);
//        comments[3] = new formatComment("comment 4",3,indent);
    }

    public void insert(int index){
        String s = comments[index].getFormatComment();
        int lineSum = comments[index].getLine() + 2;
        System.out.println(lineSum);
        int insertOffset = document.getLineStartOffset(function_begin);
        Runnable runnable;
        int k = 0;
        String space = utils.getSpace(indent);
        String symbol = Symbol.get(comment_format);
        if(hasCodeComment){
            k = Messages.showYesNoDialog(project,"是否替换掉以下注释:\n" + document.getText(new TextRange(document.getLineStartOffset(comment_begin), document.getLineEndOffset(comment_end))),"提示","是","否",Messages.getQuestionIcon());
            if(k == 0){
                int firstOffset = document.getLineStartOffset(comment_begin);
                int endOffset = document.getLineEndOffset(comment_end) + 1;
                runnable = () -> document.replaceString(firstOffset,endOffset,space + "\"\"\"\n" + s + space + "\"\"\"\n");
                comment_end = comment_begin + lineSum - 1;
                int tmp = function_end - function_begin;
                function_begin = comment_begin + lineSum;
                function_end = function_begin + tmp;
            }else{
                int tmp = document.getLineStartOffset(comment_end);
                runnable = () -> document.insertString(tmp, "\n" + s);
                comment_end += lineSum - 1; //空了一行
                function_begin += lineSum - 1;
                function_end += lineSum - 1;
            }
        }else{
            runnable = () -> document.insertString(insertOffset,space + "\"\"\"\n" + s + space + "\"\"\"\n");
            comment_begin = function_begin;
            comment_end = comment_begin + lineSum - 1;
            function_begin += lineSum;
            function_end += lineSum;
            hasCodeComment = true;
        }
        WriteCommandAction.runWriteCommandAction(project,runnable);
        model_id = index;
        //test
        System.out.println("insert commment:"+comment_begin);
        System.out.println(comment_end);
    }

    private void check(){
        String[] s = document.getText().split("\n");
        for(int i = 0; i < s.length; i++){
            if(s[i].contains("def") && s[i].contains(name)){
                function_begin = i;
                this.checkComment();
                String change = document.getText(new TextRange(document.getLineStartOffset(comment_begin),document.getLineEndOffset(comment_end)));
                System.out.println(model_id + ":");
                System.out.println(change);
                return;
            }
        }
    }
}

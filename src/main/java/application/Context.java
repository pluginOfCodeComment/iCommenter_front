package application;

import UploadAndDownload.MyClient;
import Util.Tools;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
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
import com.intellij.util.messages.MessageBusConnection;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;


public class Context {
    /** 选中的代码文本 **/
    private String code;
    /** 工程对象 **/
    private Project project;
    /** 文件 **/
    private Document document;
    /** 编辑器 **/
    private Editor editor;
    /** 相关数据 **/
    private int function_begin;
    private int function_end;
    private String name;
    private int comment_begin;
    private int comment_end;//function_end - 1

    /** 缩进量 */
    private int indent;

    /** 是否已有代码注释 **/
    private boolean hasCodeComment;
    private int commentType;

    /** 返回的代码注释 **/
    public Comment comment;

    private final MessageBusConnection connection;

    private MyClient myClient;

    private static final int DocComment = 0;
    private static final int SingleLineComment = 1;
    private static final int MultiLineComment = 2;

    public Context(Project p,Editor e){
        project = p;
        editor = e;
        document = e.getDocument();

        code = null;
        function_begin = function_end = -1;
        comment_begin = comment_end = -1;
        hasCodeComment = false;
        commentType = -1;
        comment = null;
        indent = 0;
        name = null;
        connection = project.getMessageBus().connect();
        myClient = null;
        connection.subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER,
                new FileEditorManagerListener() {
                    @Override
                    public void selectionChanged(@NotNull FileEditorManagerEvent event) {
                        System.out.println("selection change");
                        try {
                            check();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                    @Override
                    public void fileClosed(@NotNull FileEditorManager source, @NotNull VirtualFile file){
                        System.out.println("closed");
                        try {
                            check();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }

                });
    }

    public boolean isHasCodeComment() {
        return hasCodeComment;
    }

    private boolean isStartOfFunction(String s){
        String BEGINNING = "def";
        return s.strip().startsWith(BEGINNING);
    }

    public String getFunctionName(){
        int compare = 0;
        int judge = -1;
        String[] text = document.getText().split("\n");
        String function_name = null;
        if(editor.getSelectionModel().getSelectionStartPosition() == null){
            return null;
        }
        int position = editor.getSelectionModel().getSelectionStartPosition().line;
        if(position >= text.length){return null;}
        if(isStartOfFunction(text[position])){
            function_begin = position;
        }
        int i;
        if(function_begin == -1){
            for(i = position; i >= 0 && !isStartOfFunction(text[i]);i--){}
            if(i == -1){return null;}
            function_begin = function_end = i;
        }

        compare = Tools.getSpaceSize(text[function_begin]);
        for(i = function_begin + 1; i < text.length && compare != Tools.getSpaceSize(text[i]); i++);
        function_end = i - 1;

        String[] tmp = text[function_begin].split(" ");
        for (i = 0; i < tmp.length && !tmp[i].equals("def"); i++) {}
        function_name = tmp[i + 1].split("\\(")[0];
        //test
        System.out.println("function: " + function_begin + " " + function_end);

        name = text[function_begin];
        indent = compare;
        return function_name;
    }

    private boolean isCommentLine(String s){
        String check_s = s.strip();
        if(check_s.startsWith("\"\"\"")){
            this.commentType = DocComment;
            return true;
        }else if(check_s.startsWith("#")){
            this.commentType = SingleLineComment;
            return true;
        }else if(check_s.startsWith("'''")){
            this.commentType = MultiLineComment;
            return true;
        }
        return false;
    }

    public void checkComment(){
        String[] text = document.getText().split("\n");
        if(!isCommentLine(text[function_begin + 1])){
            hasCodeComment = false;
            return;
        }
        hasCodeComment = true;
        comment_begin = function_begin + 1;
        comment_end = comment_begin;
        for(int i = comment_begin + 1; i <= function_end; i++){
            if(text[i].equals("")){
                continue;
            }
            if(commentType == 1){
                if(!text[i].contains("#")){
                    return;
                }
                comment_end = i;
            }else if(text[i].contains("'''") | text[i].contains("\"\"\"")){
                comment_end = i;
                return;
            }
        }
        return;
        //查找函数体前的
//        boolean special = false;
//        for(int i = function_begin - 1; i >= 0; i--){
//            if(text[i].contains("'''") || text[i].contains("\"\"\"") || text[i].contains("#")){
//                if(!hasCodeComment){
//                    hasCodeComment = true;
//                    comment_begin = i;
//                    comment_end = function_begin - 1;
//                    if(text[i].contains("#")){
//                        special = true;
//                    }else if(text[i].matches("\"\"\"(.*?)\"\"\"")){
//                        return;
//                    }else if(text[i].matches("'''(.*?)'''")){
//                        return;
//                    }
//                }else{
//                    comment_begin = i;
//                    if(!special){return;}
//                }
//            }else if(!hasCodeComment || special){
//                return;
//            }
//        }

    }

    public void getBody(){
        TextRange textRange = new TextRange(document.getLineStartOffset(function_begin), document.getLineEndOffset(function_end));
        code = document.getText(textRange);
        //test
        System.out.println(code);
        System.out.println("comment: " + comment_begin + " " + comment_end);
    }

    /*
    return comments from different models
    */
    public void transfer() throws IOException {
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

        myClient = new MyClient("127.0.0.1",6666);
        myClient.sendRequest(code);
        myClient.receive();
        String res_comment = myClient.receive();
        comment = new Comment(res_comment,indent + 4);
    }

    public void insert(){
        String s = comment.getFormatComment();
        int lineSum = comment.getLine();
        Runnable runnable = null;
        String space = Tools.getSpace(indent + 4);

        if(hasCodeComment){
            int firstOffset = document.getLineStartOffset(comment_begin);
            int endOffset = document.getLineStartOffset(comment_end + 1);
            String before = document.getText(new TextRange(firstOffset,endOffset - 1));
            int judge = Messages.showYesNoCancelDialog(project,"已存在注释:\n" + before + "\n请决定您的插入方式","提示","完全替换","直接插入","撤销操作",Messages.getQuestionIcon());
            if(judge == Messages.CANCEL){
                return;
            }else if(judge == Messages.YES){
                runnable = () -> document.replaceString(firstOffset,endOffset,space + "\"\"\"\n" + s + space + "\"\"\"\n");
                int tmp = comment_end;
                comment_end = comment_begin + lineSum + 1;
                tmp = comment_end - tmp;
                function_end += tmp;
            }else{
                int add = lineSum + 1; //insert an empty line
                if(commentType == SingleLineComment){
                    StringBuffer sb = new StringBuffer(space + "\"\"\"\n");
                    String[] old = before.split("\n");
                    for(int i = 0; i < old.length; i++){
                        //sb.append(space + old[i].substring(indent + 1) + "\n");
                        sb.append(space + old[i].substring(indent + 5).strip() + "\n");
                    }
                    sb.append("\n" + s + space + "\"\"\"\n");
                    runnable = () -> document.replaceString(firstOffset,endOffset,sb);
                    add += 2; //""" and """
                    comment_end += add;
                    function_end += add;
                }else if(commentType == MultiLineComment){
                    String toreplace = "\"\"\"";
                    boolean flag = false;
                    if(!before.startsWith(space + "'''\n")){
                        System.out.println("begin need");
                        toreplace += "\n" + space;
                        add++;
                    }
                    if(!before.endsWith("\n" + space + "'''")){
                        System.out.println("end need");
                        flag = true;
                        add++;
                    }
                    String after = before.substring(0,before.length() - 3).replace("'''",toreplace) + (flag ? "\n" : "") +"\n" + s + space + "\"\"\"\n";
                    runnable = () -> document.replaceString(firstOffset,endOffset,after);
                    comment_end += add;
                    function_end += add;
                }else{
                    boolean flag = !before.endsWith("\n" + space + "\"\"\"");

                    if(flag){add++;System.out.println("end need");}
                    if(!before.startsWith(space + "\"\"\"\n")){
                        System.out.println("begin need");
                        String after = before.substring(0,before.length() - 3).replace("\"\"\"","\"\"\"\n" + space) +
                                (flag ? "\n" : "") + "\n" + s + space + "\"\"\"\n";
                        runnable = () -> document.replaceString(firstOffset,endOffset,after);
                        add++;
                    }else{
                        int tmp = document.getLineEndOffset(comment_end) - 3;
                        runnable = () -> document.insertString(tmp, "\n" + (flag ? "\n" : "") + s + space);
                    }
                    comment_end += add; //空了一行
                    function_end += add;
                }
            }
        }else{
            int insertOffset = document.getLineEndOffset(function_begin);
            runnable = () -> document.insertString(insertOffset,"\n" + space + "\"\"\"\n" + s + space + "\"\"\"");
            comment_begin = function_begin + 1;
            comment_end = comment_begin + lineSum + 1;
            function_end += lineSum + 2;
            hasCodeComment = true;
        }
        commentType = DocComment;
        WriteCommandAction.runWriteCommandAction(project,runnable);
        //test
        System.out.println("insert commment: "+comment_begin + " " + comment_end);
        System.out.println("function: " + function_begin + " " + function_end);
    }

    private void check() throws IOException {
        String[] s = document.getText().split("\n");
        for(int i = 0; i < s.length; i++){
            if(Objects.equals(s[i], "")){
                continue;
            }
            if(s[i].contains(name)){
                hasCodeComment = false;
                function_begin = i;
                this.checkComment();
                String change = document.getText(new TextRange(document.getLineStartOffset(comment_begin),document.getLineEndOffset(comment_end)));
                System.out.println("change :");
                System.out.println(change);
                connection.disconnect();
                myClient.close();
            }
        }
    }

    public Comment getComment() {
        return comment;
    }
}

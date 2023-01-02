package action;


import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.ui.Messages;
import application.GenerateComImpl;
import Util.utils;
import com.intellij.openapi.util.TextRange;


/**
 * 待实现：
 * 1.状态栏进度条
 */
public class generateCommentAction extends AnAction {

    //private GenerateComImpl generateCom = new GenerateComImpl();
    private int function_begin = -1;
    private int function_end = -1;
    private int comment_begin = -1;
    private int comment_end = -1;

    private String getFunction(Document document,SelectionModel selectionModel){
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

    public boolean checkComment(Document document){
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
                return hasCom;
            }
        }
        return hasCom;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        //获取当前编辑器对象
        Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        //获取选择的数据模型
        SelectionModel selectionModel = editor.getSelectionModel();
        Document document = editor.getDocument();
        String function = getFunction(document,selectionModel);
        if(function == null){
            Messages.showErrorDialog(e.getProject(), "您的输入中包含了两个函数体，请重新输入","错误提示");
            return;
        }
        boolean hasComment = checkComment(document);
        int judge;
        if(hasComment){
            judge = Messages.showYesNoDialog(e.getProject(),"函数\"" + function + "\"已有注释，是否需要为其重新生成注释","提示","是","否",Messages.getQuestionIcon());
        }else{
            judge = Messages.showYesNoDialog(e.getProject(),"您是否要为函数\""+ function + "\"生成注释","提示","是","否",Messages.getQuestionIcon());
        }
        if(judge == 1){
            return;
        }

        int insertOffset = document.getLineStartOffset(function_begin);
        TextRange textRange = new TextRange(insertOffset, document.getLineEndOffset(function_end));
        String code = document.getText(textRange);
        System.out.println(function_begin);
        System.out.println(function_end);
        System.out.println(code);
        System.out.println(comment_begin);
        System.out.println(comment_end);
        Runnable runnable;
        if(hasComment){
            judge = Messages.showYesNoDialog(e.getProject(),"是否要替换掉原来的注释","提示","是","否",Messages.getQuestionIcon());
            if(judge == 0){
                runnable = () -> document.replaceString(document.getLineStartOffset(comment_begin),document.getLineEndOffset(comment_end),"#this is new code comment");
            }else{
                runnable = () -> document.insertString(insertOffset,"#this is code comment\n");
            }
        }else{
            runnable = () -> document.insertString(insertOffset,"#this is code comment\n");
        }
        //runnable = () -> document.insertString(insertOffset,"#this is code comment\n");
        WriteCommandAction.runWriteCommandAction(e.getProject(),runnable);
        clear();

//        try {
//            generateCom.doGenerate(e.getProject(),e.getDataContext(),e.getData(PlatformDataKeys.PSI_FILE));
//        } catch (Exception ex) {
//            Messages.showErrorDialog(e.getProject(), "您的输入不合法，请选择完整的函数体", "错误提示");
//        }


    }

    public void clear(){
        this.function_begin = -1;
        this.function_end = -1;
        this.comment_begin = -1;
        this.comment_end = -1;
    }
}

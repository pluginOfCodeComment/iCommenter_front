package action;


import application.Context;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import MyToolWindowFactory.MyToolWindow;


public class generateCommentAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        //获取当前编辑器对象
        Project project = e.getProject();
        Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        Context context = new Context(project,editor);
        String function = context.getFunctionName();
        if(function == null){
            Messages.showErrorDialog(e.getProject(), "您的输入中包含了两个函数体，请重新输入","错误提示");
            return;
        }
        context.checkComment();
        int judge;
        if(context.isHasCodeComment()){
            judge = Messages.showYesNoDialog(e.getProject(),"函数\"" + function + "\"已有注释，是否需要为其重新生成注释","提示","是","否",Messages.getQuestionIcon());
        }else{
            judge = Messages.showYesNoDialog(e.getProject(),"您是否要为函数\""+ function + "\"生成注释","提示","是","否",Messages.getQuestionIcon());
        }
        if(judge == 1){
            return;
        }
        context.getBody();
        //context.insert();
        MyToolWindow myToolWindow = new MyToolWindow(context);
        myToolWindow.show();

    }


}

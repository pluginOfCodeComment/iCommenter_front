package action;

import MyToolWindow.MyToolWindowSubmit;
import application.Context;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;

import java.io.IOException;

import static MyToolWindow.Icons.LOGO;

public class GenerateAction extends AnAction {

    private Context context;
    private static final String ToolWindowName = "generate comments";
    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        //获取当前编辑器对象
        Project project = e.getProject();
        if (project == null)
        {
            return;
        }
        Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        if(editor == null){
            return;
        }
        context = new Context(project,editor);
        String function = context.getFunctionName();
        if(function == null){
            Messages.showErrorDialog(e.getProject(), "没有找到对应的函数，请重新定位","错误提示");
            return;
        }
        context.checkComment();
        String request;
        if(context.isHasCodeComment()){
            request = "函数\"" + function + "\"已有注释，是否需要为其重新生成注释？";
        }else{
            request = "您是否要为函数\""+ function + "\"生成注释";
        }
        if(Messages.showYesNoDialog(project,request,"提示","是","否",Messages.getQuestionIcon()) == Messages.NO){
            return;
        }

        try {
            context.getBody();
            context.transfer();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        //侧边窗口
        ToolWindowManager toolWindowMgr = ToolWindowManager.getInstance(project);
        ToolWindow tw = toolWindowMgr.getToolWindow(ToolWindowName);
        if (tw == null)
        {
            tw = toolWindowMgr.registerToolWindow(ToolWindowName, true, ToolWindowAnchor.RIGHT, true);
        }
        final ToolWindow toolWindow = tw;
        toolWindow.activate(() -> updateContent(toolWindow, project.getName()), true);
    }

    private void updateContent(ToolWindow toolWindow, String projectName)
    {
        MyToolWindowSubmit myToolWindowSubmit = new MyToolWindowSubmit(context);
        myToolWindowSubmit.setComment(context.getComment());
        toolWindow.setIcon(LOGO);
        ContentManager contentManager = toolWindow.getContentManager();
        contentManager.removeAllContents(true);
        Content content = contentManager.getFactory().createContent(myToolWindowSubmit.getContent(), "Comments", false);
        contentManager.addContent(content);
        content.setDescription("Description");
    }
}

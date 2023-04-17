import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.event.CaretEvent;
import com.intellij.openapi.editor.event.CaretListener;
import com.intellij.openapi.editor.event.EditorEventListener;
import com.intellij.openapi.editor.event.SelectionEvent;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.ui.awt.RelativePoint;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class HelloWorldAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {

        Project project = e.getData(PlatformDataKeys.PROJECT);
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        Document document = editor.getDocument();
        SelectionModel selectionModel = editor.getSelectionModel();


        //获取当前操作的类文件
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        //获取当前类文件的路径
        String classPath = psiFile.getVirtualFile().getPath();
        String title = "Hello World!";
        // TODO: insert action logic here

        PsiFile file = e.getData(PlatformDataKeys.PSI_FILE);
        for (PsiElement psiElement : file.getChildren()) {
            System.out.println(psiElement);
            System.out.println(psiElement.getParent() + " " + document.getLineNumber(psiElement.getStartOffsetInParent()));
        }

//        ProgressManager.getInstance().run(
//
//                new Task.Modal(project, "Generating Comment", true) {
//                    @Override
//                    public void run(@NotNull ProgressIndicator indicator) {
//                        indicator.setIndeterminate(false);
////                        indicator.setFraction(0.1);
//                        try {
//                            Thread.sleep(7000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        // 关闭进度条
//                        indicator.setIndeterminate(false);
//                        // 设置进度条为百分百
//                        indicator.setFraction(1.0);
//                        //
//                        indicator.setText("插件执行结束");
//                        // 在状态栏提示信息【代码生成完成】
//                        StatusBar statusBar = WindowManager.getInstance().getStatusBar(project);
//                        JBPopupFactory.getInstance()
//                                .createHtmlTextBalloonBuilder("插件执行结束", MessageType.INFO, null)
//                                .setFadeoutTime(4000)
//                                .createBalloon()
//                                .show(RelativePoint.getCenterOf(statusBar.getComponent()), Balloon.Position.atRight);
//
//                        if(indicator.isCanceled()){
//                            SwingUtilities.invokeLater(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Messages.showErrorDialog(project,"连接失败，请重新操作","错误");
//                                }
//                            });
//                        }
//                    }
//                });
    }
}

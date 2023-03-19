import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.CaretActionListener;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.editor.event.CaretEvent;
import com.intellij.openapi.editor.event.CaretListener;
import com.intellij.openapi.editor.event.EditorEventListener;
import com.intellij.openapi.editor.event.SelectionEvent;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

public class HelloWorldAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {

        Project project = e.getData(PlatformDataKeys.PROJECT);
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        SelectionModel selectionModel = editor.getSelectionModel();


        //获取当前操作的类文件
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        //获取当前类文件的路径
        String classPath = psiFile.getVirtualFile().getPath();
        String title = "Hello World!";

        //Messages.showMessageDialog(project, classPath, title, Messages.getInformationIcon());
        int i = Messages.showYesNoCancelDialog(project,title,"test","1","0","cancel",Messages.getQuestionIcon());
        System.out.println(i);
        i = Messages.showYesNoDialog(project,title,"test","yes","no",Messages.getInformationIcon());
        // TODO: insert action logic here

    }
}

package application;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

public abstract class AbstractGenerateCom {


    public void doGenerate(Project project, DataContext dataContext, PsiFile psiFile) throws Exception {
        //1.获取上下文
        GenerateContext generateContext = this.getGenerateContext(project,dataContext,psiFile);

    }

    protected abstract GenerateContext getGenerateContext(Project project, DataContext dataContext, PsiFile psiFile) throws Exception;

}

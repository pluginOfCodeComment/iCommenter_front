package application;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.editor.VisualPosition;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import Util.utils;

public class GenerateComImpl extends AbstractGenerateCom {
    private utils tool = new utils();
    @Override
    protected GenerateContext getGenerateContext(Project project, DataContext dataContext, PsiFile psiFile) throws Exception {
        //获得基础信息
        Editor editor = dataContext.getData(PlatformDataKeys.EDITOR);
        assert editor != null;
        Document document = editor.getDocument();

        SelectionModel selectionModel = editor.getSelectionModel();
        String selectedText = selectionModel.getSelectedText();

        //利用缩进处理格式不正确的问题
        String[] text = document.getText().split("\n");
        if(!tool.check(document,selectionModel)){
            throw new Exception();
        }

        GenerateContext generateContext = new GenerateContext();
        //封装对象
        generateContext.setProject(project);
        generateContext.setPsiFile(psiFile);
        generateContext.setDataContext(dataContext);
        generateContext.setEditor(editor);
        generateContext.setDocument(document);
        generateContext.setContext(selectedText);

        System.out.println(selectedText);

        return generateContext;
    }
}

package org.ago.goan.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.project.Project;
import org.ago.goan.anno.impl.GoGenerator;
import org.ago.goan.anno.impl.funcAnno.funcAnnotatorImpl;
import org.ago.goan.anno.Annotator;
import org.ago.goan.anno.impl.typeAnno.typeAnnotatorImpl;
import org.ago.goan.anno.impl.varAnno.varAnnotatorImpl;
import org.ago.goan.utils.StringUtils;

/**
 * generate go comment template
 */
public class GoAnAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        Project project = e.getData(PlatformDataKeys.PROJECT);
        if (null == project) {
            return;
        }

        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        if (null == editor) {
            return;
        }

        CaretModel caretModel = editor.getCaretModel();
        if (null == caretModel) {
            return;
        }

        Document document = editor.getDocument();
        if (null == document) {
            return;
        }

        String code = document.getText();
        GoGenerator go = new GoGenerator();
        go.addAnnotatorList(new funcAnnotatorImpl());
        go.addAnnotatorList(new typeAnnotatorImpl());
        go.addAnnotatorList(new varAnnotatorImpl());

        Annotator annotator = go;

        annotator.generate(project, editor, caretModel, code);

        return;
    }
}

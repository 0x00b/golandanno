package org.ago.goan.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.project.Project;
import org.ago.goan.anno.Template;
import org.ago.goan.anno.Context;
import org.ago.goan.anno.impl.GoGenerator;
import org.ago.goan.anno.impl.funcAnno.funcAnnotatorImpl;
import org.ago.goan.anno.Annotator;
import org.ago.goan.anno.impl.typeAnno.typeAnnotatorImpl;
import org.ago.goan.anno.impl.varAnno.varAnnotatorImpl;
import org.ago.goan.utils.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

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

        String content = document.getText();
        GoGenerator go = new GoGenerator();
        go.addAnnotatorList(new funcAnnotatorImpl());
        go.addAnnotatorList(new typeAnnotatorImpl());
        go.addAnnotatorList(new varAnnotatorImpl());

        Annotator annotator = go;

        Context context = new Context();
        context.project = project;
        context.caretModel = caretModel;
        context.editor = editor;
        context.document = document;
        context.content = content;

        context.selectStart = editor.getSelectionModel().getSelectionStart();
        context.selectEnd = editor.getSelectionModel().getSelectionEnd();
        context.selectCode = content.substring(context.selectStart, context.selectEnd);

        context.template = new Template();
        context.template.date = new Date().toString();
        try {
            Process p = Runtime.getRuntime().exec("git config user.name");
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";
            if ((line = reader.readLine()) != null) {
                context.template.gitName = line;
            }
        } catch (IOException err) {
            err.printStackTrace();
        }

        System.out.println(context.template.date);
        System.out.println(context.template.gitName);


        context.code = StringUtils.findCodeStart(context);
        if (!context.code.find){
            return;
        }

        annotator.generate(context);

        return;
    }
}

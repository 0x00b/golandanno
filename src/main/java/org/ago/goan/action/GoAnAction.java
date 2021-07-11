package org.ago.goan.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.project.Project;
import org.ago.goan.anno.CommonTemplate;
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

        context.SelectStart = editor.getSelectionModel().getSelectionStart();
        context.SelectEnd = editor.getSelectionModel().getSelectionEnd();
        context.SelectCode = content.substring(context.SelectStart, context.SelectEnd);

        context.commonTemplate = new CommonTemplate();
        context.commonTemplate.date = new Date().toString();
        try {
            Process p = Runtime.getRuntime().exec("git config user.name");
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";
            if ((line = reader.readLine()) != null) {
                context.commonTemplate.gitName = line;
            }
        } catch (IOException err) {
            err.printStackTrace();
        }

        System.out.println(context.commonTemplate.date);
        System.out.println(context.commonTemplate.gitName);


        context.code = StringUtils.findCodeStart(context);

        annotator.generate(context);

        return;
    }
}

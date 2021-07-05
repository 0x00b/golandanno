package org.ago.goan.anno.impl;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.project.Project;
import org.ago.goan.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class GoGenerator implements org.ago.goan.anno.Annotator {

    List<GoAnnotator> annotatorList;

    public GoGenerator() {
        super();
        annotatorList = new ArrayList<GoAnnotator>();
    }

    @Override
    public void generate(Project project, Editor editor, CaretModel caretModel, String code) {

        String annotation = null;
        DetectResult res = null;
        GoAnnotator anno = null;

        int start = editor.getSelectionModel().getSelectionStart();
        int end = editor.getSelectionModel().getSelectionEnd();


        for (int i = 0; i < annotatorList.size(); i++) {
            anno = annotatorList.get(i);
            res = anno.detect(code, caretModel);
            if (null != res) {
                annotation = anno.generate(res, code.substring(start, end));
                break;
            }
        }

        if (null == res || StringUtils.isBlank(annotation)) {
            return;
        }

        insertAnnotation(project, editor, caretModel, code, res, annotation);

        return;
    }


    void insertAnnotation(Project project, Editor editor, CaretModel caretModel, String code, DetectResult result, String annotation) {

        // find first func
        Document document = editor.getDocument();
        if (null == document) {
            return;
        }

        int start = editor.getSelectionModel().getSelectionStart();
        int end = editor.getSelectionModel().getSelectionEnd();

        if (start < end) {
            int upLineStart = document.getLineStartOffset(caretModel.getLogicalPosition().line - 1);
            int upLineEnd = document.getLineEndOffset(caretModel.getLogicalPosition().line - 1);
            String upCode = code.substring(upLineStart, upLineEnd);
            if (!StringUtils.isBlank(upCode)) {
                annotation = "\n" + annotation;
            }
            final String finalAnnotation = annotation;
            WriteCommandAction.runWriteCommandAction(project, () -> document.replaceString(start, end, finalAnnotation));
            return;
        }

        int upLineStart = document.getLineStartOffset(result.getStartLine() - 1);
        int upLineEnd = document.getLineEndOffset(result.getStartLine() - 1);
        String upCode = code.substring(upLineStart, upLineEnd);
        if (!StringUtils.isBlank(upCode)) {
            annotation = "\n\n" + annotation;
        } else {
            upLineStart = document.getLineStartOffset(result.getStartLine() - 2);
            upLineEnd = document.getLineEndOffset(result.getStartLine() - 2);
            upCode = code.substring(upLineStart, upLineEnd);
            if (!StringUtils.isBlank(upCode)) {
                annotation = "\n" + annotation;
            }
        }

        final String finalAnnotation = annotation;
        int offset = document.getLineEndOffset(result.getStartLine() - 1);
        WriteCommandAction.runWriteCommandAction(project, () -> document.insertString(offset, finalAnnotation));
    }


    public void addAnnotatorList(GoAnnotator annotator) {
        this.annotatorList.add(annotator);
    }

}

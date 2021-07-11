package org.ago.goan.anno.impl;

import com.intellij.openapi.command.WriteCommandAction;
import org.ago.goan.anno.Context;
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
    public void generate(Context ctx) {

        String annotation = null;
        DetectResult res = null;
        GoAnnotator anno = null;

        for (int i = 0; i < annotatorList.size(); i++) {
            anno = annotatorList.get(i);
            res = anno.detect(ctx);
            if (null != res) {
                annotation = anno.generate(ctx, res, ctx.SelectCode);
                break;
            }
        }

        if (null == res || StringUtils.isBlank(annotation)) {
            return;
        }

        insertAnnotation(ctx, res, annotation);

        return;
    }


    void insertAnnotation(Context ctx, DetectResult result, String annotation) {
 

        if (ctx.SelectStart < ctx.SelectEnd) {
            int upLineStart = ctx.document.getLineStartOffset(ctx.caretModel.getLogicalPosition().line - 1);
            int upLineEnd = ctx.document.getLineEndOffset(ctx.caretModel.getLogicalPosition().line - 1);
            String upCode = ctx.content.substring(upLineStart, upLineEnd);
            if (!StringUtils.isBlank(upCode)) {
                annotation = "\n" + annotation;
            }
            final String finalAnnotation = annotation;
            WriteCommandAction.runWriteCommandAction(ctx.project, () -> ctx.document.replaceString(ctx.SelectStart , ctx.SelectEnd, finalAnnotation));
            return;
        }

        int upLineStart = ctx.document.getLineStartOffset(result.getStartLine() - 1);
        int upLineEnd = ctx.document.getLineEndOffset(result.getStartLine() - 1);
        String upCode = ctx.content.substring(upLineStart, upLineEnd);
        if (!StringUtils.isBlank(upCode)) {
            annotation = "\n\n" + annotation;
        } else {
            upLineStart = ctx.document.getLineStartOffset(result.getStartLine() - 2);
            upLineEnd = ctx.document.getLineEndOffset(result.getStartLine() - 2);
            upCode = ctx.content.substring(upLineStart, upLineEnd);
            if (!StringUtils.isBlank(upCode)) {
                annotation = "\n" + annotation;
            }
        }

        final String finalAnnotation = annotation;
        int offset = ctx.document.getLineEndOffset(result.getStartLine() - 1);
        WriteCommandAction.runWriteCommandAction(ctx.project, () -> ctx.document.insertString(offset, finalAnnotation));
    }


    public void addAnnotatorList(GoAnnotator annotator) {
        this.annotatorList.add(annotator);
    }

}

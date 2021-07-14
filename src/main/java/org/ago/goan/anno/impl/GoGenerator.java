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
                annotation = anno.generate(ctx, res);
                break;
            }
        }

        if (null == res || StringUtils.isBlank(annotation)) {
            return;
        }

        replaceAnnotation(ctx, res, annotation);

        return;
    }


    void replaceAnnotation(Context ctx, DetectResult result, String annotation) {


        if (ctx.code.annotation.find) {
            int upLineStart = ctx.document.getLineStartOffset(ctx.code.annotation.startLine - 1);
            int upLineEnd = ctx.document.getLineEndOffset(ctx.code.annotation.startLine - 1);
            String upCode = ctx.content.substring(upLineStart, upLineEnd);
            if (!StringUtils.isBlank(upCode)) {
                annotation = "\n" + annotation;
            }
            final String finalAnnotation = annotation;

            int start = ctx.document.getLineStartOffset(ctx.code.annotation.startLine);
            int end = ctx.document.getLineEndOffset(ctx.code.annotation.endLine);
            WriteCommandAction.runWriteCommandAction(ctx.project, () -> ctx.document.replaceString(start, end, finalAnnotation));
            return;
        }

        int upLineStart = ctx.document.getLineStartOffset(result.startLine - 1);
        int upLineEnd = ctx.document.getLineEndOffset(result.startLine - 1);
        String upCode = ctx.content.substring(upLineStart, upLineEnd);
        if (!StringUtils.isBlank(upCode)) {
            annotation = "\n\n" + annotation;
        } else {
            int upTwoLineStart = ctx.document.getLineStartOffset(result.startLine - 2);
            int upTwoLineEnd = ctx.document.getLineEndOffset(result.startLine - 2);
            upCode = ctx.content.substring(upTwoLineStart, upTwoLineEnd);
            if (!StringUtils.isBlank(upCode)) {
                annotation = "\n" + annotation;
            } else {
                WriteCommandAction.runWriteCommandAction(ctx.project, () -> ctx.document.replaceString(upLineStart, upLineEnd, ""));
            }
        }

        final String finalAnnotation = annotation;
        int offset = ctx.document.getLineEndOffset(result.startLine - 1);
        WriteCommandAction.runWriteCommandAction(ctx.project, () -> ctx.document.insertString(offset, finalAnnotation));
    }


    public void addAnnotatorList(GoAnnotator annotator) {
        this.annotatorList.add(annotator);
    }

}

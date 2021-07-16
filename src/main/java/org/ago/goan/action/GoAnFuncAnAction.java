package org.ago.goan.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.ago.goan.anno.Annotator;
import org.ago.goan.anno.Context;
import org.ago.goan.anno.impl.GoGenerator;
import org.ago.goan.anno.impl.funcAnno.funcAnnotatorImpl;
import org.ago.goan.anno.impl.packageAnno.packageAnnotatorImpl;
import org.ago.goan.anno.impl.typeAnno.typeAnnotatorImpl;
import org.ago.goan.anno.impl.varAnno.varAnnotatorImpl;
import org.ago.goan.utils.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

/**
 * 导出函数和interface自动注释
 */
public class GoAnFuncAnAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        Project project = e.getData(PlatformDataKeys.PROJECT);
        if (null == project) {
            return;
        }

        String path = project.getBasePath();

        System.out.println(path);

        return;
    }
}

package org.ago.goan.anno;

import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import org.ago.goan.utils.FindCodeResult;

public class Context {
    public Project project;
    public Editor editor;
    public CaretModel caretModel;
    public Document document;

    public String content;

    public FindCodeResult code;

    public int selectStart;
    public int selectEnd;
    public String selectCode;

    public Template template;
}

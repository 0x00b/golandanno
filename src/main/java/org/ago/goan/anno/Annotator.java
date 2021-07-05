package org.ago.goan.anno;

import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;

public interface Annotator {

    void generate(Project project , Editor editor, CaretModel caretModel, String code );
}

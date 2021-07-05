package org.ago.goan.anno.impl;

import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.project.Project;

public interface GoAnnotator {
    DetectResult detect(String code, CaretModel caretModel);

    String generate(DetectResult code, String selectedCode);
}

package org.ago.goan.anno.impl;

import com.intellij.openapi.editor.CaretModel;

public class DetectResult {
    //是否命中类型
    String code;
    //code的起始行
    int StartLine;
    //光标
    CaretModel caretModel;

    Object Result;

    String linePrefix;

    public String getLinePrefix() {
        return linePrefix;
    }

    public void setLinePrefix(String linePrefix) {
        this.linePrefix = linePrefix;
    }


    public int getStartLine() {
        return StartLine;
    }

    public void setStartLine(int startLine) {
        StartLine = startLine;
    }

    public CaretModel getCaretModel() {
        return caretModel;
    }

    public void setCaretModel(CaretModel caretModel) {
        this.caretModel = caretModel;
    }


    public Object getResult() {
        return Result;
    }

    public void setResult(Object result) {
        Result = result;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


}
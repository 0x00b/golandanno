package org.ago.goan.anno.impl;


public class DetectResult {
    //是否命中类型
    String code;
    //code的起始行
    int StartLine;

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
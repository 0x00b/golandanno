package org.ago.goan.utils;


public class FindCodeResult {
    public String code;
    public int startLine;

    static public class Annotation {
        public String annotation;
        public int startLine;
        public int endLine;
        public boolean find;
        // 0: //  1: /**/
        public int type;
    }

    public boolean find;

    public Annotation annotation;
}
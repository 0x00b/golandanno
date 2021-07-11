package org.ago.goan.utils;

import com.intellij.openapi.editor.CaretModel;
import org.ago.goan.anno.Context;

public class StringUtils {

    public static String trim(String str) {
        return str == null ? null : str.trim();
    }

    public static boolean isBlank(String str) {
        int strLen;
        if (str != null && (strLen = str.length()) != 0) {
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }

    public static FindCodeResult findCodeStart(Context ctx) {
        FindCodeResult res = new FindCodeResult();

        int lineNumber = ctx.caretModel.getLogicalPosition().line;

        int start = ctx.document.getLineStartOffset(lineNumber);

        String[] lines = ctx.content.split("\n");

        String down = ctx.content.substring(start);
        down = StringUtils.trim(down);
        int anStart = lineNumber;

        if (down.startsWith("/*")) {
            int aEnd = down.indexOf("*/");
            if (aEnd == -1) {
                res.find = false;
                return res;
            }
            //find
        }

        int aEnd = down.indexOf("*/");

        if (aEnd == -1) {

        } else {
            int tStart = down.indexOf("/*");
            if (tStart > aEnd) {
                //当前行是/**/的中间,则注释是 tStart -> aEnd
                tStart = ctx.content.substring(0, start).lastIndexOf("/*");
                if (tStart != -1) {
                    //find

                }

            }
        }

        //这里假设注释风格不会混用，只会单纯出现 // 或者 /**/
        if (down.startsWith("//")) {
            //往下找到结束的行
            for (int i = lineNumber; i < lines.length; i++) {
                String line = StringUtils.trim(lines[i]);
                if (StringUtils.isBlank(line)) {
                    //如果有空行，按照go注释标准，这个注释不是某代码块的注释
                    res.find = false;
                    return res;
                }
                if (line.startsWith("//")) {
                    start += 1;
                    continue;
                }
                break;
            }
            // 往上找到开始的行
            for (int i = lineNumber; i >= 0; i--) {
                String line = StringUtils.trim(lines[i]);
                if (StringUtils.isBlank(line)) {
                    break;
                }
                if (line.startsWith("//")) {
                    anStart -= 1;
                    continue;
                }
                break;
            }
        }

        res.find = true;
        return res;
    }

//    public static FindCodeResult findCodeStart(Context ctx) {
//        int lineStart = ctx.caretModel.getVisualLineStart();
//
//        String funcCode = ctx.content.substring(lineStart);
//        String[] lines = funcCode.split("\n");
//
//        int startLine = ctx.caretModel.getLogicalPosition().line;
//
//        int offset = 0;
//        for (int i = 0; i < lines.length; i++) {
//            String line = StringUtils.trim(lines[i]);
//            if (StringUtils.isBlank(line) || line.startsWith("//") || line.startsWith("*") || line.startsWith("/*")) {
//                offset += lines[i].length() + 1;
//                startLine += 1;
//                continue;
//            }
//            break;
//        }
//        funcCode = funcCode.substring(offset);
//
//        FindCodeResult res = new FindCodeResult();
//        res.Code = funcCode;
//        res.StartLine = startLine;
//
//        return res;
//    }
}

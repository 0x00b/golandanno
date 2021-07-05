package org.ago.goan.utils;

import com.intellij.openapi.editor.CaretModel;

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

    public static FindCodeResult findCodeStart(String code, CaretModel caretModel) {
        int lineStart = caretModel.getVisualLineStart();

        String funcCode = code.substring(lineStart);
        String[] lines = funcCode.split("\n");

        int startLine = caretModel.getLogicalPosition().line;

        int offset = 0;
        for (int i = 0; i < lines.length; i++) {
            String line = StringUtils.trim(lines[i]);
            if (StringUtils.isBlank(line) || line.startsWith("//") || line.startsWith("*") || line.startsWith("/*")) {
                offset += lines[i].length() + 1;
                startLine += 1;
                continue;
            }
            break;
        }
        funcCode = funcCode.substring(offset);

        FindCodeResult res = new FindCodeResult();
        res.Code = funcCode;
        res.StartLine = startLine;

        return res;
    }
}

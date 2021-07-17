package org.ago.goan.utils;

import org.ago.goan.anno.Context;

import java.util.regex.Pattern;

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
        }
        return true;
    }

    public static boolean matchRegex(String str, String reg) {
        return Pattern.compile(reg).matcher(str).find();
    }

    public static FindCodeResult.Annotation findCodeAnnotation(Context ctx, String[] lines, int lineNumber) {

        FindCodeResult.Annotation res = new FindCodeResult.Annotation();
        res.find = false;
        if (lineNumber < 0 || lineNumber >= lines.length) {
            return res;
        }

        int start = ctx.document.getLineStartOffset(lineNumber);

        String down = ctx.content.substring(start);

        //这里假设注释风格不会混用，只会单纯出现 // 或者 /**/

        // 找 /**/
        int aEnd = down.indexOf("*/");
        if (aEnd != -1 && matchRegex(lines[ctx.document.getLineNumber(start + aEnd)], ".*\\*/\\s*$")) {
            int tStart = down.indexOf("/*");
            if (tStart != -1 && tStart < aEnd && matchRegex(down, "^\\s*/\\*.*")) {
                aEnd += start;
                tStart += start;
                res.find = true;
            } else if (tStart > aEnd || tStart == -1) {
                //当前行是/**/的中间,则注释是 tStart -> aEnd
                tStart = ctx.content.substring(0, start).lastIndexOf("/*");
                if (tStart != -1) {
                    aEnd += start;
                    res.find = true;
                }
            }
            if (res.find) {
                res.type = 1;
                res.startLine = ctx.document.getLineNumber(tStart);
                res.annotation = ctx.content.substring((tStart), (aEnd + "*/".length()));//+2
                res.endLine = ctx.document.getLineNumber(aEnd);
                return res;
            }
        }

        //找 //
        if (matchRegex(down, "^\\s*//.*")) {
            int anStart = lineNumber;
            aEnd = lineNumber;
            //往下找到结束的行
            for (int i = lineNumber + 1; i < lines.length; i++) {
                String line = StringUtils.trim(lines[i]);
                if (StringUtils.isBlank(line)) {
                    //如果有空行，按照go注释标准，这个注释不是某代码块的注释
                    res.find = false;
                    return res;
                }
                if (line.startsWith("//")) {
                    aEnd += 1;
                    continue;
                }
                break;
            }
            // 往上找到开始的行
            for (int i = lineNumber - 1; i >= 0; i--) {
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
            res.type = 0;
            res.startLine = anStart;
            res.annotation = ctx.content.substring(
                    ctx.document.getLineStartOffset(anStart), ctx.document.getLineEndOffset(aEnd));
            res.endLine = aEnd;
            res.find = true;
            return res;
        }
        return res;
    }

    public static FindCodeResult findCodeStart(Context ctx) {
        FindCodeResult res = new FindCodeResult();
        res.find = false;

        int lineNumber = ctx.caretModel.getLogicalPosition().line;
        String[] lines = ctx.content.split("\n");

        // TODO 空多行要不要处理

        //处理只空一行的情况
        if (StringUtils.isBlank(lines[lineNumber])) {
            if (lineNumber + 1 < lines.length && !StringUtils.isBlank(lines[lineNumber + 1])) {
                lineNumber += 1;
            } else {
                return res;
            }
        }

        FindCodeResult.Annotation ann = findCodeAnnotation(ctx, lines, lineNumber);
        if (!ann.find) {
            if (lineNumber >= 1) {
                ann = findCodeAnnotation(ctx, lines, lineNumber - 1);
            }
        }

        if (ann.find) {
            res.annotation = ann;
            res.startLine = ann.endLine + 1;
            res.code = ctx.content.substring(ctx.document.getLineStartOffset(res.startLine));
            return findCode(res);
        }

        res.annotation = new FindCodeResult.Annotation();
        res.annotation.find = false;
        res.startLine = lineNumber;
        res.code = ctx.content.substring(ctx.document.getLineStartOffset(res.startLine));

        return findCode(res);
    }

    public static FindCodeResult findCode(FindCodeResult res) {
        //处理完了还是注释那就先不管了
        if (StringUtils.isBlank(res.code) ||
                StringUtils.matchRegex(res.code, "^\\s*//.*") ||
                matchRegex(res.code, "^\\s*/\\*.*")||
                matchRegex(res.code, "^\\s*\\*/.*")) {
            res.find = false;
            return res;
        }
        //TODO find code end


        res.find = true;
        return res;
    }
}

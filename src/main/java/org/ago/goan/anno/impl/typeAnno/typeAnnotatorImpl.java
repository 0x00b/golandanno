package org.ago.goan.anno.impl.typeAnno;

import com.intellij.openapi.editor.CaretModel;
import org.ago.goan.anno.impl.DetectResult;
import org.ago.goan.anno.impl.GoType;
import org.ago.goan.utils.FindCodeResult;
import org.ago.goan.utils.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class typeAnnotatorImpl implements org.ago.goan.anno.impl.GoAnnotator {

    // match "type XXX ..."
    public static Pattern typeRegexPattern = Pattern.compile("^\\s*type\\s*(\\w+)\\s*(\\w+)\\s*");

    @Override
    public DetectResult detect(String code, CaretModel caretModel) {
        FindCodeResult codeResult = StringUtils.findCodeStart(code, caretModel);

        String typeCode = codeResult.getCode();

        GoType f = new GoType();
        Matcher funcRegexMatcher = typeRegexPattern.matcher(typeCode);

        if (!funcRegexMatcher.find()) {
            return null;
        }
        f.setName(funcRegexMatcher.group(1));
        f.setType(funcRegexMatcher.group(2));
        DetectResult res = new DetectResult();
        res.setResult(f);
        res.setCaretModel(caretModel);
        res.setStartLine(codeResult.getStartLine());
        res.setCode(typeCode);
        Matcher match = Pattern.compile("^(\\s*)\\w+").matcher(typeCode);
        if (match.find()) {
            res.setLinePrefix(match.group(1));
        }

        return res;
    }

    @Override
    public String generate(DetectResult result, String selectedCode) {

        if (result == null || null == result.getResult()) {
            return "";
        }

        if (result.getResult().getClass().equals(GoType.class)) {
            GoType type = (GoType) result.getResult();

            return String.format(result.getLinePrefix() + "//%s TODO", type.getName());
        }

        return "";
    }

}

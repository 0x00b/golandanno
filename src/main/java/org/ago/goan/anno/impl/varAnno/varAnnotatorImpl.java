package org.ago.goan.anno.impl.varAnno;

import org.ago.goan.anno.Context;
import org.ago.goan.anno.impl.DetectResult;
import org.ago.goan.anno.impl.GoType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class varAnnotatorImpl implements org.ago.goan.anno.impl.GoAnnotator {

    public static Pattern varRegexPattern = Pattern.compile("^\\s*(?:var)?\\s*((?:\\w+|(?:\\,\\s?)*)+)\\s*(\\w*|(?:\\{\\})*)\\s*");


    @Override
    public DetectResult detect(Context ctx) {

        String typeCode = ctx.code.code;

        GoType f = new GoType();
        Matcher funcRegexMatcher = varRegexPattern.matcher(typeCode);

        if (!funcRegexMatcher.find()) {
            return null;
        }
        f.setName(funcRegexMatcher.group(1));
        f.setType(funcRegexMatcher.group(2));
        DetectResult res = new DetectResult();
        res.result = (f);
         res.startLine = (ctx.code.startLine);
        res.code = (typeCode);
        Matcher match = Pattern.compile("^(\\s*)\\w+").matcher(typeCode);
        if (match.find()) {
            res.linePrefix = (match.group(1));
        }

        return res;
    }

    @Override
    public String generate(Context ctx, DetectResult result) {

        if (result.result.getClass().equals(GoType.class)) {
            GoType type = (GoType) result.result;

            return String.format(result.linePrefix + "//%s TODO", type.getName());
        }

        return "";
    }
}

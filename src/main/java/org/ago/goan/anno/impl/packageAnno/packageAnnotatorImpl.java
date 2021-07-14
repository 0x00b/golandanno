package org.ago.goan.anno.impl.packageAnno;

import org.ago.goan.anno.Context;
import org.ago.goan.anno.impl.DetectResult;
import org.ago.goan.anno.impl.GoType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class packageAnnotatorImpl implements org.ago.goan.anno.impl.GoAnnotator {

    public static Pattern varRegexPattern = Pattern.compile("^\\s*package\\s*(\\w+)\\s*$");


    @Override
    public DetectResult detect(Context ctx) {

        GoType f = new GoType();
        Matcher funcRegexMatcher = varRegexPattern.matcher(ctx.code.code);

        if (!funcRegexMatcher.find()) {
            return null;
        }
        f.setName(funcRegexMatcher.group(1));
        f.setType(funcRegexMatcher.group(2));
        DetectResult res = new DetectResult();
        res.result = f;
        res.startLine = ctx.code.startLine;
        res.code = ctx.code.code;
        Matcher match = Pattern.compile("^(\\s*)\\w+").matcher(ctx.code.code);
        if (match.find()) {
            res.linePrefix = match.group(1);
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

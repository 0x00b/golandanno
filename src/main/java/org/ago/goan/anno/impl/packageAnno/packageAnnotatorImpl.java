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
        Matcher funcRegexMatcher = varRegexPattern.matcher(ctx.code.Code);

        if (!funcRegexMatcher.find()) {
            return null;
        }
        f.setName(funcRegexMatcher.group(1));
        f.setType(funcRegexMatcher.group(2));
        DetectResult res = new DetectResult();
        res.setResult(f);
         res.setStartLine(ctx.code.StartLine);
        res.setCode(ctx.code.Code);
        Matcher match = Pattern.compile("^(\\s*)\\w+").matcher(ctx.code.Code);
        if (match.find()) {
            res.setLinePrefix(match.group(1));
        }

        return res;
    }

    @Override
    public String generate(Context ctx, DetectResult result, String selectedCode) {

        if (result.getResult().getClass().equals(GoType.class)) {
            GoType type = (GoType) result.getResult();

            return String.format(result.getLinePrefix() + "//%s TODO", type.getName());
        }

        return "";
    }
}

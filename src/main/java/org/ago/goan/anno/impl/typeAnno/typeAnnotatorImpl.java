package org.ago.goan.anno.impl.typeAnno;

import org.ago.goan.anno.Context;
import org.ago.goan.anno.Template;
import org.ago.goan.anno.impl.DetectResult;
import org.ago.goan.anno.impl.GoType;
import org.ago.goan.anno.impl.packageAnno.PackageTemplate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class typeAnnotatorImpl implements org.ago.goan.anno.impl.GoAnnotator {

    // match "type XXX ..."
    public static Pattern typeRegexPattern = Pattern.compile("^\\s*type\\s*(\\w+)\\s*(\\w+)\\s*");

    Template template;

    public typeAnnotatorImpl() {
        super();
        template = new TypeTemplate();
    }


    @Override
    public DetectResult detect(Context ctx) {

        GoType f = new GoType();
        Matcher funcRegexMatcher = typeRegexPattern.matcher(ctx.code.code);

        if (!funcRegexMatcher.find()) {
            return null;
        }
        f.setName(funcRegexMatcher.group(1));
        f.setType(funcRegexMatcher.group(2));
        DetectResult res = new DetectResult();
        res.result = (f);
        res.startLine = (ctx.code.startLine);
        res.code = (ctx.code.code);
        Matcher match = Pattern.compile("^(\\s*)\\w+").matcher(ctx.code.code);
        if (match.find()) {
            res.linePrefix = (match.group(1));
        }

        return res;
    }

    @Override
    public String generate(Context ctx, DetectResult result) {
        return template.generate(ctx,result);
    }

}

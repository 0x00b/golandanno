package org.ago.goan.anno.impl.packageAnno;

import com.intellij.ide.util.PropertiesComponent;
import org.ago.goan.anno.Context;
import org.ago.goan.anno.Template;
import org.ago.goan.anno.impl.DetectResult;
import org.ago.goan.anno.impl.funcAnno.TemplateImpl;
import org.ago.goan.cust.Constant;
import org.ago.goan.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class packageAnnotatorImpl implements org.ago.goan.anno.impl.GoAnnotator {


    public static Pattern packageRegexPattern = Pattern.compile("^\\s*package\\s*(\\w+)\\s*");

    Template template;

    public packageAnnotatorImpl() {
        super();
        template = new PackageTemplate();
    }

    @Override
    public DetectResult detect(Context ctx) {

        Matcher matcher = packageRegexPattern.matcher(ctx.code.code);

        if (!matcher.find()) {
            return null;
        }
        DetectResult res = new DetectResult();
        res.result = matcher.group(1);
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

        return template.generate(ctx, result);
    }
}

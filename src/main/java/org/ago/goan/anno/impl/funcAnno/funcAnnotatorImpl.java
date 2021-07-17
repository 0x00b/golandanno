package org.ago.goan.anno.impl.funcAnno;

import org.ago.goan.anno.Context;
import org.ago.goan.anno.impl.DetectResult;
import org.ago.goan.anno.impl.Variable;
import org.ago.goan.anno.Template;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class funcAnnotatorImpl implements org.ago.goan.anno.impl.GoAnnotator {


    public static String paramRegex = "\\(((?:\\w*\\s*\\.*\\*?\\w*(?:\\{\\})*\\[*\\]*\\,*)*)\\)";
    public static String returnRegex = "(\\w+|(?:\\((?:\\w*\\s*\\.*\\*?\\w*(?:\\{\\})*\\[*\\]*\\,*)\\)))";

    // match "func ... SomeFunc(t1 Type, t2 Type) ... {"
    public static Pattern funcRegexPattern = Pattern.compile("^\\s*(?:func(?:\\s+|\\s+\\(([\\w|\\s|\\*]+)\\)\\s+))?(\\w+)" +
            paramRegex + "\\s*" + returnRegex + "\\s*\\{?\\s*");

    // match "type Foo func(...) ...",
    public static Pattern typeFuncRegexPattern = Pattern.compile("^\\s*type\\s*(\\w+)\\s*func\\s*" +
            paramRegex + "\\s*" + returnRegex + "\\s*");

    Template template;

    public funcAnnotatorImpl() {
        super();
        template = new TemplateImpl();
    }

    @Override
    public DetectResult detect(Context ctx) {

        goFunc func = parse(ctx.code.code);
        if (func == null) {
            return null;
        }
        String funcCode = ctx.code.code;
        DetectResult res = new DetectResult();
        res.result = func;
        res.code = funcCode;
        res.startLine = ctx.code.startLine;
        Matcher match = Pattern.compile("^(\\s*)\\w+").matcher(funcCode);
        if (match.find()) {
            res.linePrefix = match.group(1);
        }

        return res;
    }

    goFunc parse(String code) {
        goFunc f = new goFunc();

        Matcher funcRegexMatcher = funcRegexPattern.matcher(code);

        if (funcRegexMatcher.find()) {
            f.setReceiver(Variable.ParseString(funcRegexMatcher.group(1)));
            f.setName(funcRegexMatcher.group(2));
            f.setParameters(Variable.ParseArrayString(funcRegexMatcher.group(3)));
            f.setReturns(Variable.ParseArrayString(funcRegexMatcher.group(4)));

            f.autoComplete();

            return f;
        }

        funcRegexMatcher = typeFuncRegexPattern.matcher(code);
        if (funcRegexMatcher.find()) {
            f.setName(funcRegexMatcher.group(1));
            f.setParameters(Variable.ParseArrayString(funcRegexMatcher.group(2)));
            f.setReturns(Variable.ParseArrayString(funcRegexMatcher.group(3)));

            f.autoComplete();

            return f;
        }
        return null;
    }

    @Override
    public String generate(Context ctx, DetectResult result) {

        return template.generate(ctx, result);
    }
}

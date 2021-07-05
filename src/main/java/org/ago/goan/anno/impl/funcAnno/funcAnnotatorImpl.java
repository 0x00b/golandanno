package org.ago.goan.anno.impl.funcAnno;

import com.intellij.openapi.editor.CaretModel;
import org.ago.goan.anno.impl.DetectResult;
import org.ago.goan.anno.impl.Variable;
import org.ago.goan.utils.FindCodeResult;
import org.ago.goan.utils.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class funcAnnotatorImpl implements org.ago.goan.anno.impl.GoAnnotator {

    // match "func ... SomeFunc(t1 Type, t2 Type) ... {"
    public static Pattern funcRegexPattern = Pattern.compile("^\\s*(?:func(?:\\s+|\\s+\\(([\\w|\\s|\\*]+)\\)\\s+))?(\\w+)" +
            "\\(((?:\\w*\\s*\\.*\\*?\\w*(?:\\{\\})*\\,*)*)\\)\\s*\\(?((?:\\w*\\s*\\.*\\*?\\w*(?:\\{\\})*\\,*)*)\\)?\\s*\\{?\\s*");

    // match "type Foo func(...) ...",
    public static Pattern typeFuncRegexPattern = Pattern.compile("^\\s*type\\s*(\\w+)\\s*func\\s*\\(((?:\\w*\\s*\\.*\\*?\\w*(?:\\{\\})*\\,*)*)\\)\\s*" +
            "\\(?((?:\\w*\\s*\\.*\\*?\\w*(?:\\{\\})*\\,*)*)\\)?\\s*");

    Template template;

    public funcAnnotatorImpl() {
        super();
        template = new TemplateImpl();
    }

    @Override
    public DetectResult detect(String code, CaretModel caretModel) {

        FindCodeResult codeResult = StringUtils.findCodeStart(code, caretModel);
        goFunc func = parse(codeResult.getCode());
        if (func == null) {
            return null;
        }
        String funcCode = codeResult.getCode();
        DetectResult res = new DetectResult();
        res.setResult(func);
        res.setCode(funcCode);
        res.setCaretModel(caretModel);
        res.setStartLine(codeResult.getStartLine());
        Matcher match = Pattern.compile("^(\\s*)\\w+").matcher(funcCode);
        if (match.find()) {
            res.setLinePrefix(match.group(1));
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
    public String generate(DetectResult result, String selectedCode) {
        if (result == null) {
            return "";
        }

        if (null == result.getResult()) {
            return "";
        }

        if (result.getResult().getClass().equals(goFunc.class)) {
            goFunc func = (goFunc) result.getResult();
            return template.generate(func, result.getLinePrefix(), selectedCode);
        }

        return "";
    }
}

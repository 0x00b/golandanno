package org.ago.goan.anno.impl.funcAnno;

import org.ago.goan.anno.Context;
import org.ago.goan.anno.impl.DetectResult;
import org.ago.goan.anno.Template;
import org.ago.goan.anno.impl.Variable;
import org.ago.goan.utils.StringUtils;
import sun.tools.jstack.JStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class funcAnnotatorImpl implements org.ago.goan.anno.impl.GoAnnotator {

//    public static String paramRegex = "\\(((?:\\w*\\s*\\.*\\*?\\w*(?:\\{\\})*\\[*\\]*\\,*)*)\\)";
//    public static String returnRegex = "((?:\\*?\\w+(?:\\{\\})*\\[*\\]*)*|(?:\\((?:\\w*\\s*\\.*\\*?\\w*(?:\\{\\})*\\[*\\]*\\,*)\\))*)";
//
//    // match "func ... SomeFunc(t1 Type, t2 Type) ... {"
//    public static Pattern funcRegexPattern = Pattern.compile("^\\s*(?:func(?:\\s+|\\s+\\(([\\w|\\s|\\*]+)\\)\\s+))?(\\w+)" +
//            paramRegex + "\\s*" + returnRegex + "\\s*\\{?\\s*");
//
//    // match "type Foo func(...) ...",
//    public static Pattern typeFuncRegexPattern = Pattern.compile("^\\s*type\\s*(\\w+)\\s*func\\s*" +
//            paramRegex + "\\s*" + returnRegex + "\\s*");
//    goFunc parse(String code) {
//        goFunc f = new goFunc();
//
//        Matcher funcRegexMatcher = funcRegexPattern.matcher(code);
//
//        if (funcRegexMatcher.find()) {
//            f.setReceiver(Variable.ParseString(funcRegexMatcher.group(1)));
//            f.setName(funcRegexMatcher.group(2));
//            f.setParameters(Variable.ParseArrayString(funcRegexMatcher.group(3)));
//            f.setReturns(Variable.ParseArrayString(funcRegexMatcher.group(4)));
//
//            f.autoComplete();
//
//            return f;
//        }
//
//        funcRegexMatcher = typeFuncRegexPattern.matcher(code);
//        if (funcRegexMatcher.find()) {
//            f.setName(funcRegexMatcher.group(1));
//            f.setParameters(Variable.ParseArrayString(funcRegexMatcher.group(2)));
//            f.setReturns(Variable.ParseArrayString(funcRegexMatcher.group(3)));
//
//            f.autoComplete();
//
//            return f;
//        }
//        return null;
//    }

    public goFunc parse(String code) {

        goFunc f = new goFunc();
        int index = 0;
        //func (r receiver) Foo (
        Matcher matcher = Pattern.compile("^\\s*(?:func(?:\\s+|\\s+\\(([\\w\\s\\*]+)\\)\\s+))?(\\w+)\\s*\\(").matcher(code);
        if (matcher.find()) {
            index = matcher.group(0).length();
            f.setReceiver(Variable.ParseString(matcher.group(1)));
            f.setName(matcher.group(2));

            //parse parameters and return parameters
            return parseInParams(f, code, index);

        } else {
            //type Foo func(
            matcher = Pattern.compile("^\\s*type\\s*(\\w+)\\s*func\\s*\\(").matcher(code);
            if (matcher.find()) {
                index = matcher.group(0).length();
                f.setName(matcher.group(1));

                //parse parameters and return parameters
                return parseInParams(f, code, index);
            }
        }

        //not function
        return null;
    }

    public static class ParseParamsResult {
        List<Variable> list;
        int start;
    }

    public ParseParamsResult parseParams(List<Variable> list, String code, int start) {

        // (x1, x2 int, f func(t1, t2 interface{}) )
        int end = findBracketEnd(code, start, false);
        if (-1 == end) {
            return null;
        }

        String paramCode = code.substring(start, end);
        String[] params = paramCode.split(",");

        for (int i = 0; i < params.length; i++) {
            String p = params[i];
            if (StringUtils.matchRegex(p, ".*func\\s*\\(")) {
                //find end ')
                int paramEnd = findBracketEnd(code, start, true);
                if (-1 == paramEnd) {
                    return null;
                }
                String funcParam = code.substring(start, paramEnd + 1);

                Variable v = Variable.ParseString(funcParam);
                if (v != null) {
                    list.add(v);
                }

                start = paramEnd + 1;
                if (start < end) {
                    return parseParams(list, code, start);
                } else {
                    break;
                }
            }

            Variable v = Variable.ParseString(p);
            if (v != null) {
                list.add(v);
            }
            start += p.length() + 1;
        }
        ParseParamsResult result = new ParseParamsResult();
        result.list = list;
        result.start = start;
        return result;
    }

    public goFunc parseInParams(goFunc func, String code, int start) {

        List<Variable> list = new ArrayList<>();
        ParseParamsResult result = parseParams(list, code, start);
        if (result == null) {
            return null;
        }
        func.setParameters(result.list);

        return parseReturns(func, code, result.start);
    }

    public goFunc parseReturns(goFunc func, String code, int start) {

        String returnParam = code.substring(start);

        Matcher matcher = Pattern.compile("^\\s*\\(").matcher(returnParam);
        if (matcher.find()) {
            List<Variable> list = new ArrayList<>();
            ParseParamsResult result = parseParams(list, code, start + matcher.group(0).length());
            if (result == null) {
                return null;
            }
            func.setReturns(result.list);
        } else {
            String[] ps = returnParam.split("\n");
            if (ps.length > 0) {
                // xxx
                matcher = Pattern.compile("^\\s*(\\**\\[*\\w*\\]*\\**\\w*\\.*\\w*(?:\\{\\})*)\\s*\\{?\\s*").matcher(ps[0]);
                if (matcher.find()) {
                    func.addReturns(Variable.ParseString(matcher.group(1)));
                }

                //TODO 返回值 func (
            }
        }

        return func;
    }


    public int findBracketEnd(String code, int start, boolean hasFirstBracket) {
        Stack<String> stack = new Stack();
        if (!hasFirstBracket) {
            stack.push("(");
        }
        for (int i = start; i < code.length(); i++) {
            char ch = code.charAt(i);
            if (ch == '(') {
                stack.push("(");
            }
            if (ch == ')') {
                stack.pop();
                if (stack.size() == 0) {
                    //find parameters end position
                    return i;
                }
            }
        }
        return -1;
    }

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


    @Override
    public String generate(Context ctx, DetectResult result) {

        return template.generate(ctx, result);
    }
}

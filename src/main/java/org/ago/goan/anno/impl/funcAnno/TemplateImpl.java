package org.ago.goan.anno.impl.funcAnno;

import com.google.errorprone.annotations.Var;
import com.intellij.ide.util.PropertiesComponent;
import org.ago.goan.anno.Template;
import org.ago.goan.anno.Context;
import org.ago.goan.anno.impl.Variable;
import org.ago.goan.cust.Constant;
import org.ago.goan.utils.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateImpl implements org.ago.goan.anno.impl.funcAnno.Template {

    String[] Keys = new String[]{
            FUNCTION_NAME,
            RECEIVER_NAME_TYPE,
            RECEIVER_NAME,
            RECEIVER_TYPE,
            PARAM_NAME_TYPE,
            PARAM_NAME,
            PARAM_TYPE,
            RET_NAME_TYPE,
            RET_NAME,
            RET_TYPE,
            Template.DATE,
            Template.GIT_NAME,
    };

    // 0:// other: /**/
    int annotateType;

    public final static String Receiver = "@receiver";
    public final static String Param = "@param";
    public final static String Return = "@return";
    public final static String Update = "@update";

    enum LineType {
        Line,
        LineReceiver,
        LineParam,
        LineReturn,
        LineUpdate,
    }

    public class LineTemplate {
        public List<String> keys;
        public String lineTemplate;
        public LineType type;
    }

    public List<LineTemplate> parseTemplate(Context ctx, String template) {

//        template = StringUtils.trim(template);
        List<LineTemplate> list = new ArrayList<LineTemplate>();
        if (!template.startsWith("//") && !template.startsWith("/*")) {
            return list;
        }

        if (template.startsWith("/*")) {
            ///*
            // aaa*/
            annotateType = 1;
            template = template.substring(2, template.lastIndexOf("*/"));
            if (template.charAt(template.length() - 1) == '\n') {
                // /*
                // * aaa
                // */
                annotateType = 2;
            }
        }

        String[] lines = template.split("\n");

        for (int i = 0; i < lines.length; i++) {
            LineTemplate kt = new LineTemplate();
            kt.lineTemplate = lines[i];
            kt.type = lineType(kt.lineTemplate);

            for (int j = 0; j < Keys.length; j++) {
                if (lines[i].contains(Keys[j])) {
                    if (kt.keys == null) {
                        kt.keys = new ArrayList<>();
                    }
                    kt.keys.add(Keys[j]);
                }
            }
            list.add(kt);
        }
        return list;
    }

    public LineType lineType(String line) {
        if (line.contains(Receiver)) {
            return LineType.LineReceiver;
        }
        if (line.contains(Param)) {
            return LineType.LineParam;
        }
        if (line.contains(Return)) {
            return LineType.LineReturn;
        }
        if (line.contains(Update)) {
            return LineType.LineUpdate;
        }
        return LineType.Line;
    }

    // 记住一个原则，go的参数type一定会有，name不一定有
    @Override
    public String generate(Context ctx, goFunc func, String linePrefix) {

        List<String> originAnnotation = null;
        if (ctx.code.annotation.find) {
            originAnnotation = parseOriginAnnotation(ctx);
        }

        String template = PropertiesComponent.getInstance().getValue(Constant.FUNC_TEMPLATE_KEY);

        if (template == null || StringUtils.isBlank(template)) {
            template = org.ago.goan.anno.impl.funcAnno.Template.DefaultFuncTemplate;
        }

        List<LineTemplate> lineTemplates = parseTemplate(ctx, template);
        if (null == lineTemplates) {
            return "";
        }

        String annotation = "";

        for (int i = 0; i < lineTemplates.size(); i++) {
            LineTemplate lineTemplate = lineTemplates.get(i);
            String line = lineTemplate.lineTemplate;
            switch (lineTemplate.type) {
                case LineParam:
                    if (null == func.getParameters()) {
                        break;
                    }
                    for (int j = 0; j < func.Parameters.size(); j++) {
                        line = replaceVariablesKey(ctx, line, func, lineTemplate, func.getParameters().get(j));
                        annotation = formatLine(ctx, i == 0, func, lineTemplate, originAnnotation, annotation, linePrefix, line) + "\n";
                    }
                    break;
                case LineReturn:
                    if (null == func.getReturns()) {
                        break;
                    }
                    for (Variable variable : func.getReturns()) {
                        line = replaceVariablesKey(ctx, line, func, lineTemplate, variable);
                        annotation = formatLine(ctx, i == 0, func, lineTemplate, originAnnotation, annotation, linePrefix, line) + "\n";
                    }
                    break;

                case LineReceiver:
                    if (func.getReceiver() == null) {
                        break;
                    }
                case Line:
                case LineUpdate:
                default:
                    line = replaceKey(ctx, func, lineTemplate);
                    annotation = formatLine(ctx, i == 0, func, lineTemplate, originAnnotation, annotation, linePrefix, line) + "\n";
            }
        }
        if (StringUtils.isBlank(annotation)) {
            return null;
        }
        annotation = annotation.substring(0, annotation.length() - "\n".length());
//        annotation = annotation.replaceAll("\\s+$", "");

        if (annotateType == 1 || annotateType == 2) {
            int idx = annotation.indexOf(linePrefix);
            if (idx < 0) {
                annotation = linePrefix + "/*" + annotation;
            } else {
                annotation = annotation.replaceFirst(linePrefix, linePrefix + "/*");
            }
            switch (annotateType) {
                case 1:
                    annotation = annotation + "*/";
                    break;
                case 2:
                    annotation = annotation + "\n" + linePrefix + "*/";
                    break;
                default:
            }
        }
        return annotation;
    }

    public String formatLine(Context ctx, boolean firstLine, goFunc func, LineTemplate lineTemplate, List<String> originAnnotation, String annotation, String linePrefix, String line) {

        String content = "";
        if (firstLine && originAnnotation != null && originAnnotation.size() == 1 &&
                StringUtils.matchReg(lineTemplate.lineTemplate, "^\\s*(?://|/\\*)\\s*\\$\\{func_name\\}")) {
            String origin = originAnnotation.get(0);
            if (StringUtils.matchReg(origin, "^\\s*(?://|/\\*)\\s*" + func.Name)) {
                Matcher m = Pattern.compile("^\\s*(?://|/\\*)\\s*" + func.Name + "\\s*(.*)").matcher(origin);
                if (m.find() && !StringUtils.isBlank(m.group(1))) {
                    content = origin.substring(origin.indexOf(m.group(1)));
                }
            } else {
                //满足go规范的注释模版，如果原来的不满足，则直接用原来的注释补充第一行
                content = origin.trim().substring(2).trim();
            }

        } else {
            content = originContent(ctx, originAnnotation, line);
        }

//        content.split("\n");

        annotation += linePrefix + line + content;

        return annotation;

    }

    String originContent(Context ctx, List<String> originAnnotation, String line) {
        if (originAnnotation == null) {
            return "";
        }

        line = line.replace("{", "\\{").replace("}", "\\}").replace(".", "\\.").
                replace("*", "\\*").replace("[", "\\[").replace("]", "\\]");
        String reg = line.replaceAll("\\s+", "\\\\s*") + "(.+)";

        for (String s : originAnnotation) {
            Matcher matcher = Pattern.compile(reg).matcher(s);
            if (matcher.find()) {
                String t = matcher.group(1);
                if (StringUtils.isBlank(t)) {
                    return "";
                }
                return s.substring(s.indexOf(t));
            }
        }

        return "";
    }

    List<String> parseOriginAnnotation(Context ctx) {
        if (!ctx.code.annotation.find) {
            return null;

        }
        String content = ctx.code.annotation.annotation;
        String[] lines = content.split("\n");
        if (lines.length == 0) {
            return null;
        }

        List<String> annotations = new ArrayList<>();

        int current = 0;
        annotations.add(lines[0]);


        for (int i = 1; i < lines.length; i++) {
            LineType type = lineType(lines[i]);
            if (type == LineType.Line) {
                annotations.set(current, annotations.get(current) + "\n" + lines[i]);
                continue;
            }
            current++;
            annotations.add(lines[i]);
        }

        return annotations;
    }

    public String replaceVariablesKey(Context ctx, String line, goFunc func, LineTemplate lineTemplate, Variable variable) {

        line = replaceKey(ctx, func, lineTemplate);

        for (int j = 0; j < lineTemplate.keys.size(); j++) {
            String key = lineTemplate.keys.get(j);
            int type = 0;
            switch (lineTemplate.keys.get(j)) {
                case PARAM_NAME_TYPE:
                case RET_NAME_TYPE:
                    type = 0;
                    break;
                case PARAM_NAME:
                case RET_NAME:
                    type = 1;
                    break;
                case PARAM_TYPE:
                case RET_TYPE:
                    type = 2;
                    break;
            }
            line = replaceVariableKey(ctx, line, key, variable, type);
        }

        return line;

    }

    public String replaceVariableKey(Context ctx, String line, String key, Variable variable, int type) {
        switch (type) {
            case 1: //name
                if (!StringUtils.isBlank(variable.getName())) {
                    line = line.replace(key, variable.getName());
                } else {
                    line = line.replace(key, variable.getType());
                }
                break;
            case 2: //type
                line = line.replace(key, variable.getType());
                break;
            default:
                if (!StringUtils.isBlank(variable.getName()) && !StringUtils.isBlank(variable.getType())) {
                    line = line.replace(key, variable.getName() + " " + variable.getType());
                } else {
                    line = line.replace(key, variable.getType());
                }
                break;
        }
        return line;
    }

    public String replaceKey(Context ctx, goFunc func, LineTemplate lineTemplate) {
        String line = lineTemplate.lineTemplate;
        for (int j = 0; j < lineTemplate.keys.size(); j++) {
            switch (lineTemplate.keys.get(j)) {
                case FUNCTION_NAME:
                    line = line.replace(FUNCTION_NAME, func.Name);
                    break;
                case RECEIVER_NAME_TYPE:
                    if (null == func.getReceiver() || StringUtils.isBlank(func.getReceiver().getType())) {
                        line = line.replace(RECEIVER_NAME_TYPE, "");
                        continue;
                    }
                    if (!StringUtils.isBlank(func.getReceiver().getName())) {
                        String value = func.getReceiver().getName() + " " + func.getReceiver().getType();
                        line = line.replace(RECEIVER_NAME_TYPE, value);
                    } else {
                        line = line.replace(RECEIVER_NAME_TYPE, func.getReceiver().getType());
                    }
                    break;
                case RECEIVER_NAME:
                    if (null == func.getReceiver()) {
                        line = line.replace(RECEIVER_NAME_TYPE, "");
                        continue;
                    }
                    if (!StringUtils.isBlank(func.getReceiver().getName())) {
                        line = line.replace(RECEIVER_NAME, func.getReceiver().getName());
                    } else {
                        line = line.replace(RECEIVER_NAME, func.getReceiver().getType());
                    }
                    break;
                case RECEIVER_TYPE:
                    if (null == func.getReceiver()) {
                        continue;
                    }
                    line = line.replace(RECEIVER_TYPE, func.getReceiver().getType());
                    break;
                case Template.DATE:
                    line = line.replace(Template.DATE, ctx.template.date);
                case Template.GIT_NAME:
                    line = line.replace(Template.GIT_NAME, ctx.template.gitName);
                default:
            }
        }
        return line;
    }


}

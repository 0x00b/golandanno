package org.ago.goan.anno.impl.funcAnno;

import com.intellij.ide.util.PropertiesComponent;
import org.ago.goan.anno.Template;
import org.ago.goan.anno.Context;
import org.ago.goan.anno.impl.DetectResult;
import org.ago.goan.anno.impl.Variable;
import org.ago.goan.cust.Constant;
import org.ago.goan.utils.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateImpl extends Template {

    // default template code
    public static final String DefaultFuncTemplate = "// ${func_name} \n" +
            "//  @receiver ${receiver_name} \n" +
            "//  @param ${param_name} \n" +
            "//  @return ${return_name} \n";

    // current method name
    final String FUNCTION_NAME = "${func_name}";

    // golang receiver
    final String RECEIVER_NAME_TYPE = "${receiver_name_type}";
    // golang receiver name
    final String RECEIVER_NAME = "${receiver_name}";
    // golang receiver type
    final String RECEIVER_TYPE = "${receiver_type}";

    // param info
    final String PARAM_NAME_TYPE = "${param_name_type}";
    // param name info
    final String PARAM_NAME = "${param_name}";
    // param info
    final String PARAM_TYPE = "${param_type}";

    // return info
    final String RET_NAME_TYPE = "${return_name_type}";
    // return name
    final String RET_NAME = "${return_name}";
    // return type
    final String RET_TYPE = "${return_type}";

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

    private static final int LineReceiver = 2;
    private static final int LineParam = 3;
    private static final int LineReturn = 4;

    @Override
    public int lineType(String line) {
        if (line.contains(Receiver)) {
            return LineReceiver;
        }
        if (line.contains(Param)) {
            return LineParam;
        }
        if (line.contains(Return)) {
            return LineReturn;
        }
        if (line.contains(Update)) {
            return LineUpdate;
        }
        return LineType;
    }

    // 记住一个原则，go的参数type一定会有，name不一定有
//    @Override
    public String generate(Context ctx, DetectResult result) {

        if (!result.result.getClass().equals(goFunc.class)) {
            return "";
        }

        goFunc func = (goFunc) result.result;

        List<String> originAnnotation = null;
        if (ctx.code.annotation.find) {
            originAnnotation = parseOriginAnnotation(ctx);
        }
        String linePrefix = result.linePrefix;

        String template = PropertiesComponent.getInstance().getValue(Constant.FUNC_TEMPLATE_KEY);

        if (template == null || StringUtils.isBlank(template)) {
            template = DefaultFuncTemplate;
        }

        List<LineTemplate> lineTemplates = parseTemplate(ctx, Keys, template);
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
                case LineType:
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
                StringUtils.matchRegex(lineTemplate.lineTemplate, "^\\s*(?://|/\\*)\\s*\\$\\{func_name\\}")) {
            String origin = originAnnotation.get(0);
            if (StringUtils.matchRegex(origin, "^\\s*(?://|/\\*)\\s*" + func.Name)) {
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

//      content.split("\n");

        annotation += linePrefix + line + content;

        return annotation;

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
                    line = line.replace(Template.DATE, ctx.date);
                case Template.GIT_NAME:
                    line = line.replace(Template.GIT_NAME, ctx.gitName);
                default:
            }
        }
        return line;
    }


}

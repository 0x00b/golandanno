package org.ago.goan.anno.impl.varAnno;

import com.intellij.ide.util.PropertiesComponent;
import org.ago.goan.anno.Context;
import org.ago.goan.anno.Template;
import org.ago.goan.anno.impl.DetectResult;
import org.ago.goan.anno.impl.GoType;
import org.ago.goan.cust.Constant;
import org.ago.goan.utils.StringUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VarTemplate extends Template {

    // default template code
    public static final String DefaultVarTemplate = "// ${var_name} \n" ;
//            "// ${var_type} \n";

    public static final String VAR_NAME = "${var_name}";

    public static final String VAR_TYPE = "${var_type}";


    String[] Keys = new String[]{
            VAR_NAME,
            VAR_TYPE,
            DATE,
            GIT_NAME,
    };


    // default template code

    static final String PACKAGE_NAME = "${package_name}";

    @Override
    public int lineType(String line) {
        if (line.contains(Update)) {
            return LineUpdate;
        }
        return LineType;
    }

    @Override
    public String generate(Context ctx, DetectResult result) {

        if (result.result.getClass().equals(GoType.class)) {
            GoType type = (GoType) result.result;

            List<String> originAnnotation = null;
            if (ctx.code.annotation.find) {
                originAnnotation = parseOriginAnnotation(ctx);
            }

            String template = PropertiesComponent.getInstance().getValue(Constant.PACKAGE_TEMPLATE_KEY);

            if (template == null || StringUtils.isBlank(template)) {
                template = DefaultVarTemplate;
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
                    case LineType:
                    case LineUpdate:
                    default:
                        line = replaceKey(ctx, type, lineTemplate);
                        line = formatLine(ctx, i == 0, type, lineTemplate, originAnnotation, annotation, result.linePrefix, line);
                        annotation = line + "\n";
                }
            }
            if (StringUtils.isBlank(annotation)) {
                return null;
            }
            annotation = annotation.substring(0, annotation.length() - "\n".length());

            if (annotateType == 1 || annotateType == 2) {
                annotation = "/*" + annotation;
                switch (annotateType) {
                    case 1:
                        annotation = annotation + "*/";
                        break;
                    case 2:
                        annotation = annotation + "\n" + "*/";
                        break;
                    default:
                }
            }
            return annotation;
        }
        return "";
    }

    public String replaceKey(Context ctx, GoType type, LineTemplate lineTemplate) {
        String line = lineTemplate.lineTemplate;
        for (int j = 0; j < lineTemplate.keys.size(); j++) {
            switch (lineTemplate.keys.get(j)) {
                case VAR_NAME:
                    line = line.replace(VAR_NAME, type.getName());
                    break;
                case VAR_TYPE:
                    line = line.replace(VAR_TYPE, type.getType());
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

    public String formatLine(Context ctx, boolean firstLine, GoType type, LineTemplate lineTemplate, List<String> originAnnotation, String annotation, String linePrefix, String line) {

        String content = "";
        if (firstLine && originAnnotation != null && originAnnotation.size() == 1 &&
                StringUtils.matchRegex(lineTemplate.lineTemplate, "^\\s*(?://|/\\*)\\s*\\$\\{var_name\\}")) {
            String origin = originAnnotation.get(0);
            if (StringUtils.matchRegex(origin, "^\\s*(?://|/\\*)\\s*" + type.getName())) {
                Matcher m = Pattern.compile("^\\s*(?://|/\\*)\\s*" + type.getName() + "\\s*(.*)").matcher(origin);
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

}
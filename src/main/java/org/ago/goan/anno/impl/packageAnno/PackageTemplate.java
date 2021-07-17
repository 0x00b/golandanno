package org.ago.goan.anno.impl.packageAnno;

import com.intellij.ide.util.PropertiesComponent;
import org.ago.goan.anno.Context;
import org.ago.goan.anno.Template;
import org.ago.goan.anno.impl.DetectResult;
import org.ago.goan.cust.Constant;
import org.ago.goan.utils.StringUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PackageTemplate extends Template {

    String[] Keys = new String[]{
            PACKAGE_NAME,
            DATE,
            GIT_NAME,
    };


    // default template code
    public static final String DefaultPackageTemplate = "// Package ${package_name} \n";

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

        if (result.result.getClass().equals(String.class)) {
            String packageName = (String) result.result;

            List<String> originAnnotation = null;
            if (ctx.code.annotation.find) {
                originAnnotation = parseOriginAnnotation(ctx);
            }

            String template = PropertiesComponent.getInstance().getValue(Constant.PACKAGE_TEMPLATE_KEY);

            if (template == null || StringUtils.isBlank(template)) {
                template = DefaultPackageTemplate;
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
                        line = replaceKey(ctx, packageName, lineTemplate);
                        line = formatLine(ctx, i == 0, packageName, lineTemplate, originAnnotation, annotation, line);
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

    public String formatLine(Context ctx, boolean firstLine, String packageName, LineTemplate lineTemplate, List<String> originAnnotation, String annotation, String line) {

        String content = "";
        if (firstLine && originAnnotation != null && originAnnotation.size() == 1 &&
                StringUtils.matchRegex(lineTemplate.lineTemplate, "^\\s*(?://|/\\*)\\s*Package\\s*\\$\\{package_name\\}")) {
            String origin = originAnnotation.get(0);
            if (StringUtils.matchRegex(origin, "^\\s*(?://|/\\*)\\s*Package\\s*" + packageName)) {
                Matcher m = Pattern.compile("^\\s*(?://|/\\*)\\s*Package\\s*" + packageName + "\\s*(.*)").matcher(origin);
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

        annotation += line + content;

        return annotation;

    }

    public String replaceKey(Context ctx, String packageName, LineTemplate lineTemplate) {
        String line = lineTemplate.lineTemplate;
        for (int j = 0; j < lineTemplate.keys.size(); j++) {
            switch (lineTemplate.keys.get(j)) {
                case PackageTemplate.PACKAGE_NAME:
                    line = line.replace(PackageTemplate.PACKAGE_NAME, packageName);
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
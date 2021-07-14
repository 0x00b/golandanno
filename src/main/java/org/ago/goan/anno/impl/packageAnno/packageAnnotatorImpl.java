package org.ago.goan.anno.impl.packageAnno;

import com.intellij.ide.util.PropertiesComponent;
import org.ago.goan.anno.Context;
import org.ago.goan.anno.Template;
import org.ago.goan.anno.impl.DetectResult;
import org.ago.goan.cust.Constant;
import org.ago.goan.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class packageAnnotatorImpl implements org.ago.goan.anno.impl.GoAnnotator {


    public static Pattern packageRegexPattern = Pattern.compile("^\\s*package\\s*(\\w+)\\s*");

    String[] Keys = new String[]{
            PackageTemplate.PACKAGE_NAME,
            Template.DATE,
            Template.GIT_NAME,
    };


    // 0:// other: /**/
    int annotateType;

    public final static String Update = "@update";

    enum LineType {
        Line,
        LineUpdate,
    }

    public class LineTemplate {
        public List<String> keys;
        public String lineTemplate;
        public LineType type;
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

        if (result.result.getClass().equals(String.class)) {
            String packageName = (String) result.result;

            List<String> originAnnotation = null;
            if (ctx.code.annotation.find) {
                originAnnotation = parseOriginAnnotation(ctx);
            }

            String template = PropertiesComponent.getInstance().getValue(Constant.PACKAGE_TEMPLATE_KEY);

            if (template == null || StringUtils.isBlank(template)) {
                template = PackageTemplate.DefaultPackageTemplate;
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
                    case Line:
                    case LineUpdate:
                    default:
                        line = replaceKey(ctx, packageName, lineTemplate);
                        annotation = line + "\n";
                }
            }
            if (StringUtils.isBlank(annotation)) {
                return null;
            }
//            annotation = annotation.substring(0, annotation.length() - "\n".length());
//        annotation = annotation.replaceAll("\\s+$", "");

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

    public String replaceKey(Context ctx, String packageName, LineTemplate lineTemplate) {
        String line = lineTemplate.lineTemplate;
        for (int j = 0; j < lineTemplate.keys.size(); j++) {
            switch (lineTemplate.keys.get(j)) {
                case PackageTemplate.PACKAGE_NAME:
                    line = line.replace(PackageTemplate.PACKAGE_NAME, packageName);
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

    public LineType lineType(String line) {
        if (line.contains(Update)) {
            return LineType.LineUpdate;
        }
        return LineType.Line;
    }

    public List<LineTemplate> parseTemplate(Context ctx, String template) {

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
}

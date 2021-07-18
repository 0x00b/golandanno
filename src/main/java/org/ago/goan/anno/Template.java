package org.ago.goan.anno;


import org.ago.goan.anno.impl.DetectResult;
import org.ago.goan.anno.impl.funcAnno.TemplateImpl;
import org.ago.goan.anno.impl.packageAnno.packageAnnotatorImpl;
import org.ago.goan.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Template {

    // current date
    public final static String DATE = "${date}";
    public final static String GIT_NAME = "${git_name}";

    public final static String Update = "@update";

    public class LineTemplate {
        public List<String> keys;
        public String lineTemplate;
        public int type;
    }

    // 0:// other: /**/
    public int annotateType;

    public static final int LineType = 0;
    public static final int LineUpdate = 1;

    abstract public int lineType(String line);

    abstract public String generate(Context ctx, DetectResult result);


    public List<LineTemplate> parseTemplate(Context ctx, String[] keys, String template) {
        //template = StringUtils.trim(template);
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

            if (!StringUtils.matchRegex(lines[i], ".*\\s+$")) {
                //主动补个空格,真正的注释是空格后的，查找原注释内容按照这个规则
                lines[i] += " ";
            }
            kt.lineTemplate = lines[i];
            kt.type = lineType(kt.lineTemplate);

            for (int j = 0; j < keys.length; j++) {
                if (lines[i].contains(keys[j])) {
                    if (kt.keys == null) {
                        kt.keys = new ArrayList<>();
                    }
                    kt.keys.add(keys[j]);
                }
            }
            list.add(kt);
        }
        return list;
    }

    public List<String> parseOriginAnnotation(Context ctx) {
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
            int type = lineType(lines[i]);
            if (type == LineType) {
                annotations.set(current, annotations.get(current) + "\n" + lines[i]);
                continue;
            }
            current++;
            annotations.add(lines[i]);
        }

        return annotations;
    }

    public String originContent(Context ctx, List<String> originAnnotation, String line) {
        if (originAnnotation == null) {
            return "";
        }

        line = line.replace("{", "\\{").replace("}", "\\}").replace(".", "\\.").
                replace("*", "\\*").replace("[", "\\[").replace("]", "\\]").
                replace("(", "\\(").replace(")", "\\)");
        String reg = line.replaceAll("\\s+", "\\\\s*") + "\\s+(.+)";

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
}
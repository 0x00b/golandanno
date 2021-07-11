package org.ago.goan.anno.impl.funcAnno;

import com.intellij.ide.util.PropertiesComponent;
import org.ago.goan.anno.CommonTemplate;
import org.ago.goan.anno.Context;
import org.ago.goan.anno.impl.Variable;
import org.ago.goan.cust.Constant;
import org.ago.goan.utils.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TemplateImpl implements  Template {

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
            CommonTemplate.DATE,
    };

    int AnnotateType;

    public String[] getKeys() {
        return Keys;
    }

    public void setKeys(String[] keys) {
        Keys = keys;
    }

    public int getAnnotateType() {
        return AnnotateType;
    }

    public void setAnnotateType(int annotateType) {
        AnnotateType = annotateType;
    }

    public class KeyTemplate{
         String Key;
        String Template;

        public String getKey() {
            return Key;
        }

        public void setKey(String key) {
            Key = key;
        }

        public String getTemplate() {
            return Template;
        }

        public void setTemplate(String template) {
            Template = template;
        }
    }

    // 记住一个原则，go的参数type一定会有，name不一定有
    @Override
    public String generate(Context ctx, goFunc func, String linePrefix, String selectedCode) {

        String[] selectedCodes = selectedCode.split("\n");
        for (int i = 0; i < selectedCodes.length; i++) {
            selectedCodes[i] = selectedCodes[i].replace("TODO","");
        }

        String template = PropertiesComponent.getInstance().getValue(Constant.FUNC_TEMPLATE_KEY);

        if (template == null) {
            template = Template.DefaultFuncTemplate;
        }

        List<KeyTemplate> keyTemplates = parse(template);
        if (null == keyTemplates){
            return "";
        }

        String annotation = "";
        String tLine = null;
        for (int i = 0; i < keyTemplates.size(); i++) {
            KeyTemplate kt = keyTemplates.get(i);
            String line = null;
            List<Variable> ps = func.getParameters();
            List<Variable> rs = func.getReturns();
            switch (kt.getKey()){
                case FUNCTION_NAME:
                    tLine = kt.getTemplate().replace(FUNCTION_NAME, func.Name);
                    line = linePrefix  + originContent(selectedCodes, tLine);
                    break;
                case RECEIVER_NAME_TYPE:
                    if (null == func.getReceiver() ||  StringUtils.isBlank(func.getReceiver().getType())){
                        continue;
                    }
                    line = linePrefix + kt.getTemplate().replace(RECEIVER_NAME_TYPE,  func.getReceiver().getType());
                    if (!StringUtils.isBlank(func.getReceiver().getName())){
                        String value = func.getReceiver().getName()+" "+func.getReceiver().getType();
                        value =  kt.getTemplate().replace(RECEIVER_NAME_TYPE, value);
                        line = linePrefix + originContent(selectedCodes, value);
                    }
                    break;
                case RECEIVER_NAME:
                    if (null == func.getReceiver()){
                        continue;
                    }
                    if (!StringUtils.isBlank(func.getReceiver().getName())){
                        tLine = kt.getTemplate().replace(RECEIVER_NAME, func.getReceiver().getName());
                        line = linePrefix + originContent(selectedCodes, tLine);
                    }else if(!StringUtils.isBlank(func.getReceiver().getType())) {
                        line = linePrefix + kt.getTemplate().replace(RECEIVER_NAME, func.getReceiver().getType());
                    }
                    break;
                case RECEIVER_TYPE:
                    if (null == func.getReceiver()){
                        continue;
                    }
                    if (!StringUtils.isBlank(func.getReceiver().getType())){
                        line = linePrefix + kt.getTemplate().replace(RECEIVER_TYPE, func.getReceiver().getType());
                    }
                    break;
                case PARAM_NAME_TYPE:
                    line = formatListLine(PARAM_NAME_TYPE, kt.getTemplate(),selectedCodes,linePrefix, ps,0);
                    break;
                case PARAM_NAME:
                    line = formatListLine(PARAM_NAME, kt.getTemplate(),selectedCodes,linePrefix, ps,1);
                    break;
                case PARAM_TYPE:
                    line = formatListLine(PARAM_TYPE, kt.getTemplate(),selectedCodes,linePrefix, ps,2);
                    break;
                case RET_NAME_TYPE:
                    line = formatListLine(RET_NAME_TYPE, kt.getTemplate(),selectedCodes,linePrefix, rs,0);
                    break;
                case RET_NAME:
                    line = formatListLine(RET_NAME, kt.getTemplate(),selectedCodes,linePrefix, rs,1);
                    break;
                case RET_TYPE:
                    line = formatListLine(RET_TYPE, kt.getTemplate(),selectedCodes, linePrefix, rs,2);
                    break;
                case CommonTemplate.DATE:
                    line = linePrefix + kt.getTemplate().replace(CommonTemplate.DATE, new Date().toString());
                    break;
                default:
                    line = linePrefix + kt.getTemplate();
            }
            if (null != line){
                annotation += line;
                if (i < keyTemplates.size()-1){
                    annotation += "\n";
                }
            }
        }
        if (StringUtils.isBlank(annotation)){
            return null;
        }
        annotation = annotation.replaceAll("\\s+$", "");

        if (AnnotateType == 1 || AnnotateType == 2){
            int idx = annotation.indexOf(linePrefix);
            if (idx < 0 ){
                annotation = linePrefix + "/*"+annotation;
            }else{
                annotation = annotation.replaceFirst(linePrefix, linePrefix+"/*");
            }
            switch (AnnotateType){
                case 1:
                    annotation =  annotation + "*/";
                    break;
                case 2:
                    annotation =  annotation+"\n" + linePrefix + "*/";
                    break;
                default:
            }
        }
        return annotation;
    }

    public List<KeyTemplate> parse(String template){

        template = StringUtils.trim(template);

        if (template.startsWith("/*")){
            ///*
            // aaa*/
            AnnotateType = 1;
            template = template.substring(2, template.lastIndexOf("*/"));
            if (template.charAt(template.length()-1) == '\n'){
                // /*
                // * aaa
                // */
                AnnotateType = 2;
            }
        }

        String[] lines = template.split("\n");
        List<KeyTemplate> list = new ArrayList<KeyTemplate>();

        for (int i = 0; i < lines.length; i++) {
            int j = 0;
            for (; j < Keys.length; j++) {
                if (lines[i].contains(Keys[j])){
                    KeyTemplate kt = new KeyTemplate();
                    kt.Key = Keys[j];
                    kt.Template = lines[i];
                    list.add(kt);
                    break;
                }
            }
            if (j == Keys.length){
                //不认识的行，原封不动
                KeyTemplate kt = new KeyTemplate();
                kt.Template = lines[i];
                kt.Key = "";
                list.add(kt);
            }
        }
        return list;
    }

    String formatListLine(String key, String template,String[] selectCodes,String linePrefix, List<Variable> list, int type){
        if (list == null || list.size()==0) {
            return null;
        }
        String line = "";
        switch (type){
            case 1: //name
                for (int j = 0; j < list.size(); j++) {
                    if (!StringUtils.isBlank(list.get(j).getName())){
                        String tLine = template.replace(key, list.get(j).getName());
                        line += linePrefix + originContent(selectCodes, tLine);
                    }else if (!StringUtils.isBlank(list.get(j).getType())){
                        line += linePrefix + template.replace(key, list.get(j).getType());
                    }
                    if (j < list.size()-1){
                        line += "\n";
                    }
                }
                break;
            case 2: //type
                for (int j = 0; j < list.size(); j++) {
                    if (StringUtils.isBlank(list.get(j).getType())){
                        continue;
                    }
                    line += linePrefix + template.replace(key, list.get(j).getType());
                    if (j < list.size()-1){
                        line += "\n";
                    }
                }
                break;
            default:
                String tLine = null;
                for (int j = 0; j < list.size(); j++) {
                    if (StringUtils.isBlank(list.get(j).getName()) && StringUtils.isBlank(list.get(j).getType())){
                        continue;
                    }
                    if (!StringUtils.isBlank(list.get(j).getName())){
                        tLine = list.get(j).getName()+ " "+ list.get(j).getType();
                        tLine = template.replace(key, tLine);
                        line += linePrefix + originContent(selectCodes, tLine);
                    }else {
                        tLine = list.get(j).getType();
                        line += linePrefix + template.replace(key, tLine);
                    }
                    if (j < list.size()-1){
                        line += "\n";
                    }
                }
                break;
        }
        if (StringUtils.isBlank(line)){
            return null;
        }
        return line;
    }

    String originContent(String[] selectedCodes, String value){

        String old = value;
        int idx = value.indexOf("TODO");
        if (idx != -1){
            value = value.replace("TODO","");
        }
        for (int i = 0; i < selectedCodes.length; i++) {
            int index = selectedCodes[i].indexOf(value);
            if (-1 != index){
                String origin =  selectedCodes[i].substring(index + value.length());
                if (StringUtils.isBlank(origin)){
                    return old;
                }
                return value + origin;
            }
        }

        return old;
    }

}

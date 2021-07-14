package org.ago.goan.anno.impl.funcAnno;

import org.ago.goan.anno.Context;

public interface Template {

    String generate(Context ctx, goFunc func, String linePrefix);

    // default template code
    String DefaultFuncTemplate = "// ${func_name} \n" +
            "//  @receiver ${receiver_name} \n" +
            "//  @param ${param_name} \n" +
            "//  @return ${return_name} \n";

    // current method name
    String FUNCTION_NAME = "${func_name}";

    // golang receiver
    String RECEIVER_NAME_TYPE = "${receiver_name_type}";
    // golang receiver name
    String RECEIVER_NAME = "${receiver_name}";
    // golang receiver type
    String RECEIVER_TYPE = "${receiver_type}";

    // param info
    String PARAM_NAME_TYPE = "${param_name_type}";
    // param name info
    String PARAM_NAME = "${param_name}";
    // param info
    String PARAM_TYPE = "${param_type}";

    // return info
    String RET_NAME_TYPE = "${return_name_type}";
    // return name//    public List<LineTemplate> parse(String template) {
    ////
    ////        template = StringUtils.trim(template);
    ////        List<LineTemplate> list = new ArrayList<LineTemplate>();
    ////        if (!template.startsWith("//") || !template.startsWith("/*")){
    ////            return list;
    ////        }
    ////
    ////        if (template.startsWith("/*")) {
    ////            ///*
    ////            // aaa*/
    ////            AnnotateType = 1;
    ////            template = template.substring(2, template.lastIndexOf("*/"));
    ////            if (template.charAt(template.length() - 1) == '\n') {
    ////                // /*
    ////                // * aaa
    ////                // */
    ////                AnnotateType = 2;
    ////            }
    ////        }
    ////
    ////        String[] lines = template.split("\n");
    ////
    ////        for (int i = 0; i < lines.length; i++) {
    ////            int j = 0;
    ////            LineTemplate kt = new LineTemplate();
    ////            kt.lineTemplate = lines[i];
    ////            for (; j < Keys.length; j++) {
    ////                if (lines[i].contains(Keys[j])) {
    ////                    if (kt.keys == null) {
    ////                        kt.keys = new ArrayList<>();
    ////                    }
    ////                    kt.keys.add(Keys[i]);
    ////                }
    ////            }
    ////            list.add(kt);
    ////        }
    ////        return list;
    String RET_NAME = "${return_name}";
    // return type
    String RET_TYPE = "${return_type}";
}

// ${func_name} 
// @receiver ${receiver_name_type}:
// @param ${param_name_type}:
// @return ${return_name_type}:

// ${func_name} 
// @receiver ${receiver_name}:
// @param ${param_name}:
// @return ${return_name}:

// ${func_name} 
// @receiver ${receiver_type}:
// @param ${param_type}:
// @return ${return_type}:

/* ${func_name} 
 * @receiver ${receiver_name_type}:
 * @param ${param_name_type}:
 * @return ${return_name_type}: */

/*
 * ${func_name} 
 * @receiver ${receiver_name_type}:
 * @param ${param_name_type}:
 * @return ${return_name_type}: */

/**
 * ${func_name} 
 * *@receiver ${receiver_name_type}:
 * *@param ${param_name_type}:
 * *@return ${return_name_type}:
 * <p>
 * *${func_name} 
 * *@receiver ${receiver_name_type}:
 * *@param ${param_name_type}:
 * *@return ${return_name_type}:
 **/

/**
 **${func_name} 
 **@receiver ${receiver_name_type}:
 **@param ${param_name_type}:
 **@return ${return_name_type}:
 **/
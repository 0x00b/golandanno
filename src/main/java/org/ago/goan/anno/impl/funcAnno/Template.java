package org.ago.goan.anno.impl.funcAnno;

public interface Template {

    String generate(goFunc func, String linePrefix, String selectedCode);

    // default template code
    String DefaultFuncTemplate = "//${function_name} TODO\n" +
            "//@receiver ${receiver_name_type}: \n" +
            "//@param ${param_name_type}: \n" +
            "//@return ${ret_name_type}: \n";

    String FUNC_TEMPLATE_KEY = "GoAn.Edit.FuncTemplate";

    // current method name
    String FUNCTION_NAME = "${function_name}";

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
    String RET_NAME_TYPE = "${ret_name_type}";
    // return name
    String RET_NAME = "${ret_name}";
    // return type
    String RET_TYPE = "${ret_type}";

    // current date in "yyyy-MM-dd HH:mm:ss"
    String DATE = "${date}";


}

//${function_name} TODO
//@receiver ${receiver_name_type}:
//@param ${param_name_type}:
//@return ${ret_name_type}:

//${function_name} TODO
//@receiver ${receiver_name}:
//@param ${param_name}:
//@return ${ret_name}:

//${function_name} TODO
//@receiver ${receiver_type}:
//@param ${param_type}:
//@return ${ret_type}:

/*${function_name} TODO
 *@receiver ${receiver_name_type}:
 *@param ${param_name_type}:
 *@return ${ret_name_type}: */

/*
 *${function_name} TODO
 *@receiver ${receiver_name_type}:
 *@param ${param_name_type}:
 *@return ${ret_name_type}: */

/**
 * ${function_name} TODO
 * *@receiver ${receiver_name_type}:
 * *@param ${param_name_type}:
 * *@return ${ret_name_type}:
 * <p>
 * *${function_name} TODO
 * *@receiver ${receiver_name_type}:
 * *@param ${param_name_type}:
 * *@return ${ret_name_type}:
 **/

/**
 **${function_name} TODO
 **@receiver ${receiver_name_type}:
 **@param ${param_name_type}:
 **@return ${ret_name_type}:
 **/
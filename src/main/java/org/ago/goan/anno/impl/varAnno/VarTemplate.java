package org.ago.goan.anno.impl.varAnno;

import org.ago.goan.anno.impl.funcAnno.goFunc;

public interface VarTemplate {

    String generate(goFunc func, String linePrefix, String selectedCode);

    // default template code
    String DefaultVarTemplate = "// ${var_name} TODO\n" +
            "// ${var_type} \n";

    String VAR_NAME = "${var_name}";

    String VAR_TYPE = "${var_type}";

}
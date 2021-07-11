package org.ago.goan.anno.impl.typeAnno;

import org.ago.goan.anno.impl.funcAnno.goFunc;

public interface TypeTemplate {

    String generate(goFunc func, String linePrefix, String selectedCode);

    // default template code
    String DefaultTypeTemplate = "// ${type_name} TODO\n";

    String TYPE_NAME = "${type_name}";
}

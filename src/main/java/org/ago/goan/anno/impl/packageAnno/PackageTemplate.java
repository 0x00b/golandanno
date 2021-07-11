package org.ago.goan.anno.impl.packageAnno;

import org.ago.goan.anno.impl.funcAnno.goFunc;

public interface PackageTemplate {

    String generate(goFunc func, String linePrefix, String selectedCode);

    // default template code
    String DefaultPackageTemplate = "// Package ${package_name} TODO\n";

    String PACKAGE_NAME = "${package_name}";
}
package org.ago.goan.anno.impl;

import org.ago.goan.anno.Context;

public interface GoAnnotator {
    DetectResult detect(Context ctx);

    String generate(Context ctx, DetectResult code, String selectedCode);
}

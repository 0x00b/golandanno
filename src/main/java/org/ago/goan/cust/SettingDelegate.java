package org.ago.goan.cust;

public interface SettingDelegate {
    String loadFuncTemplate();
    String loadPackageTemplate();
    String loadVarTemplate();
    String loadTypeTemplate();
    void submitFuncTemplate(String template);
    void submitPackageTemplate(String template);
    void submitVarTemplate(String template);
    void submitTypeTemplate(String template);
}

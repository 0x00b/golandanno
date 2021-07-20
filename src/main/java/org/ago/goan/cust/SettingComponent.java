package org.ago.goan.cust;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class SettingComponent extends DialogWrapper {
    private SetTemplateView setTemplateView = new SetTemplateView();

    public SettingComponent(org.ago.goan.cust.SettingDelegate delegate, @Nullable Project project) {
        super(project);
        if (null != delegate) {
            SettingComponent sc = this;
            setTemplateView.setDelegate(new org.ago.goan.cust.SettingDelegate() {

                @Override
                public String loadFuncTemplate() {
                    return delegate.loadFuncTemplate();
                }

                @Override
                public String loadPackageTemplate() {
                    return delegate.loadPackageTemplate();
                }

                @Override
                public String loadVarTemplate() {
                    return delegate.loadVarTemplate();
                }

                @Override
                public String loadTypeTemplate() {
                    return delegate.loadTypeTemplate();
                }

                @Override
                public void submitFuncTemplate(String template) {
                    delegate.submitFuncTemplate(template);
                    sc.close(0);
                }

                @Override
                public void submitPackageTemplate(String template) {

                }

                @Override
                public void submitVarTemplate(String template) {

                }

                @Override
                public void submitTypeTemplate(String template) {

                }
            }, sc);
        }

        setTitle("Edit GoComment Template");
        init();
    }


    @Override
    protected JComponent createNorthPanel() {
        return setTemplateView.initNorth();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return null;
    }

    @Override
    protected JComponent createSouthPanel() {
        return setTemplateView.initSouth();
    }

}

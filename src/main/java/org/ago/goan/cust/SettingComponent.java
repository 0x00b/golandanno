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
                public String loadTemplate() {
                    return delegate.loadTemplate();
                }

                @Override
                public void submitTemplate(String setting) {
                    delegate.submitTemplate(setting);
                    sc.close(0);
                }
            });
        }

        setTitle("Edit GoAn Template");
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

package org.ago.goan.action;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.ago.goan.anno.impl.funcAnno.Template;
import org.apache.commons.lang.StringUtils;
import org.ago.goan.cust.SettingComponent;
import org.ago.goan.cust.SettingDelegate;

public class GoAnSettingAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        SettingComponent settingComponent = new SettingComponent(new SettingDelegate() {
            @Override
            public String loadTemplate() {
                String data = PropertiesComponent.getInstance().getValue(Template.FUNC_TEMPLATE_KEY);
                if (StringUtils.isBlank(data)) {
                    data = Template.DefaultFuncTemplate;
                }

                return data;
            }
            @Override
            public void submitTemplate(String template) {
                if (template != null) {
                    PropertiesComponent.getInstance().setValue(Template.FUNC_TEMPLATE_KEY, template);
                }
            }
        }, e.getProject());

        settingComponent.setResizable(true);
        settingComponent.show();
    }
}
package org.ago.goan.action;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.ago.goan.anno.impl.funcAnno.Template;
import org.ago.goan.anno.impl.packageAnno.PackageTemplate;
import org.ago.goan.anno.impl.typeAnno.TypeTemplate;
import org.ago.goan.anno.impl.varAnno.VarTemplate;
import org.ago.goan.cust.Constant;
import org.apache.commons.lang.StringUtils;
import org.ago.goan.cust.SettingComponent;
import org.ago.goan.cust.SettingDelegate;

public class GoAnSettingAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        SettingComponent settingComponent = new SettingComponent(new SettingDelegate() {
            @Override
            public String loadFuncTemplate() {
                String data = PropertiesComponent.getInstance().getValue(Constant.FUNC_TEMPLATE_KEY);
                if (StringUtils.isBlank(data)) {
                    data = Template.DefaultFuncTemplate;
                }

                return data;
            }

            public String loadPackageTemplate() {
                String data = PropertiesComponent.getInstance().getValue(Constant.PACKAGE_TEMPLATE_KEY);
                if (StringUtils.isBlank(data)) {
                    data = PackageTemplate.DefaultPackageTemplate;
                }

                return data;
            }

            public String loadVarTemplate() {
                String data = PropertiesComponent.getInstance().getValue(Constant.VAR_TEMPLATE_KEY);
                if (StringUtils.isBlank(data)) {
                    data = VarTemplate.DefaultVarTemplate;
                }

                return data;
            }

            public String loadTypeTemplate() {
                String data = PropertiesComponent.getInstance().getValue(Constant.TYPE_TEMPLATE_KEY);
                if (StringUtils.isBlank(data)) {
                    data = TypeTemplate.DefaultTypeTemplate;
                }

                return data;
            }

            @Override
            public void submitFuncTemplate(String template) {
                if (template != null) {
                    PropertiesComponent.getInstance().setValue(Constant.FUNC_TEMPLATE_KEY, template);
                }
            }

            @Override
            public void submitPackageTemplate(String template) {
                if (template != null) {
                    PropertiesComponent.getInstance().setValue(Constant.PACKAGE_TEMPLATE_KEY, template);
                }
            }

            @Override
            public void submitVarTemplate(String template) {
                if (template != null) {
                    PropertiesComponent.getInstance().setValue(Constant.VAR_TEMPLATE_KEY, template);
                }
            }

            @Override
            public void submitTypeTemplate(String template) {
                if (template != null) {
                    PropertiesComponent.getInstance().setValue(Constant.TYPE_TEMPLATE_KEY, template);
                }
            }
        }, e.getProject());

        settingComponent.setResizable(true);
        settingComponent.show();
    }
}
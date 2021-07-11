package org.ago.goan.cust;

import com.intellij.ui.components.JBTabbedPane;

import javax.swing.*;
import java.awt.*;

public class SetTemplateView {
    private SettingDelegate delegate;
    private JTabbedPane tabbedPane = new JBTabbedPane();
    private JPanel south = new JPanel();

    private TextArea funcTextArea;
    private TextArea packageTextArea;
    private TextArea varTextArea;
    private TextArea typeTextArea;
    private SettingComponent settingComponent;

    public void setDelegate(SettingDelegate delegate, SettingComponent sc) {
        this.delegate = delegate;
        this.settingComponent = sc;
    }

    public JTabbedPane initNorth() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 1));
        funcTextArea = new TextArea();
        funcTextArea.setBounds(0, 0, 400, 500);
        if (null != this.delegate) {
            funcTextArea.setText(this.delegate.loadFuncTemplate());
        }
        panel.add(funcTextArea);
        tabbedPane.addTab("函数模版", panel);

        /////
        panel = new JPanel();
        panel.setLayout(new GridLayout(1, 1));
        packageTextArea = new TextArea();
        packageTextArea.setBounds(0, 0, 400, 500);
        if (null != this.delegate) {
            packageTextArea.setText(this.delegate.loadPackageTemplate());
        }
        panel.add(packageTextArea);
        tabbedPane.addTab("Package模版", panel);

        /////
        panel = new JPanel();
        panel.setLayout(new GridLayout(1, 1));
        varTextArea = new TextArea();
        varTextArea.setBounds(0, 0, 400, 500);
        if (null != this.delegate) {
            varTextArea.setText(this.delegate.loadVarTemplate());
        }
        panel.add(varTextArea);
        tabbedPane.addTab("变量模版", panel);

        /////
        panel = new JPanel();
        panel.setLayout(new GridLayout(1, 1));
        typeTextArea = new TextArea();
        typeTextArea.setBounds(0, 0, 400, 500);
        if (null != this.delegate) {
            typeTextArea.setText(this.delegate.loadTypeTemplate());
        }
        panel.add(typeTextArea);
        tabbedPane.addTab("类型定义模版", panel);


        return tabbedPane;
    }

    public JPanel initSouth() {
        JButton submit = new JButton("submit");
        submit.setHorizontalAlignment(SwingConstants.CENTER);
        submit.setVerticalAlignment(SwingConstants.CENTER);
        south.add(submit);

        submit.addActionListener(e -> {
            if (null != this.delegate) {
                String setting = funcTextArea.getText();
                this.delegate.submitFuncTemplate(setting);

                setting = packageTextArea.getText();
                this.delegate.submitPackageTemplate(setting);

                setting = varTextArea.getText();
                this.delegate.submitVarTemplate(setting);

                setting = typeTextArea.getText();
                this.delegate.submitTypeTemplate(setting);

            }
            settingComponent.close(0);
        });

        return south;
    }
}

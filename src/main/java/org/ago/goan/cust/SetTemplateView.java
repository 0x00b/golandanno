package org.ago.goan.cust;

import javax.swing.*;
import java.awt.*;

public class SetTemplateView {
    private SettingDelegate delegate;

    private JPanel north = new JPanel();
    private JPanel south = new JPanel();

    private TextArea settingTestArea = new TextArea();

    public void setDelegate(SettingDelegate delegate) {
        this.delegate = delegate;
    }

    public JPanel initNorth() {
        north.setLayout(new GridLayout(1, 1));

        settingTestArea = new TextArea();
        settingTestArea.setBounds(0, 0, 400, 500);
        if (null != this.delegate) {
            settingTestArea.setText(this.delegate.loadTemplate());
        }

        north.add(settingTestArea);
        return north;
    }

    public JPanel initSouth() {
        JButton submit = new JButton("submit");
        submit.setHorizontalAlignment(SwingConstants.CENTER);
        submit.setVerticalAlignment(SwingConstants.CENTER);
        south.add(submit);

        submit.addActionListener(e -> {
            String setting = settingTestArea.getText();
             if (null != this.delegate) {
                this.delegate.submitTemplate(setting);
            }
        });

        return south;
    }
}

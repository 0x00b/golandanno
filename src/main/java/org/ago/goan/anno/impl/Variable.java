package org.ago.goan.anno.impl;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Variable {
    String Name;
    String Type;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public static Variable ParseString(String code) {
        Variable variable = new Variable();
        if (null == code) {
            return variable;
        }
        code = StringUtils.trim(code);
        if (code == null) {
            return variable;
        }
        String[] receivers = code.split(" ");
        switch (receivers.length) {
            case 1:
                variable.setType(StringUtils.trim(receivers[0]));
                break;
            case 2:
                variable.setName(StringUtils.trim(receivers[0]));
                variable.setType(StringUtils.trim(receivers[1]));
                break;
        }

        return variable;
    }

    public static List<Variable> ParseArrayString(String code) {
        if (code == null) {
            return null;
        }
        String[] params = code.split(",");
        List<Variable> variables = new ArrayList<>();
        for (int i = 0; i < params.length; i++) {
            Variable vari = Variable.ParseString(params[i]);
            if (vari != null) {
                variables.add(vari);
            }
        }

        return variables;
    }
}


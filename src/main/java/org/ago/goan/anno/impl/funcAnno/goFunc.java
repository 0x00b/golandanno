package org.ago.goan.anno.impl.funcAnno;


import org.ago.goan.anno.impl.Variable;
import org.ago.goan.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class goFunc {
    //函数名
    String Name;
    //函数接收器
    Variable Receiver;
    //参数
    List<Variable> Parameters;
    //返回值
    List<Variable> Returns;

    public void autoComplete() {
        autoComplete(Parameters);
        autoComplete(Returns);
    }

    //自动补全参数的name和type
    //使用后面的type来作为前面无type的参数的type
    public static void autoComplete(List<Variable> list) {
        if (null == list || list.isEmpty()) {
            return;
        }
        String lastName = list.get(list.size() - 1).getName();
        if (StringUtils.isBlank(lastName)) {
            return;
        }
        String lastType = null;
        for (int i = list.size() - 1; i >= 0; i--) {
            if (list.get(i).getName() != null) {
                lastType = list.get(i).getType();
            } else if (lastType != null) {
                list.get(i).setName(list.get(i).getType());
                list.get(i).setType(lastType);
            }
        }
    }


    public Variable getReceiver() {
        if (Receiver == null) {
            return null;
        }
        if (StringUtils.isBlank(Receiver.getName()) && StringUtils.isBlank(Receiver.getType())) {
            return null;
        }
        return Receiver;
    }

    public void setReceiver(Variable receiver) {
        Receiver = receiver;
    }


    public List<Variable> getReturns() {
        return Returns;
    }

    public void addReturns(Variable ret) {
        if (ret == null) {
            return;
        }
        if (null == Returns) {
            Returns = new ArrayList<>();
        }
        Returns.add(ret);
    }

    public void setReturns(List<Variable> rets) {
        Returns = rets;
    }

    public List<Variable> getParameters() {
        return Parameters;
    }

    public void addParameters(Variable variable) {
        if (Parameters == null) {
            Parameters = new ArrayList<>();
        }
        Parameters.add(variable);
    }

    public void addParameters(String code) {
        Variable v = Variable.ParseString(code);
        if (v != null) {
            if (Parameters == null) {
                Parameters = new ArrayList<>();
            }
            Parameters.add(v);
        }
    }

    public void setParameters(List<Variable> parameters) {
        Parameters = parameters;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}

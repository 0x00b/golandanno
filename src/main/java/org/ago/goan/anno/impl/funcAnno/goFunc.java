package org.ago.goan.anno.impl.funcAnno;


import org.ago.goan.anno.impl.Variable;
import org.ago.goan.utils.StringUtils;

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
        return Receiver;
    }

    public void setReceiver(Variable receiver) {
        Receiver = receiver;
    }


    public List<Variable> getReturns() {
        return Returns;
    }

    public void setReturns(List<Variable> returns) {
        Returns = returns;
    }

    public List<Variable> getParameters() {
        return Parameters;
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

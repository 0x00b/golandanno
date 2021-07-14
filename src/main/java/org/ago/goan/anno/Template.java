package org.ago.goan.anno;


import org.ago.goan.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Template {

    // current date
    public final static String DATE = "${date}";
    public final static String GIT_NAME = "${git_name}";

    public String date;
    public String gitName;

}
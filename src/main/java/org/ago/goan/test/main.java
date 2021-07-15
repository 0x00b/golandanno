package org.ago.goan.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;

import org.ago.goan.anno.impl.funcAnno.TemplateImpl;
import org.ago.goan.anno.impl.funcAnno.funcAnnotatorImpl;
import org.ago.goan.anno.impl.typeAnno.typeAnnotatorImpl;
import org.ago.goan.anno.impl.varAnno.varAnnotatorImpl;

public class main {

    public static void main1(String[] args) {
        TemplateImpl t = new TemplateImpl();


        String[] templates = new String[]{
                t.DefaultFuncTemplate,

                "/*${function_name} TODO\n" +
                        "*@receiver ${receiver_name_type}: \n" +
                        "*@param ${param_name_type}: \n" +
                        "*@return ${ret_name_type}: */\n",

                "/*\n*${function_name} TODO\n" +
                        " **@receiver ${receiver_name_type}: \n" +
                        " **@param ${param_name_type}: \n" +
                        " **@return ${ret_name_type}: */\n",

                "/**\n*${function_name} TODO\n" +
                        " **@receiver ${receiver_name_type}: \n" +
                        " **@param ${param_name_type}: \n" +
                        " **@return ${ret_name_type}: \n" +
                        "*/",
        };

        for (int j = 0; j < templates.length; j++) {
            System.out.println("==============================================");
//            List<TemplateImpl.LineTemplate> list = t.parse(templates[j]);
//            System.out.println(t.getAnnotateType());
//            for (int i = 0; i < list.size(); i++) {
//                System.out.println(list.get(i).keys + ":" + list.get(i).lineTemplate);
//            }
//            System.out.println("==============================================");
        }

    }

    public static void main2(String[] args) {
//        Matcher funcRegexMatcher = funcAnnotatorImpl.funcRegexPattern.matcher();


        String[] code = new String[]{
                "SomeFunc()",
                "SomeFunc(t1 Type)",
                "SomeFunc() error",
                "SomeFunc(t1 Type) error",
                "SomeFunc(t1 Type,t2 Type) error",
                "SomeFunc(t1 Type,\n t2 Type) error",
                "func SomeFunc()",
                "func SomeFunc(t1 Type)",
                "func SomeFunc(t1 Type) error",
                "func SomeFunc(t1 interface{}) interface{} {",
                "func SomeFunc(t1 interface{}) (i interface{}) {",
                "func SomeFunc(t1 interface{}, i2 interface{}) (i interface{}, i3 interface{}) {",
                "func SomeFunc(t1 Type,t2 Type) (e error){",
                "func (xx *sss) SomeFunc(t1 Type,\n t2 *Type) (e error, ddd *DD) {",
                "func (xx *sss) SomeFunc(t1 Type,\n t2 *Type) (e error,\n ddd *DD) {",
                "func (f DefaultResponseFormatter) Success(c context.Context, in interface{}) (Header,b ProtoType, interface{}) {\n",
                "func (f DefaultResponseFormatter) Success(c context.Context, in interface{}) (a Header, context.Context,c interface{}) {\n",

                //Not Normal
                "SomeFunc(sfa#dfa)",
                "Some=Func(sfa#dfa)",
                "Some=Func(sfadfa)",
                "Some=Func(sfadfa){",
                "func Some=Func(sfadfa){",
                "func SomeFunc(t1 interface{xxx}) (i interface{xxx}) {",
        };

        for (int i = 0; i < code.length; i++) {
            System.out.println("==============================================");
            Matcher funcRegexMatcher = funcAnnotatorImpl.funcRegexPattern.matcher(code[i]);

            System.out.println(code[i]);
            if (funcRegexMatcher.find()) {
                for (int j = 0; j <= funcRegexMatcher.groupCount(); j++) {
                    System.out.println(funcRegexMatcher.group(j));
                }
                System.out.println("==============================================");
            }
        }
        code = new String[]{
                "type XXX struct()",
                "type  XXX  struct",
                "type  XXX  struct {",
                "\n\ttype  ss XXX",
                "type Foo func()",
                "type Foo func SomeFunc()",
                "type Foo func()",
                "type Foo func(t1 Type)",
                "type Foo func(t1 Type) error",
                "type Foo func(t1 interface{}) interface{} {",
                "type Foo func (t1 interface{}) (i interface{}) ",
                "type Foo func(t1 interface{}, i2 interface{}) (i interface{}, i3 interface{}) {",
                "type Foo func (t1 Type,t2 Type) (e error){",
                "type Foo func (c context.Context, in interface{}) (Header,b ProtoType, interface{}) {\n",
                "type Foo func(t1 Type,\n t2 *Type) (e error, ddd *DD) {",
                "type Foo func (xx *sss) SomeFunc(t1 Type,\n t2 *Type) (e error,\n ddd *DD) {",
                "type Foo func (f DefaultResponseFormatter) Success(c context.Context, in interface{}) (a Header, context.Context,c interface{}) {\n",
        };

        for (int i = 0; i < code.length; i++) {
            System.out.println("==============================================");
            Matcher regexMatcher = funcAnnotatorImpl.typeFuncRegexPattern.matcher(code[i]);

            System.out.println(code[i]);
            if (regexMatcher.find()) {
                for (int j = 0; j <= regexMatcher.groupCount(); j++) {
                    System.out.println(regexMatcher.group(j));
                }
                System.out.println("==============================================");
            }
        }
    }

    public static void main3(String[] args) {

        String[] code = new String[]{
                "type XXX struct()",
                "type  XXX  struct",
                "type  XXX  struct {",
                "\n\ttype  ss XXX",
        };

        for (int i = 0; i < code.length; i++) {
            System.out.println("==============================================");
            Matcher regexMatcher = typeAnnotatorImpl.typeRegexPattern.matcher(code[i]);

            System.out.println(code[i]);
            if (regexMatcher.find()) {
                for (int j = 0; j <= regexMatcher.groupCount(); j++) {
                    System.out.println(regexMatcher.group(j));
                }
                System.out.println("==============================================");
            }
        }
    }

    public static void main(String[] args) {

        try {
            Process p = Runtime.getRuntime().exec("git config user.name");
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] code = new String[]{
                " XXX  ",
                " XXX  foo",
                "var  XXX  foo = 11",
                " XXX  foo = 11",
                "var XXX struct{}",
                "  XXX  interface{}",
                "  XXX1,XXX2  ",
                "  XXX1, XXX2  ",
                "  XXX1,XXX2  interface{}",
        };

        for (int i = 0; i < code.length; i++) {
            System.out.println("==============================================");
            Matcher regexMatcher = varAnnotatorImpl.varRegexPattern.matcher(code[i]);

            System.out.println(code[i]);
            if (regexMatcher.find()) {
                for (int j = 0; j <= regexMatcher.groupCount(); j++) {
                    System.out.println(regexMatcher.group(j));
                }
                System.out.println("==============================================");
            }
        }
    }

}

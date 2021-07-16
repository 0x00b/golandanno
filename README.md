# GoAn (Goland Annotation)

<!-- Plugin description -->
**GoAn** is a plugin for goland, auto generate for golang function, variable, struct comments.
<br/>

## <t1>How to use</t1>
+ 1.control + commond + /  (for windows: control + alt + /)  
+ 2.Right click -> Generate -> GoAn
+ 3.Edit comment template
  
## config template
+ 1.Select "Tools - GoAn Setting"
+ 2.Edit template

<br/>
推荐使用默认注释，满足Golang godoc注释规范，满足golint默认扫描规则。<br/>

[点击查看使用介绍](https://github.com/0x00b/golandanno/blob/main/README.md)


![](https://github.com/0x00b/golandanno/tree/main/src/main/resources/intro.gif)


<!-- Plugin description end -->

Fork from **[IntelliJ Platform Plugin Template](https://github.com/JetBrains/intellij-platform-plugin-template)** .
IntelliJ Platform Plugin Template is a repository that provides a pure boilerplate template to make it easier to create a new plugin project (check the [Creating a repository from a template][gh:template] article).

# Getting started

## How to install
1.goland plugins marketplace(search GoAn)

### special tag, represent beginning of a special line.
* @receiver ： golang function receiver
* @param ： golang function parameter 
* @return ： golang function return parameter
* @update ： update tag, when update annotation, will add one line

### support variable
```go
func (r receiver)Foo(i interface{}) (e error)
```
* ${func_name} : function name is "Foo".
* ${receiver_name} : will be replaced by "r".
* ${receiver_type} : will be replaced by "receiver".
* ${receiver_name_type} :  will be replaced by "r receiver".
* ${param_name} : "i"
* ${param_type} : "interface{}"
* ${param_name_type} : "i interface{}"
* ${return_name} : "e"
* ${return_type} : "error"
* ${return_name_type} : "e error"
* ${package_name} : package name
* ${type_name} : type Int int64,  ${type_name} is "Int"
* ${var_name} : var n int, ${var_name} is "n"
* ${var_type} : var n int, ${var_type} is "int"
* ${date} : date
* ${git_name}: git config name

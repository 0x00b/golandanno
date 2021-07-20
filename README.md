# GoComment (Goland Comment)

<!-- Plugin description -->
**GoComment** is a plugin for goland, auto generate for golang function, variable, struct comments. 
使用默认模版可以生成满足golint要求的注释。
<br/>

## <t1>How to use</t1>
+ **control + command + / (For windows: control + alt + /)**
+ 2.Right click -> Generate -> GoComment
+ 3.Edit comment template
  
## config template
+ 1.Select "Tools - GoComment Setting"
+ 2.Edit template

<br/>
推荐使用默认注释，满足Golang godoc注释规范，满足golint默认扫描规则。<br/>

[点击查看使用介绍](https://github.com/0x00b/golandanno/blob/main/README.md)

![](https://raw.githubusercontent.com/0x00b/golandanno/main/src/main/resources/intro.gif)

使用godoc查看注释效果如下：

```shell
godoc -http=localhost:6060
```
![](https://raw.githubusercontent.com/0x00b/golandanno/main/src/main/resources/img_1.png)

![](https://raw.githubusercontent.com/0x00b/golandanno/main/src/main/resources/godoc.gif)


<!-- Plugin description end -->

Fork from **[IntelliJ Platform Plugin Template](https://github.com/JetBrains/intellij-platform-plugin-template)** .
IntelliJ Platform Plugin Template is a repository that provides a pure boilerplate template to make it easier to create a new plugin project (check the [Creating a repository from a template][gh:template] article).

# Getting started

## How to install
1.goland plugins marketplace(search GoComment)

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

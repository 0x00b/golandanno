# GoAn (Goland Annotation)

[![official JetBrains project](https://jb.gg/badges/official.svg)][jb:confluence-on-gh]
[![Twitter Follow](https://img.shields.io/twitter/follow/JBPlatform?style=flat)][jb:twitter]
[![Build](https://github.com/JetBrains/intellij-platform-plugin-template/workflows/Build/badge.svg)][gh:build]
[![Slack](https://img.shields.io/badge/Slack-%23intellij--platform--plugin--template-blue)][jb:slack]

<!-- Plugin description -->
**GoAn** is a plugin for goland, auto generate for golang function, variable, struct comments.
<!-- Plugin description end -->

Fork from **[IntelliJ Platform Plugin Template](https://github.com/JetBrains/intellij-platform-plugin-template)** .
IntelliJ Platform Plugin Template is a repository that provides a pure boilerplate template to make it easier to create a new plugin project (check the [Creating a repository from a template][gh:template] article).

## Getting started



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
##插件使用指南

### 1.环境配置
在 设置-项目结构 里配置正确的SDK版本
SDK版本：Intellij平台插件SDK，选择内部java平台为11的版本
语言级别：11

### 2.运行
在gradle构建完成后运行沙盒演示
点击右侧侧边栏的Gradle，在目录中找到/gradlePlugin/Tasks/intellij/runIde
点击runIde即可运行，若一切正常将会跳出Pycharm的初始界面

### 3.使用
将光标移动至函数体内，使用快捷键Shift + C 或右键选择generate->generate comment，即可看到侧边栏界面出现

点击apply进行插入操作，若已有注释，可选择直接在其后插入、替换插入或撤销插入操作；点击feedback可对生成的注释进行评价
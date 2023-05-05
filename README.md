##iCommenter插件使用指南

### 1.环境配置
#### 1.1 插件开发调试时使用
在 设置-项目结构 里配置正确的SDK版本

    SDK版本：Intellij平台插件SDK，选择内部java平台为11的版本
    语言级别：11

#### 1.2 正常使用
下载代码文件后在 设置-插件 中选择从磁盘安装插件，将文件导入即可

可跳过第二部分-运行直接使用插件

### 2.运行
在gradle构建完成后运行沙盒演示

点击右侧侧边栏的Gradle，在目录中找到/gradlePlugin/Tasks/intellij/runIde

点击runIde即可运行，若一切正常将会跳出Pycharm的初始界面

关闭Pycharm后会结束runIde的调用

### 3.插件使用
在Pycharm中打开项目后，选择好目标函数

将光标移动至函数体内，使用快捷键Shift + C 或右键选择generate->generate comment，确认生成注释后即可看到侧边栏界面出现

点击apply进行插入操作，若已有注释，可选择直接在其后插入、替换插入或撤销插入操作；

可对生成的注释进行评价，点击Submit即可提交评价

点击下方的Feedback for Plugin即可对插件工具的使用进行评价
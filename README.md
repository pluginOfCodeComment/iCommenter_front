iCommenter是一个应用于PyCharm的智能化代码注释生成插件

## 安装
1.下载gradlePlugin-1.0-SNAPSHOT.zip

2.打开PyCharm，选择
- Windows：`File` -> `Settings` -> `Plugins`;

![setting](./pic/step1.png)
- Mac：`IntelliJ IDEA` -> `Preferences` -> `Plugins`;

3.选择 `Install Plugin from Disk`

![plugin](./pic/step2.png)

4.找到zip文件，并将其导入

5.重启PyCharm以激活插件
![img.png](./pic/step3.png)

## 使用
1.在PyCharm中打开项目后，选择你想要为其生成注释的目标函数

2.将光标移动至函数体内，使用快捷键`Shift + C` 或右键选择`Generate->generate comment`
![plugin](./pic/app1.png)

3.确认生成注释后会启动插件，等待片刻后会跳出侧边栏界面，上方显示了插件生成的注释结果

4.点击apply可调用插件自动完成插入操作，若已有注释，可自行选择直接在其后插入、替换插入或撤销插入；
![plugin](./pic/app2.png)
![plugin](./pic/app4.png)
示例图展示的是用户选择在其后插入的结果，若选择替换插入，则会将原注释完全替换为插件生成的注释

5.在侧边栏下方可对生成的注释进行评价，点击Submit即可提交评价
![plugin](./pic/app3.png)
6.点击页面右下角的`Feedback for Plugin`可提交对于插件工具的反馈

## 源码使用

### 环境配置
1.下载源码后，打开Intellij IDEA导入项目，选择`File` -> `Project Structure`

2.在`Project Settings` -> `Project`页面
- 配置SDK为IntelliJ Platform Plugin SDK，若本地没有可点击`Add SDK`下载，选择对应JDK11的版本
- 配置语言级别为11

### 运行
1.使用gradle构建项目

2.点击右侧侧边栏的Gradle，选择`Tasks` -> `intellij` -> `runIde`

3.开始运行，若一切正常将会跳出PyCharm（已配置好插件）的初始界面，可按照上文的`使用`正常使用插件iCommenter

4.关闭Pycharm后会结束runIde的调用
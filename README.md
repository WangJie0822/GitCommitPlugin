# GitCommitPlugin

![Build](https://github.com/WangJie0822/GitCommitPlugin/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)

## Template ToDo list
- [x] Create a new [IntelliJ Platform Plugin Template][template] project.
- [ ] Verify the [pluginGroup](/gradle.properties), [plugin ID](/src/main/resources/META-INF/plugin.xml) and [sources package](/src/main/kotlin).
- [ ] Review the [Legal Agreements](https://plugins.jetbrains.com/docs/marketplace/legal-agreements.html).
- [ ] [Publish a plugin manually](https://plugins.jetbrains.com/docs/intellij/publishing-plugin.html?from=IJPluginTemplate) for the first time.
- [ ] Set the Plugin ID in the above README badges.
- [ ] Set the [Deployment Token](https://plugins.jetbrains.com/docs/marketplace/plugin-upload.html).
- [ ] Click the <kbd>Watch</kbd> button on the top of the [IntelliJ Platform Plugin Template][template] to be notified about releases containing new features and fixes.

<!-- Plugin description -->
        按照以下默认模板创建 Commit 信息:</p>

       type(scope):subject
       BLANK LINE
       body
       BLANK LINE
       footer

        <p>当然你可以在项目根目录下添加 git_template_config.json 文件对修改类型以及弹窗显示文本进行自定义</p>
        {
            "dialog": {
                 "typeOfChange": "修改类型",
                 "scopeOfThisChange": "修改范围",
                 "shortDescription": "简单说明",
                 "longDescription": "详细说明",
                 "breakingChanges": "重大改变",
                 "closedIssues": "已解决问题",
                 "wrapText": "是否72位自动换行？",
                 "skipCI": "是否跳过CI构建？"
               },
            "changeTypeList": [
                {
                    "action": "[代码新增]",
                    "title": "代码新增",
                    "description": "新增功能、补充功能新增代码"
                },
                {
                    "action": "[代码修改]",
                    "title": "代码修改",
                    "description": "新增功能、补充功能修该代码"
                }
            ]
        }
<!-- Plugin description end -->

## Installation

- Using IDE built-in plugin system:
  
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "GitCommitPlugin"</kbd> >
  <kbd>Install Plugin</kbd>
  
- Manually:

  Download the [latest release](https://github.com/WangJie0822/GitCommitPlugin/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template

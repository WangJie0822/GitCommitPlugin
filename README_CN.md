# VcsHelperPlugin

![Build](https://github.com/WangJie0822/VcsHelperPlugin/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/cn.wj.plugin.vcs.svg)](https://plugins.jetbrains.com/plugin/cn.wj.plugin.vcs)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/cn.wj.plugin.vcs.svg)](https://plugins.jetbrains.com/plugin/cn.wj.plugin.vcs)

<!-- Plugin description -->
你可用通过这个插件按照以下模板创建 commit 信息:

```
<type>(<scope>): <subject>
<BLANK LINE>
<body>
<BLANK LINE>
<footer>
```

你还可以在项目的根目录下添加 **commit_template.json** 文件来修改默认配置:
```json
{
  "label": {
    "typeOfChange": "修改类型",
    "scopeOfChange": "影响范围",
    "shortDescription": "简单说明",
    "longDescription": "详细说明",
    "breakingChanges": "重大改变",
    "closedIssues": "关闭的问题"
  },
  "keywords": {
    "wrapWords": true,
    "maxLineLength": 70,
    "scopeWrapperStart": "(",
    "scopeWrapperEnd": ")",
    "descriptionSeparator": ": ",
    "breakingChanges": "BREAKING CHANGES: ",
    "breakingChangesEmpty": "",
    "closedIssues": "Closes: ",
    "closedIssuesSeparator": ",",
    "closedIssuesEmpty": ""
  },
  "changeTypes": [
    {
      "title": "Styles",
      "action": "STYLE",
      "description": "Changes that do not affect the meaning of the code (white-space, formatting, missing semi-colons, etc)"
    },
    {
      "title": "Code Refactoring",
      "action": "REFACTOR",
      "description": "A code change that neither fixes a bug nor adds a feature"
    }
  ]
}
```
<!-- Plugin description end -->

## Installation

- Using IDE built-in plugin system:
  
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "VcsHelper"</kbd> >
  <kbd>Install Plugin</kbd>
  
- Manually:

  Download the [latest release](https://github.com/WangJie0822/VcsHelper/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template

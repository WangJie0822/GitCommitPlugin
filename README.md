# GitCommitPlugin

![Build](https://github.com/WangJie0822/GitCommitPlugin/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)

<!-- Plugin description -->

This plugin allows to create a commit message with the following template:

```
<type>(<scope>): <subject>
<BLANK LINE>
<body>
<BLANK LINE>
<footer>
```

You can also add **commit_template.json** files to the project root directory to modify the default configuration:
```json
{
  "label": {
    "typeOfChange": "Type of change",
    "scopeOfChange": "Scope of change",
    "shortDescription": "short description",
    "longDescription": "long description",
    "breakingChanges": "Breaking changes",
    "closedIssues": "Closed issues"
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
      "description": "新增功能、补充功能修该代码"
    },
    {
      "action": "[缺陷修复]",
      "title": "缺陷修复",
      "description": "A code change that neither fixes a bug nor adds a feature"
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

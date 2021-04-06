# GitCommitPlugin

![Build](https://github.com/WangJie0822/VcsHelperPlugin/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/cn.wj.plugin.vcs.svg)](https://plugins.jetbrains.com/plugin/cn.wj.plugin.vcs)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/cn.wj.plugin.vcs.svg)](https://plugins.jetbrains.com/plugin/cn.wj.plugin.vcs)

<!-- Plugin description -->

This plugin allows to create a commit message with the following template:

```
<type>(<scope>): <subject>
<BLANK LINE>
<body>
<BLANK LINE>
<footer>
```
You can also customize your own build rules through <kbd>Settings/Preferences</kbd> > <kbd>Tools</kbd> > <kbd>VcsHelper</kbd>

Or add the `*.json` configuration file like this:
```json
{
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

  Download the [latest release](https://github.com/WangJie0822/VcsHelperPlugin/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template

package cn.wj.plugin.gitcommit

data class DialogEntity(
        var typeOfChange: String = "Type of change",
        var scopeOfThisChange: String = "Scope of this change",
        var shortDescription: String = "Short description",
        var longDescription: String = "Long description",
        var breakingChanges: String = "Breaking changes",
        var closedIssues: String = "Closed issues",
        var wrapText: String = "Wrap at 72 characters?",
        var skipCI: String = "Skip CI?"
)
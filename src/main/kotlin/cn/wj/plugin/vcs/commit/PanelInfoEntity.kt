package cn.wj.plugin.vcs.commit

import kotlinx.serialization.Serializable

/**
 * 提交规范面板数据
 *
 * > [王杰](mailto:w15555650921@gmail.com) 创建于 20201/3/19
 */
@Serializable
data class PanelInfoEntity(
    val keywords: KeywordsEntity = KeywordsEntity(),
    val changeTypes: ArrayList<ChangeTypeEntity> = arrayListOf(
        ChangeTypeEntity("FEAT", "Features", "A new feature"),
        ChangeTypeEntity("FIX", "Bug Fixes", "A bug fix"),
        ChangeTypeEntity("DOCS", "Documentation", "Documentation only changes"),
        ChangeTypeEntity(
            "Styles", "STYLE",
            "Changes that do not affect the meaning of the" +
                " code (white-space, formatting, missing semi-colons, etc)"
        ),
        ChangeTypeEntity(
            "Code Refactoring", "REFACTOR",
            "A code change that neither " +
                "fixes a bug nor adds a feature"
        ),
        ChangeTypeEntity(
            "Performance Improvements", "PERF",
            "A code change that " +
                "improves performance"
        ),
        ChangeTypeEntity("Tests", "TEST", "Adding missing tests or correcting existing tests"),
        ChangeTypeEntity(
            "Builds", "BUILD",
            "Changes that affect the build system or " +
                "external dependencies (example scopes: gulp, broccoli, npm)"
        ),
        ChangeTypeEntity(
            "Continuous Integrations", "CI",
            "Changes to our CI configuration" +
                " files and scripts (example scopes: Travis, Circle, BrowserStack, SauceLabs)"
        ),
        ChangeTypeEntity("Chores", "CHORE", "Other changes that don't modify src or test files"),
        ChangeTypeEntity("Reverts", "REVERT", "Reverts a previous commit")
    )
)

/**
 * 关键字数据
 */
@Serializable
data class KeywordsEntity(
    val wrapWords: Boolean = true,
    val maxLineLength: Int = 70,
    val scopeWrapperStart: String = "(",
    val scopeWrapperEnd: String = ")",
    val descriptionSeparator: String = ": ",
    val breakingChanges: String = "BREAKING CHANGES: ",
    val breakingChangesEmpty: String = "",
    val closedIssues: String = "Closes ",
    val closedIssuesSeparator: String = ",",
    val closedIssuesEmpty: String = ""
)

/**
 * 修改类型数据
 */
@Serializable
data class ChangeTypeEntity(
    val title: String = "",
    val action: String = "",
    val description: String = ""
) {
    override fun toString(): String {
        return "$title - $action - $description"
    }

    fun display(): String {
        return "$title - $description"
    }
}

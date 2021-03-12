package cn.wj.plugin.gitcommit

/**
 * 修改类型
 *
 * @param action 类型动作
 * @param title 标题
 * @param description 说明
 */
data class ChangeType(
    val action: String,
    val title: String,
    val description: String
) {

    fun label(): String {
        return action
    }

    override fun toString(): String {
        return "%s - %s".format(title, description)
    }

    companion object {
        val DEFAULT_LIST = arrayListOf(
            ChangeType("FEAT", "Features", "A new feature"),
            ChangeType("FIX", "Bug Fixes", "A bug fix"),
            ChangeType("DOCS", "Documentation", "Documentation only changes"),
            ChangeType(
                "STYLE", "Styles",
                "Changes that do not affect the meaning of the" +
                    " code (white-space, formatting, missing semi-colons, etc)"
            ),
            ChangeType(
                "REFACTOR", "Code Refactoring",
                "A code change that neither " +
                    "fixes a bug nor adds a feature"
            ),
            ChangeType(
                "PERF", "Performance Improvements",
                "A code change that " +
                    "improves performance"
            ),
            ChangeType("TEST", "Tests", "Adding missing tests or correcting existing tests"),
            ChangeType(
                "BUILD", "Builds",
                "Changes that affect the build system or " +
                    "external dependencies (example scopes: gulp, broccoli, npm)"
            ),
            ChangeType(
                "CI", "Continuous Integrations",
                "Changes to our CI configuration" +
                    " files and scripts (example scopes: Travis, Circle, BrowserStack, SauceLabs)"
            ),
            ChangeType("CHORE", "Chores", "Other changes that don't modify src or test files"),
            ChangeType("REVERT", "Reverts", "Reverts a previous commit")
        )
    }
}

package cn.wj.plugin.vcs.constants

import cn.wj.plugin.vcs.entity.ChangeTypeEntity
import cn.wj.plugin.vcs.tools.toJsonString

/** 默认提交模板配置文件名 */
const val DEFAULT_CONFIG_FILE_NAME = "commit_template.json"

/** 项目路径占位符 */
const val PROJECT_PATH_PLACEHOLDER = "\${projectDir}"

/** 是否使用 json 文件配置 */
const val DEFAULT_USE_JSON_CONFIG = true

/** 配置文件路径 */
const val DEFAULT_JSON_CONFIG_PATH = "$PROJECT_PATH_PLACEHOLDER/$DEFAULT_CONFIG_FILE_NAME"

/** 是否自动换行 */
const val DEFAULT_TEXT_AUTO_WRAP = true

/** 单行长度 */
const val DEFAULT_AUTO_WRAP_LENGTH = "70"
val DEFAULT_AUTO_WRAP_LENGTH_INT = DEFAULT_AUTO_WRAP_LENGTH.toInt()

/** 影响范围包裹符号左 */
const val DEFAULT_SCOPE_WRAPPER_START = "("

/** 影响范围包裹符号右 */
const val DEFAULT_SCOPE_WRAPPER_END = ")"

/** 简单描述分隔符 */
const val DEFAULT_DESCRIPTION_SEPARATOR = ": "

/** 重大改变关键字 */
const val DEFAULT_BREAKING_CHANGES = "BREAKING CHANGES: "

/** 重大改变为空显示 */
const val DEFAULT_BREAKING_CHANGES_WHEN_EMPTY = ""

/** 关闭的问题关键字 */
const val DEFAULT_CLOSED_ISSUES = "CLOSED: "

/** 关闭的问题分隔符 */
const val DEFAULT_CLOSED_ISSUES_SEPARATOR = ","

/** 关闭的问题为空显示 */
const val DEFAULT_CLOSED_ISSUES_WHEN_EMPTY = ""

/** 修改类型列表 */
val DEFAULT_TYPE_OF_CHANGE_LIST: String
    get() = arrayListOf(
        ChangeTypeEntity("Features", "FEAT", "A new feature"),
        ChangeTypeEntity("Bug Fixes", "FIX", "A bug fix"),
        ChangeTypeEntity("Documentation", "DOCS", "Documentation only changes"),
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
    ).toJsonString()

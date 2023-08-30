package cn.wj.plugin.vcs.constants

import cn.wj.plugin.vcs.entity.ChangeTypeEntity
import cn.wj.plugin.vcs.tools.toJsonString

/** 缓存数据 key - 提交消息 */
const val STORAGE_KEY_COMMIT_MESSAGE = "storage_key_commit_message"

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

/** 默认字体 */
const val DEFAULT_FONT_NAME = ""

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
const val DEFAULT_CLOSED_ISSUES = "closes issue: "

/** 关闭的问题分隔符 */
const val DEFAULT_CLOSED_ISSUES_SEPARATOR = ","

/** 关闭的问题为空显示 */
const val DEFAULT_CLOSED_ISSUES_WHEN_EMPTY = ""

/** 修改类型列表 */
val DEFAULT_TYPE_OF_CHANGE_LIST: ArrayList<ChangeTypeEntity>
    get() = arrayListOf(
            ChangeTypeEntity("Features", "feat", "A new feature"),
            ChangeTypeEntity("Bug Fixes", "fix", "A bug fix"),
            ChangeTypeEntity("Documentation", "docs", "Documentation only changes"),
            ChangeTypeEntity(
                    "Styles", "style",
                    "Changes that do not affect the meaning of the" +
                            " code (white-space, formatting, missing semi-colons, etc)"
            ),
            ChangeTypeEntity(
                    "Code Refactoring", "refactor",
                    "A code change that neither " +
                            "fixes a bug nor adds a feature"
            ),
            ChangeTypeEntity(
                    "Performance Improvements", "perf",
                    "A code change that " +
                            "improves performance"
            ),
            ChangeTypeEntity("Tests", "test", "Adding missing tests or correcting existing tests"),
            ChangeTypeEntity(
                    "Builds", "build",
                    "Changes that affect the build system or " +
                            "external dependencies (example scopes: gulp, broccoli, npm)"
            ),
            ChangeTypeEntity(
                    "Continuous Integrations", "ci",
                    "Changes to our CI configuration" +
                            " files and scripts (example scopes: Travis, Circle, BrowserStack, SauceLabs)"
            ),
            ChangeTypeEntity("Chores", "chore", "Other changes that don't modify src or test files"),
            ChangeTypeEntity("Reverts", "revert", "Reverts a previous commit")
    )
val DEFAULT_TYPE_OF_CHANGE_LIST_JSON: String
    get() = DEFAULT_TYPE_OF_CHANGE_LIST.toJsonString()

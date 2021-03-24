package cn.wj.plugin.vcs.commit

import com.intellij.openapi.project.Project
import org.apache.commons.lang.StringUtils
import org.apache.commons.lang.WordUtils

/**
 * 提交信息
 *
 * @param typeOfChange 修改类型
 * @param scopeOfChange 修改范围
 * @param shortDescription 简单说明
 * @param longDescription 详细说明
 * @param breakingChanges 重大修改
 * @param closedIssues 关闭的问题
 */
data class CommitMessageEntity(
    var typeOfChange: ChangeTypeEntity = ChangeTypeEntity("", "", ""),
    var scopeOfChange: String = "",
    var shortDescription: String = "",
    var longDescription: String = "",
    var breakingChanges: String = "",
    var closedIssues: String = ""
) {

    fun getCommitString(project: Project?): String {
        val keywords = ConfigHelper.loadConfig(project).keywords
        return with(StringBuilder()) {
            // 修改类型
            append(typeOfChange.action)

            // 修改范围
            if (scopeOfChange.isNotBlank()) {
                append("${keywords.scopeWrapperStart}$scopeOfChange${keywords.scopeWrapperEnd}")
            }

            // 简单说明
            append("${keywords.descriptionSeparator}$shortDescription")

            // 详细描述
            if (longDescription.isNotBlank()) {
                appendln()
                appendln()
                append(
                    if (keywords.wrapWords) {
                        WordUtils.wrap(longDescription, keywords.maxLineLength)
                    } else {
                        longDescription
                    }
                )
            }

            // 重大改变
            if (breakingChanges.isNotBlank()) {
                appendln()
                appendln()
                append(
                    if (keywords.wrapWords) {
                        WordUtils.wrap("${keywords.breakingChanges}$breakingChanges", keywords.maxLineLength)
                    } else {
                        "${keywords.breakingChanges}$breakingChanges"
                    }
                )
            } else {
                if (keywords.breakingChangesEmpty.isNotBlank()) {
                    appendln()
                    appendln()
                    append("${keywords.breakingChanges}${keywords.breakingChangesEmpty}")
                }
            }

            // 关闭的问题
            if (closedIssues.isNotBlank()) {
                appendln()
                for (closedIssue in closedIssues.split(keywords.closedIssuesSeparator)) {
                    appendln()
                    append("${keywords.closedIssues}${formatClosedIssue(closedIssue)}")
                }
            } else {
                if (keywords.closedIssuesEmpty.isNotBlank()) {
                    appendln()
                    appendln()
                    append("${keywords.closedIssues}${keywords.closedIssuesEmpty}")
                }
            }

            toString()
        }
    }

    private fun formatClosedIssue(closedIssue: String): String {
        val trimmed = closedIssue.trim { it <= ' ' }
        return (if (StringUtils.isNumeric(trimmed)) "#" else "") + trimmed
    }

    companion object {

        fun parse(message: String, project:Project?): CommitMessageEntity {
            val result = CommitMessageEntity()
            val strings = message.split("\n".toRegex()).toTypedArray()
            if (strings.isEmpty()) {
                return result
            }

            val config = ConfigHelper.loadConfig(project)
            val keywords = config.keywords
            val typeList = config.changeTypes

            val line1 = strings[0]
            if (!line1.contains(keywords.descriptionSeparator)) {
                return result
            }

            // 修改类型
            val changeTypeStr = if (line1.contains(keywords.scopeWrapperStart)) {
                // 包含范围
                result.scopeOfChange = line1.substring(
                    line1.indexOf(keywords.scopeWrapperStart) + 1,
                    line1.indexOf(keywords.scopeWrapperEnd)
                )
                    .trim { it <= ' ' }
                line1.substring(0, line1.indexOf(keywords.scopeWrapperStart))
            } else {
                // 没有范围
                line1.substring(0, line1.indexOf(keywords.descriptionSeparator))
            }
            // 修改类型
            result.typeOfChange = typeList.firstOrNull {
                it.action == changeTypeStr
            } ?: typeList.first()

            // 简单说明
            result.shortDescription = line1.substring(line1.indexOf(keywords.descriptionSeparator) + 1, line1.length)
                .trim { it <= ' ' }
            if (strings.size < 2) {
                return result
            }

            // 详细说明
            var pos = 1
            val sb = StringBuilder()
            while (pos < strings.size) {
                val lineString = strings[pos]
                if (lineString.startsWith(keywords.breakingChanges) ||
                    lineString.startsWith(keywords.closedIssues)
                ) {
                    break
                }
                sb.append(lineString).appendln()
                pos++
            }
            result.longDescription = sb.toString().trim { it <= ' ' }

            // 重大改变
            sb.clear()
            while (pos < strings.size) {
                val lineString = strings[pos]
                if (lineString.startsWith(keywords.closedIssues)) {
                    break
                }
                sb.append(lineString).appendln()
                pos++
            }
            val breakingChanges = sb.toString().trim { it <= ' ' }.replace(keywords.breakingChanges, "")
            if (breakingChanges != keywords.breakingChangesEmpty) {
                result.breakingChanges = breakingChanges
            }

            // 关闭的问题
            sb.clear()
            while (pos < strings.size) {
                val lineString = strings[pos]
                if (lineString.startsWith(keywords.closedIssues)) {
                    sb.append(lineString.replace(keywords.closedIssues, ""))
                        .append(keywords.closedIssuesSeparator)
                }
                pos++
            }
            if (sb.isNotEmpty() && sb.endsWith(keywords.closedIssuesSeparator)) {
                sb.delete(sb.length - 1, sb.length)
            }
            val closedIssues = sb.toString()
            if (closedIssues != keywords.closedIssuesEmpty) {
                result.closedIssues = closedIssues
            }
            return result
        }
    }
}

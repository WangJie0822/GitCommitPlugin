package cn.wj.plugin.gitcommit

import java.util.regex.Pattern
import org.apache.commons.lang.StringUtils
import org.apache.commons.lang.WordUtils

/**
 * 提交信息
 *
 * @param changeType 修改类型
 * @param changeScope 修改范围
 * @param shortDescription 简单说明
 * @param longDescription 详细说明
 * @param breakingChanges 重大修改
 * @param closedIssues 关闭的问题
 * @param wrapText 自动换行
 * @param skipCI 跳过 CI
 */
data class CommitMessage(
    var changeType: ChangeType = ConfigHelper.DEFAULT,
    var changeScope: String = "",
    var shortDescription: String = "",
    var longDescription: String = "",
    var breakingChanges: String = "",
    var closedIssues: String = "",
    var wrapText: Boolean = true,
    var skipCI: Boolean = false
) {

    override fun toString(): String {
        return with(StringBuilder()) {
            append(changeType.action)

            if (changeScope.isNotBlank()) {
                append("($changeScope)")
            }

            append(": $shortDescription")

            if (longDescription.isNotBlank()) {
                appendln()
                appendln()
                append(
                    if (wrapText) {
                        WordUtils.wrap(longDescription, MAX_LINE_LENGTH)
                    } else {
                        longDescription
                    }
                )
            }

            if (breakingChanges.isNotBlank()) {
                val content = "BREAKING CHANGE: $breakingChanges"
                appendln()
                appendln()
                append(
                    if (wrapText) {
                        WordUtils.wrap(content, MAX_LINE_LENGTH)
                    } else {
                        content
                    }
                )
            }

            if (closedIssues.isNotBlank()) {
                appendln()
                for (closedIssue in closedIssues.split(",")) {
                    appendln()
                    append("Closes ${formatClosedIssue(closedIssue)}")
                }
            }

            if (skipCI) {
                appendln()
                appendln()
                append("[skip ci]")
            }

            toString()
        }
    }

    private fun formatClosedIssue(closedIssue: String): String {
        val trimmed = closedIssue.trim { it <= ' ' }
        return (if (StringUtils.isNumeric(trimmed)) "#" else "") + trimmed
    }

    companion object {
        private const val MAX_LINE_LENGTH = 72

        private val COMMIT_CLOSES_FORMAT = Pattern.compile("Closes (.+)")

        fun parse(message: String): CommitMessage {
            val result = CommitMessage()
            val strings = message.split("\n".toRegex()).toTypedArray()
            if (strings.isEmpty()) {
                return result
            }

            val line1 = strings[0]
            if (!line1.contains(":")) {
                return result
            }

            val changeTypeStr: String
            if (line1.contains("(")) {
                // 包含范围
                changeTypeStr = line1.substring(0, line1.indexOf("("))
                result.changeScope = line1.substring(line1.indexOf("(") + 1, line1.indexOf(")"))
            } else {
                // 没有范围
                changeTypeStr = line1.substring(0, line1.indexOf(":"))
            }
            // 修改类型
            result.changeType = ConfigHelper.findChangeType(changeTypeStr)
            // 简单说明
            result.shortDescription = line1.substring(line1.indexOf(": ") + 1, line1.length)
            if (strings.size < 2) {
                return result
            }
            var pos = 1
            val sb = StringBuilder()
            while (pos < strings.size) {
                val lineString = strings[pos]
                if (lineString.startsWith("BREAKING") ||
                    lineString.startsWith("Closes") ||
                    lineString.equals("[skip ci]", ignoreCase = true)
                ) {
                    break
                }
                sb.append(lineString).append('\n')
                pos++
            }
            result.longDescription = sb.toString().trim { it <= ' ' }

            sb.clear()
            while (pos < strings.size) {
                val lineString = strings[pos]
                if (lineString.startsWith("Closes") ||
                    lineString.equals("[skip ci]", ignoreCase = true)
                ) {
                    break
                }
                sb.append(lineString).append('\n')
                pos++
            }
            result.breakingChanges = sb.toString().trim { it <= ' ' }.replace("BREAKING CHANGE: ", "")

            sb.clear()
            try {
                val matcher = COMMIT_CLOSES_FORMAT.matcher(message)
                while (matcher.find()) {
                    sb.append(matcher.group(1)).append(',')
                }
            } catch (throwable: Throwable) {
                print(throwable)
            }
            if (sb.isNotEmpty()) {
                sb.delete(sb.length - 1, sb.length)
            }
            result.closedIssues = sb.toString()
            result.skipCI = message.contains("[skip ci]")
            return result
        }
    }
}

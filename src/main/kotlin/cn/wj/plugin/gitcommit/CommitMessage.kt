package cn.wj.plugin.gitcommit

import org.apache.commons.lang.StringUtils
import org.apache.commons.lang.WordUtils
import java.util.regex.Pattern

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
        var changeType: ChangeType = ChangeType.DEFAULT,
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
            append(changeType.label())

            if (changeScope.isNotBlank()) {
                append("($changeScope)")
            }

            append(": $shortDescription")

            if (longDescription.isNotBlank()) {
                appendln()
                appendln()
                append(if (wrapText) {
                    WordUtils.wrap(longDescription, MAX_LINE_LENGTH)
                } else {
                    longDescription
                })
            }

            if (breakingChanges.isNotBlank()) {
                val content = "BREAKING CHANGE: $breakingChanges"
                appendln()
                appendln()
                append(if (wrapText) {
                    WordUtils.wrap(content, MAX_LINE_LENGTH)
                } else {
                    content
                })
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

        private val COMMIT_FIRST_LINE_FORMAT = Pattern.compile("^([a-z]+)(\\((.+)\\))?: (.+)")
        private val COMMIT_CLOSES_FORMAT = Pattern.compile("Closes (.+)")

        fun parse(message: String): CommitMessage {
            val result = CommitMessage()
            try {
                var matcher = COMMIT_FIRST_LINE_FORMAT.matcher(message)
                if (!matcher.find()) {
                    return result
                }

                result.changeType = ChangeType.valueOf(matcher.group(1).toUpperCase())
                result.changeScope = matcher.group(3)
                result.shortDescription = matcher.group(4)

                val strings = message.split("\n".toRegex()).toTypedArray()
                if (strings.size < 2) {
                    return result
                }

                var pos = 1
                val sb = StringBuilder()
                while (pos < strings.size) {
                    val lineString = strings[pos]
                    if (lineString.startsWith("BREAKING")
                            || lineString.startsWith("Closes")
                            || lineString.equals("[skip ci]", ignoreCase = true)) {
                        break
                    }
                    sb.append(lineString).append('\n')
                    pos++
                }
                result.longDescription = sb.toString().trim { it <= ' ' }

                sb.clear()
                while (pos < strings.size) {
                    val lineString = strings[pos]
                    if (lineString.startsWith("Closes")
                            || lineString.equals("[skip ci]", ignoreCase = true)) {
                        break
                    }
                    sb.append(lineString).append('\n')
                    pos++
                }
                result.breakingChanges = sb.toString().trim { it <= ' ' }.replace("BREAKING CHANGE: ", "")

                matcher = COMMIT_CLOSES_FORMAT.matcher(message)
                sb.clear()
                while (matcher.find()) {
                    sb.append(matcher.group(1)).append(',')
                }
                if (sb.isNotEmpty()) {
                    sb.delete(sb.length - 1, sb.length)
                }
                result.closedIssues = sb.toString()

                result.skipCI = message.contains("[skip ci]")
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
            return result
        }
    }
}
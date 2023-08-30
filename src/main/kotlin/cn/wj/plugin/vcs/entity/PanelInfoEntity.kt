package cn.wj.plugin.vcs.entity

import cn.wj.plugin.vcs.constants.DEFAULT_TYPE_OF_CHANGE_LIST
import kotlinx.serialization.Serializable

/**
 * 提交规范面板数据
 *
 * > [王杰](mailto:w15555650921@gmail.com) 创建于 20201/3/19
 */
@Serializable
data class PanelInfoEntity(
        val keywords: KeywordsEntity = KeywordsEntity(),
        val changeTypes: ArrayList<ChangeTypeEntity> = DEFAULT_TYPE_OF_CHANGE_LIST
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
        val closedIssues: String = "closes issue: ",
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

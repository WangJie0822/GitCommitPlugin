package cn.wj.plugin.vcs.ext

import cn.wj.plugin.vcs.constants.STORAGE_KEY_COMMIT_MESSAGE
import cn.wj.plugin.vcs.entity.CommitMessageEntity
import cn.wj.plugin.vcs.tools.toTypeEntity
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.vcs.CheckinProjectPanel
import com.intellij.openapi.vcs.CommitMessageI
import com.intellij.openapi.vcs.VcsDataKeys
import com.intellij.openapi.vcs.changes.LocalChangeList
import com.intellij.openapi.vcs.changes.ui.CommitMessageProvider
import com.intellij.openapi.vcs.ui.Refreshable

fun AnActionEvent.getCommitMessageI(): CommitMessageI? {
    val data = Refreshable.PANEL_KEY.getData(dataContext)
    return if (data is CommitMessageI) {
        data
    } else {
        VcsDataKeys.COMMIT_MESSAGE_CONTROL.getData(dataContext)
    }
}

fun AnActionEvent.getMessage(localChangeList: LocalChangeList? = null): CommitMessageEntity? {
    val cmi = getCommitMessageI()
    return when {
        cmi is CheckinProjectPanel -> {
            val msg = cmi.commitMessage
            CommitMessageEntity.parse(msg, project)
        }
        null != localChangeList -> {
            CommitMessageEntity.parse(getCommitMessageFor(localChangeList).orEmpty(), project)
        }
        null != project -> {
            PropertiesComponent.getInstance(project!!)
                .getValue(STORAGE_KEY_COMMIT_MESSAGE)
                .toTypeEntity()
        }
        else -> {
            null
        }
    }
}

fun AnActionEvent.getCommitMessageFor(changeList: LocalChangeList): String? {
    if (null == project) {
        return null
    }
    CommitMessageProvider.EXTENSION_POINT_NAME.extensionList.forEach { provider ->
        val providerMessage = provider.getCommitMessage(changeList, project!!)
        if (providerMessage != null) return providerMessage
    }

    val changeListDescription = changeList.comment
    if (!changeListDescription.isNullOrBlank()) return changeListDescription

    return if (!changeList.hasDefaultName()) changeList.name else null
}

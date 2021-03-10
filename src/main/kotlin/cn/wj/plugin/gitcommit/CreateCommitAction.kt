package cn.wj.plugin.gitcommit

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.vcs.CheckinProjectPanel
import com.intellij.openapi.vcs.CommitMessageI
import com.intellij.openapi.vcs.VcsDataKeys
import com.intellij.openapi.vcs.ui.Refreshable

/**
 * 创建提交按钮
 */
class CreateCommitAction
    : DumbAwareAction() {

    init {
        isEnabledInModalContext = true
    }

    override fun actionPerformed(e: AnActionEvent) {
        val cmi = getCommitMessageI(e) ?: return
        val commitMessage = getCommitMessage(cmi)
        val dialog = CommitDialog(e.project, commitMessage)
        dialog.show()

        if (dialog.exitCode == DialogWrapper.OK_EXIT_CODE) {
            cmi.setCommitMessage(dialog.getCommitMessage().toString())
        }
    }

    private fun getCommitMessageI(e: AnActionEvent): CommitMessageI? {
        val data = Refreshable.PANEL_KEY.getData(e.dataContext)
        return if (data is CommitMessageI) {
            data
        } else {
            VcsDataKeys.COMMIT_MESSAGE_CONTROL.getData(e.dataContext)
        }
    }

    private fun getCommitMessage(cmi: CommitMessageI): CommitMessage? {
        return if (cmi is CheckinProjectPanel) {
            val msg = cmi.commitMessage
            CommitMessage.parse(msg)
        } else {
            null
        }
    }

}
package cn.wj.plugin.vcs.action

import cn.wj.plugin.vcs.dialog.CommitSpecificationDialog
import cn.wj.plugin.vcs.ext.getCommitMessageI
import cn.wj.plugin.vcs.ext.getMessage
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.ui.DialogWrapper

/**
 * 创建提交按钮
 *
 * > [王杰](mailto:w15555650921@gmail.com) 创建于 20201/3/19
 */
class CreateCommitAction :
    DumbAwareAction() {

    init {
        isEnabledInModalContext = true
    }

    override fun actionPerformed(e: AnActionEvent) {
        val commitMessage = e.getMessage()
        val dialog = CommitSpecificationDialog(e.project, commitMessage)
        dialog.show()

        if (dialog.exitCode == DialogWrapper.OK_EXIT_CODE) {
            e.getCommitMessageI()?.setCommitMessage(dialog.getMessageEntity().getCommitString(e.project))
        }
    }
}

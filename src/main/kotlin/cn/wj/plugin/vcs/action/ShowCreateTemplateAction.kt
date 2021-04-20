package cn.wj.plugin.vcs.action

import cn.wj.plugin.vcs.dialog.CommitSpecificationDialog
import cn.wj.plugin.vcs.ext.getMessage
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.vcs.actions.CommonCheckinProjectAction
import com.intellij.openapi.vcs.actions.VcsContextWrapper
import com.intellij.openapi.vcs.changes.ChangeListManager
import com.intellij.openapi.vcs.changes.ChangesViewManager
import com.intellij.vcs.commit.AbstractCommitWorkflowHandler
import com.intellij.vcs.commit.ChangesViewCommitWorkflow
import com.intellij.vcs.commit.ChangesViewCommitWorkflowUi

/**
 * 显示创建提交弹窗按钮
 *
 * > [王杰](mailto:w15555650921@gmail.com) 创建于 20201/4/14
 */
class ShowCreateTemplateAction : CommonCheckinProjectAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project
        val changeList = if (null == project) {
            null
        } else {
            getInitiallySelectedChangeList(VcsContextWrapper.createCachedInstanceOn(e), project)
        }
        val commitMessage = e.getMessage(changeList)
        val dialog = CommitSpecificationDialog(e.project, commitMessage)
        dialog.show()

        if (dialog.exitCode == DialogWrapper.OK_EXIT_CODE) {
            if (null != project) {
                // 生成后的提交信息
                val changedMessage = dialog.getMessageEntity().getCommitString(project)
                // 修改 Tool window 模式下文本
                (
                    ChangesViewManager.getInstanceEx(project).commitWorkflowHandler
                        as? AbstractCommitWorkflowHandler<ChangesViewCommitWorkflow, ChangesViewCommitWorkflowUi>
                    )
                    ?.ui?.commitMessageUi?.text = changedMessage
                // 修改 Dialog 模式下文本
                val change = getInitiallySelectedChangeList(VcsContextWrapper.createCachedInstanceOn(e), project)
                ChangeListManager.getInstance(project)
                    .editComment(change.name, changedMessage)
            }
            // 显示提交界面
            super.actionPerformed(e)
        }
    }
}

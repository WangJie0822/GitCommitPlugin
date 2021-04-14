package cn.wj.plugin.vcs.action

import cn.wj.plugin.vcs.dialog.CommitSpecificationDialog
import cn.wj.plugin.vcs.ext.getMessage
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.vcs.actions.CommonCheckinProjectAction
import com.intellij.openapi.vcs.actions.VcsContextWrapper
import com.intellij.openapi.vcs.changes.ChangeListManager

/**
 * 显示创建提交弹窗按钮
 *
 * > [王杰](mailto:w15555650921@gmail.com) 创建于 20201/4/14
 */
class ShowCreateTemplateAction : CommonCheckinProjectAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val changeList = if (null == e.project) {
            null
        } else {
            getInitiallySelectedChangeList(VcsContextWrapper.createCachedInstanceOn(e), e.project!!)
        }
        val commitMessage = e.getMessage(changeList)
        val dialog = CommitSpecificationDialog(e.project, commitMessage)
        dialog.show()

        if (dialog.exitCode == DialogWrapper.OK_EXIT_CODE) {
            changeList?.let {
                ChangeListManager.getInstance(e.project!!)
                    .editComment(it.name, dialog.getMessageEntity().getCommitString(e.project))
            }
            super.actionPerformed(e)
        }
    }
}

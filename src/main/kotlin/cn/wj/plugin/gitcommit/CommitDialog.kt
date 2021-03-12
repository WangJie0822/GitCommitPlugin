package cn.wj.plugin.gitcommit

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import javax.swing.JComponent

/**
 * 提交弹窗
 *
 * @param project [Project] 对象
 * @param message 提交信息 [CommitMessage] 对象
 */
class CommitDialog(project: Project?, message: CommitMessage?) :
    DialogWrapper(project) {

    private val panel: CommitPanel = CommitPanel(project, message)

    init {
        title = "Commit"
        setOKButtonText("OK")
        init()
    }

    override fun createCenterPanel(): JComponent {
        return panel.mainPanel
    }

    fun getCommitMessage(): CommitMessage {
        return panel.getCommitMessage()
    }
}

package cn.wj.plugin.gitcommit

import com.google.gson.JsonParser
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.vcs.CheckinProjectPanel
import com.intellij.openapi.vcs.CommitMessageI
import com.intellij.openapi.vcs.VcsDataKeys
import com.intellij.openapi.vcs.ui.Refreshable
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader

/**
 * 创建提交按钮
 */
class CreateCommitAction
    : DumbAwareAction() {

    init {
        isEnabledInModalContext = true
    }

    override fun actionPerformed(e: AnActionEvent) {
        val templateList = getTemplateList(e.project)
        if (templateList.isNotEmpty()) {
            ChangeType.typeList = templateList
        } else {
            ChangeType.typeList = ChangeType.DEFAULT_LIST
        }
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

    private fun getTemplateList(project: Project?): ArrayList<ChangeType> {
        val projectRoot = project?.basePath ?: return arrayListOf()
        val templateFile = File(projectRoot, "git_template.json")
        if (!templateFile.exists()) {
            return arrayListOf()
        }
        try {
            val result = arrayListOf<ChangeType>()
            val jsonArray = JsonParser.parseReader(BufferedReader(InputStreamReader(FileInputStream(templateFile), "UTF-8"))).asJsonArray
            jsonArray.forEach { element ->
                element.asJsonObject.let { obj ->
                    result.add(ChangeType(
                            obj.get("action").asString,
                            obj.get("title").asString,
                            obj.get("description").asString
                    ))
                }
            }
            return result
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            return arrayListOf()
        }
    }
}
package cn.wj.plugin.gitcommit

import com.google.gson.JsonArray
import com.google.gson.JsonObject
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
class CreateCommitAction :
    DumbAwareAction() {

    init {
        isEnabledInModalContext = true
    }

    override fun actionPerformed(e: AnActionEvent) {
        initConfig(e.project)
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

    private fun initConfig(project: Project?) {
        val projectRoot = project?.basePath ?: return
        val templateFile = File(projectRoot, "git_template_config.json")
        if (!templateFile.exists()) {
            return
        }
        try {
            val jsonObject = JsonParser.parseReader(
                BufferedReader(InputStreamReader(FileInputStream(templateFile), "UTF-8"))
            ).asJsonObject
            if (jsonObject.has("changeTypeList")) {
                // 有自定义修改类型字段
                val changeType = jsonObject.get("changeTypeList")
                if (changeType.isJsonArray) {
                    // 类型正确
                    loadChangeTypeList(changeType.asJsonArray)
                } else {
                    ConfigHelper.typeList = ChangeType.DEFAULT_LIST
                }
            } else {
                ConfigHelper.typeList = ChangeType.DEFAULT_LIST
            }

            if (jsonObject.has("dialog")) {
                val dialog = jsonObject.get("dialog")
                if (dialog.isJsonObject) {
                    loadDialogInfo(dialog.asJsonObject)
                } else {
                    ConfigHelper.dialog = DialogEntity()
                }
            } else {
                ConfigHelper.dialog = DialogEntity()
            }
        } catch (throwable: Throwable) {
            print(throwable)
        }
    }

    private fun loadDialogInfo(jsonObject: JsonObject) {
        val dialogEntity = DialogEntity()
        if (jsonObject.has("typeOfChange")) {
            dialogEntity.typeOfChange = jsonObject.get("typeOfChange").asString
        }
        if (jsonObject.has("scopeOfThisChange")) {
            dialogEntity.scopeOfThisChange = jsonObject.get("scopeOfThisChange").asString
        }
        if (jsonObject.has("shortDescription")) {
            dialogEntity.shortDescription = jsonObject.get("shortDescription").asString
        }
        if (jsonObject.has("longDescription")) {
            dialogEntity.longDescription = jsonObject.get("longDescription").asString
        }
        if (jsonObject.has("breakingChanges")) {
            dialogEntity.breakingChanges = jsonObject.get("breakingChanges").asString
        }
        if (jsonObject.has("closedIssues")) {
            dialogEntity.closedIssues = jsonObject.get("closedIssues").asString
        }
        if (jsonObject.has("wrapText")) {
            dialogEntity.wrapText = jsonObject.get("wrapText").asString
        }
        if (jsonObject.has("skipCI")) {
            dialogEntity.skipCI = jsonObject.get("skipCI").asString
        }
        ConfigHelper.dialog = dialogEntity
    }

    private fun loadChangeTypeList(jsonArray: JsonArray) {
        val ls = arrayListOf<ChangeType>()
        jsonArray.forEach { element ->
            val obj = element.asJsonObject
            val action = if (obj.has("action")) {
                obj.get("action").asString
            } else {
                "ACTION"
            }
            val title = if (obj.has("title")) {
                obj.get("title").asString
            } else {
                "TITLE"
            }
            val description = if (obj.has("description")) {
                obj.get("description").asString
            } else {
                "DESCRIPTION"
            }
            ls.add(ChangeType(action, title, description))
        }
        if (ls.isEmpty()) {
            ConfigHelper.typeList = ChangeType.DEFAULT_LIST
        } else {
            ConfigHelper.typeList = ls
        }
    }
}

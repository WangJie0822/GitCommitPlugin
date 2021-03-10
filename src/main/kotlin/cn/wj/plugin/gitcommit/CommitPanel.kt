package cn.wj.plugin.gitcommit

import com.intellij.openapi.project.Project
import java.io.File
import java.util.function.Consumer
import javax.swing.*

/**
 * 创建提交面标
 */
class CommitPanel(project: Project?, message: CommitMessage?) {

    lateinit var mainPanel: JPanel
    lateinit var changeScope: JComboBox<String>
    lateinit var shortDescription: JTextField
    lateinit var longDescription: JTextArea
    lateinit var breakingChanges: JTextArea
    lateinit var closedIssues: JTextField
    lateinit var wrapTextCheckBox: JCheckBox
    lateinit var skipCICheckBox: JCheckBox
    lateinit var featRadioButton: JRadioButton
    lateinit var fixRadioButton: JRadioButton
    lateinit var docsRadioButton: JRadioButton
    lateinit var styleRadioButton: JRadioButton
    lateinit var refactorRadioButton: JRadioButton
    lateinit var perfRadioButton: JRadioButton
    lateinit var testRadioButton: JRadioButton
    lateinit var buildRadioButton: JRadioButton
    lateinit var ciRadioButton: JRadioButton
    lateinit var choreRadioButton: JRadioButton
    lateinit var revertRadioButton: JRadioButton
    lateinit var changeTypeGroup: ButtonGroup

    init {
        val path = project?.basePath.orEmpty()
        val workingDirectory = File(path)
        val result = GitLogQuery(workingDirectory).execute()
        if (result.isSuccess()) {
            changeScope.addItem("")
            result.getScopes().forEach(Consumer { item: String? ->
                changeScope.addItem(item)
            })
        }
        if (message != null) {
            restoreValuesFromParsedCommitMessage(message)
        }
    }

    private fun restoreValuesFromParsedCommitMessage(message: CommitMessage) {
        val buttons = changeTypeGroup.elements
        while (buttons.hasMoreElements()) {
            val button = buttons.nextElement()
            if (button.actionCommand.equals(message.changeType.label(), ignoreCase = true)) {
                button.isSelected = true
            }
        }
        changeScope.selectedItem = message.changeScope
        shortDescription.text = message.shortDescription
        longDescription.text = message.longDescription
        breakingChanges.text = message.breakingChanges
        closedIssues.text = message.closedIssues
        skipCICheckBox.isSelected = message.skipCI
    }

    private fun getSelectedChangeType(): ChangeType {
        val buttons = changeTypeGroup.elements
        while (buttons.hasMoreElements()) {
            val button = buttons.nextElement()
            if (button.isSelected) {
                return ChangeType.valueOf(button.actionCommand.toUpperCase())
            }
        }
        return ChangeType.DEFAULT
    }


    fun getCommitMessage(): CommitMessage {
        return CommitMessage(
                getSelectedChangeType(),
                changeScope.selectedItem?.toString().orEmpty(),
                shortDescription.text.trim { it <= ' ' },
                longDescription.text.trim { it <= ' ' },
                breakingChanges.text.trim { it <= ' ' },
                closedIssues.text.trim { it <= ' ' },
                wrapTextCheckBox.isSelected,
                skipCICheckBox.isSelected
        )
    }
}
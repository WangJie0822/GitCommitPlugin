package cn.wj.plugin.gitcommit

import com.intellij.openapi.project.Project
import com.intellij.uiDesigner.core.GridConstraints
import com.intellij.uiDesigner.core.GridLayoutManager
import com.intellij.uiDesigner.core.Spacer
import java.io.File
import java.util.function.Consumer
import javax.swing.*

/**
 * 创建提交面板
 */
class CommitPanel(project: Project?, message: CommitMessage?) {

    private val typeList: ArrayList<ChangeType>
        get() = ConfigHelper.typeList

    lateinit var mainPanel: JPanel
    lateinit var changeTypePanel: JPanel
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
    lateinit var typeOfChangeLabel: JLabel
    lateinit var scopeOfThisChangeLabel: JLabel
    lateinit var shortDescriptionLabel: JLabel
    lateinit var longDescriptionLabel: JLabel
    lateinit var breakingChangesLabel: JLabel
    lateinit var closedIssuesLabel: JLabel

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

        ConfigHelper.dialog.let { entity ->
            typeOfChangeLabel.text = entity.typeOfChange
            scopeOfThisChangeLabel.text = entity.scopeOfThisChange
            shortDescriptionLabel.text = entity.shortDescription
            longDescriptionLabel.text = entity.longDescription
            breakingChangesLabel.text = entity.breakingChanges
            closedIssuesLabel.text = entity.closedIssues
            wrapTextCheckBox.text = entity.wrapText
            skipCICheckBox.text = entity.skipCI
        }
    }

    private fun restoreValuesFromParsedCommitMessage(message: CommitMessage) {
        val buttons = changeTypeGroup.elements.toList()
        buttons.forEach { button ->
            changeTypeGroup.remove(button)
        }
        val spacer = changeTypePanel.components.firstOrNull { it is Spacer }
        val spacerConstraint = if (spacer != null) (changeTypePanel.layout as GridLayoutManager).getConstraintsForComponent(spacer) else null
        val defaultConstraint = (changeTypePanel.layout as GridLayoutManager).getConstraintsForComponent(buttons.first())

        changeTypePanel.removeAll()

        if (spacer != null) {
            changeTypePanel.add(spacer, spacerConstraint)
        }

        typeList.forEachIndexed { index, changeType ->
            val rb = JRadioButton(changeType.toString())
            rb.actionCommand = changeType.action
            val clone = defaultConstraint.clone() as GridConstraints
            clone.row = index
            changeTypeGroup.add(rb)
            changeTypePanel.add(rb, clone)
        }

        var selected = false
        changeTypeGroup.elements.toList().forEach { button ->
            if (button.actionCommand.equals(message.changeType.action, true)) {
                button.isSelected = true
                selected = true
            }
        }
        if (!selected) {
            // 未选中，默认选中第一条
            changeTypeGroup.elements.toList().firstOrNull()?.isSelected = true
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
                return typeList.firstOrNull {
                    it.action.equals(button.actionCommand, true)
                } ?: typeList[0]
            }
        }
        return typeList[0]
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
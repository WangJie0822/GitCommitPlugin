package cn.wj.plugin.vcs.commit

import cn.wj.plugin.vcs.ui.fillX
import cn.wj.plugin.vcs.ui.migLayout
import cn.wj.plugin.vcs.ui.migLayoutVertical
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogWrapper
import net.miginfocom.layout.CC
import java.io.File
import java.util.function.Consumer
import javax.swing.ButtonGroup
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JRadioButton
import javax.swing.JTextArea
import javax.swing.JTextField

/**
 * 提交规范弹窗
 *
 * > [王杰](mailto:w15555650921@gmail.com) 创建于 20201/3/19
 */
class CommitSpecificationDialog(project: Project?, message: CommitMessageEntity?) :
    DialogWrapper(project) {

    private val panel = CommitSpecificationPanel(project, message)

    init {
        title = "Commit"
        setOKButtonText("OK")
        init()
    }

    override fun createCenterPanel(): JComponent {
        return panel.createCenterPanel()
    }

    fun getMessageEntity(): CommitMessageEntity {
        return panel.getMessageEntity()
    }
}

/**
 * 规范弹窗面板
 */
class CommitSpecificationPanel(private val project: Project?, private val message: CommitMessageEntity?) {

    private var typeOfChangeGroup: ButtonGroup? = null

    private var scopeOfChangeBox: ComboBox<String>? = null

    private var shortDescriptionField: JTextField? = null

    private var longDescriptionArea: JTextArea? = null

    private var breakingChangesArea: JTextArea? = null

    private var closedIssuesField: JTextField? = null

    fun createCenterPanel(): JComponent {
        val config = ConfigHelper.loadConfig(project)

        return JPanel(migLayout()).apply {
            // 修改类型
            add(JLabel(config.label.typeOfChange))
            add(
                JPanel(migLayoutVertical()).apply {
                    typeOfChangeGroup = ButtonGroup()
                    var selected = false
                    config.changeTypes.forEach { changeType ->
                        val rb = JRadioButton(changeType.display()).apply {
                            actionCommand = changeType.action
                            if (message?.typeOfChange?.action == changeType.action) {
                                isSelected = true
                                selected = true
                            }
                        }
                        typeOfChangeGroup!!.add(rb)
                        add(rb, fillX())
                    }
                    if (!selected) {
                        typeOfChangeGroup!!.elements.toList().firstOrNull()?.isSelected = true
                    }
                },
                fillX().gap("5", "5", "5", "5").wrap()
            )

            // 影响范围
            add(JLabel(config.label.scopeOfChange), CC())
            scopeOfChangeBox = ComboBox<String>().apply {
                isEditable = true
                val path = project?.basePath.orEmpty()
                val workingDirectory = File(path)
                val result = GitLogQuery(workingDirectory).execute()
                if (result.isSuccess()) {
                    addItem("")
                    result.getScopes().forEach(
                        Consumer { item: String? ->
                            addItem(item)
                        }
                    )
                }
                selectedItem = message?.scopeOfChange.orEmpty()
            }
            add(scopeOfChangeBox!!, fillX().gap("5", "5", "5", "5").wrap())

            // 简单说明
            add(JLabel(config.label.shortDescription))
            shortDescriptionField = JTextField(message?.shortDescription.orEmpty())
            add(shortDescriptionField!!, fillX().gap("5", "5", "5", "5").wrap())

            // 详细说明
            add(JLabel(config.label.longDescription))
            longDescriptionArea = JTextArea(message?.longDescription.orEmpty())
            add(longDescriptionArea!!, fillX().gap("5", "5", "5", "5").minWidth("200").minHeight("100").wrap())

            // 重大改变
            add(JLabel(config.label.breakingChanges))
            breakingChangesArea = JTextArea(message?.breakingChanges.orEmpty())
            add(breakingChangesArea!!, fillX().gap("5", "5", "5", "5").minWidth("200").minHeight("50").wrap())

            // 关闭的问题
            add(JLabel(config.label.closedIssues))
            closedIssuesField = JTextField(message?.closedIssues.orEmpty())
            add(closedIssuesField!!, fillX().gap("5", "5", "5", "5").wrap())
        }
    }

    fun getMessageEntity(): CommitMessageEntity {
        return CommitMessageEntity(
            typeOfChange = getSelectedChangeType(),
            scopeOfChange = scopeOfChangeBox?.selectedItem.toString(),
            shortDescription = shortDescriptionField?.text.orEmpty().trim { it <= ' ' },
            longDescription = longDescriptionArea?.text.orEmpty().trim { it <= ' ' },
            breakingChanges = breakingChangesArea?.text.orEmpty().trim { it <= ' ' },
            closedIssues = closedIssuesField?.text.orEmpty().trim { it <= ' ' }
        )
    }

    private fun getSelectedChangeType(): ChangeTypeEntity {
        val typeList = ConfigHelper.loadConfig(project).changeTypes
        val selectedButton = typeOfChangeGroup?.elements?.toList()?.firstOrNull {
            it.isSelected
        } ?: return typeList.first()
        return typeList.firstOrNull {
            it.action == selectedButton.actionCommand
        } ?: typeList.first()
    }
}

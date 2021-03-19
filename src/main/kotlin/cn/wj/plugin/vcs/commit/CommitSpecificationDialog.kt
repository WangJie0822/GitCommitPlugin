package cn.wj.plugin.vcs.commit

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.uiDesigner.core.GridConstraints
import com.intellij.uiDesigner.core.GridLayoutManager
import java.awt.Component
import java.awt.Dimension
import java.awt.Font
import java.io.File
import java.util.function.Consumer
import javax.swing.ButtonGroup
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JRadioButton
import javax.swing.JTextArea
import javax.swing.JTextField
import javax.swing.UIManager
import javax.swing.plaf.FontUIResource

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

    private var typeOfChangeLabel: JLabel? = null
    private var typeOfChangePanel: JPanel? = null
    private var typeOfChangeGroup: ButtonGroup? = null

    private var scopeOfChangeLabel: JLabel? = null
    private var scopeOfChangeBox: ComboBox<String>? = null

    private var shortDescriptionLabel: JLabel? = null
    private var shortDescriptionField: JTextField? = null

    private var longDescriptionLabel: JLabel? = null
    private var longDescriptionArea: JTextArea? = null

    private var breakingChangesLabel: JLabel? = null
    private var breakingChangesArea: JTextArea? = null

    private var closedIssuesLabel: JLabel? = null
    private var closedIssuesField: JTextField? = null

    private var rowIndex = 0

    private val panelOneMap = hashMapOf<Component, Any>()
    private val panelTwoMap = hashMapOf<Component, Any>()

    fun createCenterPanel(): JComponent {
        val config = ConfigHelper.loadConfig(project)

        panelOneMap.clear()
        panelTwoMap.clear()
        rowIndex = 0

        initGlobalFont()

        createTypeOfChange(config)

        createScopeOfChange(config)

        createShortDescription(config)

        createLongDescription(config)

        createBreakingChanges(config)

        createClosedIssues(config)

        val panel = JPanel(GridLayoutManager(panelOneMap.size, 2))
        panelOneMap.forEach { (component, constraint) ->
            panel.add(component, constraint)
        }
        panelTwoMap.forEach { (component, constraint) ->
            panel.add(component, constraint)
        }
        return panel
    }

    private fun initGlobalFont() {
        val fontRes = FontUIResource(Font("YaHei", Font.PLAIN, TEXT_SIZE))
        val keys = UIManager.getDefaults().keys()
        while (keys.hasMoreElements()) {
            val key = keys.nextElement()
            val value = UIManager.get(key)
            if (value is FontUIResource) {
                UIManager.put(key, fontRes)
            }
        }
    }

    private fun createClosedIssues(config: PanelInfoEntity) {
        if (config.label.closedIssues.isNotBlank()) {
            // 已修复问题
            closedIssuesLabel = JLabel(config.label.closedIssues)
            panelOneMap[closedIssuesLabel!!] = createLabelConstraint(rowIndex)

            closedIssuesField = JTextField()
            closedIssuesField!!.text = message?.closedIssues.orEmpty()

            panelTwoMap[closedIssuesField!!] = createConstraint(rowIndex)
        }
    }

    private fun createBreakingChanges(config: PanelInfoEntity) {
        if (config.label.breakingChanges.isNotBlank()) {
            // 重大改变
            breakingChangesLabel = JLabel(config.label.breakingChanges)
            panelOneMap[breakingChangesLabel!!] = createLabelConstraint(rowIndex)

            breakingChangesArea = JTextArea()
            breakingChangesArea!!.text = message?.breakingChanges.orEmpty()

            panelTwoMap[breakingChangesArea!!] =
                createConstraint(rowIndex, Dimension(AREA_WIDTH, AREA_HEIGHT_BREAKING_CHANGE))
            rowIndex++
        }
    }

    private fun createLongDescription(config: PanelInfoEntity) {
        if (config.label.longDescription.isNotBlank()) {
            // 详细说明
            longDescriptionLabel = JLabel(config.label.longDescription)
            panelOneMap[longDescriptionLabel!!] = createLabelConstraint(rowIndex)

            longDescriptionArea = JTextArea()
            longDescriptionArea!!.text = message?.longDescription.orEmpty()

            panelTwoMap[longDescriptionArea!!] =
                createConstraint(rowIndex, Dimension(AREA_WIDTH, AREA_HEIGHT_DESCRIPTION))
            rowIndex++
        }
    }

    private fun createShortDescription(config: PanelInfoEntity) {
        if (config.label.shortDescription.isNotBlank()) {
            // 简单说明
            shortDescriptionLabel = JLabel(config.label.shortDescription)
            panelOneMap[shortDescriptionLabel!!] = createLabelConstraint(rowIndex)

            shortDescriptionField = JTextField()
            shortDescriptionField!!.text = message?.shortDescription.orEmpty()

            panelTwoMap[shortDescriptionField!!] = createConstraint(rowIndex)
            rowIndex++
        }
    }

    private fun createScopeOfChange(config: PanelInfoEntity) {
        if (config.label.scopeOfChange.isNotBlank()) {
            // 修改范围
            scopeOfChangeLabel = JLabel(config.label.scopeOfChange)
            panelOneMap[scopeOfChangeLabel!!] = createLabelConstraint(rowIndex)
            // 范围选框
            scopeOfChangeBox = ComboBox()
            scopeOfChangeBox!!.isEditable = true
            val path = project?.basePath.orEmpty()
            val workingDirectory = File(path)
            val result = GitLogQuery(workingDirectory).execute()
            if (result.isSuccess()) {
                scopeOfChangeBox!!.addItem("")
                result.getScopes().forEach(
                    Consumer { item: String? ->
                        scopeOfChangeBox!!.addItem(item)
                    }
                )
            }
            scopeOfChangeBox!!.selectedItem = message?.scopeOfChange.orEmpty()

            panelTwoMap[scopeOfChangeBox!!] = createConstraint(rowIndex)
            rowIndex++
        }
    }

    private fun createTypeOfChange(config: PanelInfoEntity) {
        if (config.label.typeOfChange.isNotBlank()) {
            // 修改类型
            typeOfChangeLabel = JLabel(config.label.typeOfChange)
            panelOneMap[typeOfChangeLabel!!] = createLabelConstraint(rowIndex)
            // 类型列表
            val typeList = config.changeTypes
            typeOfChangePanel = JPanel(GridLayoutManager(typeList.size, 1))
            typeOfChangeGroup = ButtonGroup()
            val defaultConstraint = GridConstraints().apply {
                anchor = GridConstraints.ANCHOR_WEST
                hSizePolicy = GridConstraints.SIZEPOLICY_CAN_GROW or GridConstraints.SIZEPOLICY_WANT_GROW
            }
            var selected = false
            typeList.forEachIndexed { index, changeType ->
                val rb = JRadioButton(changeType.toString())
                rb.actionCommand = changeType.action
                if (message?.typeOfChange?.action == changeType.action) {
                    rb.isSelected = true
                    selected = true
                }
                val clone = defaultConstraint.clone() as GridConstraints
                clone.row = index
                typeOfChangeGroup!!.add(rb)
                typeOfChangePanel!!.add(rb, clone)
            }
            if (!selected) {
                typeOfChangeGroup!!.elements.toList().firstOrNull()?.isSelected = true
            }

            panelTwoMap[typeOfChangePanel!!] = createConstraint(rowIndex)
            rowIndex++
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

    private fun createLabelConstraint(row: Int): GridConstraints {
        return GridConstraints().apply {
            this.row = row
        }
    }

    private fun createConstraint(
        row: Int,
        preferredSize: Dimension = Dimension(-1, -1)
    ): GridConstraints {
        return GridConstraints(
            row, 1, 1, 1, GridConstraints.ANCHOR_WEST,
            GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_GROW or GridConstraints.SIZEPOLICY_CAN_SHRINK,
            GridConstraints.SIZEPOLICY_CAN_GROW or GridConstraints.SIZEPOLICY_CAN_SHRINK,
            Dimension(-1, -1), preferredSize, Dimension(-1, -1)
        )
    }

    companion object {
        private const val AREA_WIDTH = 150
        private const val AREA_HEIGHT_DESCRIPTION = 100
        private const val AREA_HEIGHT_BREAKING_CHANGE = 50
        private const val TEXT_SIZE = 12
    }
}

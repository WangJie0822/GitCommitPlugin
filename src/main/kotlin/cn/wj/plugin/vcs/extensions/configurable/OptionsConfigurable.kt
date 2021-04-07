package cn.wj.plugin.vcs.extensions.configurable

import cn.wj.plugin.vcs.R
import cn.wj.plugin.vcs.bundle.getString
import cn.wj.plugin.vcs.constants.DEFAULT_AUTO_WRAP_LENGTH
import cn.wj.plugin.vcs.constants.DEFAULT_BREAKING_CHANGES
import cn.wj.plugin.vcs.constants.DEFAULT_BREAKING_CHANGES_WHEN_EMPTY
import cn.wj.plugin.vcs.constants.DEFAULT_CLOSED_ISSUES
import cn.wj.plugin.vcs.constants.DEFAULT_CLOSED_ISSUES_SEPARATOR
import cn.wj.plugin.vcs.constants.DEFAULT_CLOSED_ISSUES_WHEN_EMPTY
import cn.wj.plugin.vcs.constants.DEFAULT_DESCRIPTION_SEPARATOR
import cn.wj.plugin.vcs.constants.DEFAULT_JSON_CONFIG_PATH
import cn.wj.plugin.vcs.constants.DEFAULT_SCOPE_WRAPPER_END
import cn.wj.plugin.vcs.constants.DEFAULT_SCOPE_WRAPPER_START
import cn.wj.plugin.vcs.constants.DEFAULT_TEXT_AUTO_WRAP
import cn.wj.plugin.vcs.constants.DEFAULT_TYPE_OF_CHANGE_LIST
import cn.wj.plugin.vcs.constants.DEFAULT_USE_JSON_CONFIG
import cn.wj.plugin.vcs.constants.PROJECT_PATH_PLACEHOLDER
import cn.wj.plugin.vcs.dialog.TypeOfChangeDialog
import cn.wj.plugin.vcs.entity.ChangeTypeEntity
import cn.wj.plugin.vcs.storage.Options
import cn.wj.plugin.vcs.tools.ConfigHelper
import cn.wj.plugin.vcs.tools.toJsonString
import cn.wj.plugin.vcs.tools.toTypeEntity
import cn.wj.plugin.vcs.ui.fillX
import cn.wj.plugin.vcs.ui.fillY
import cn.wj.plugin.vcs.ui.migLayout
import cn.wj.plugin.vcs.ui.migLayoutVertical
import cn.wj.plugin.vcs.ui.wrap
import com.intellij.openapi.Disposable
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.project.guessCurrentProject
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.ui.CollectionListModel
import com.intellij.ui.IdeBorderFactory
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBList
import net.miginfocom.layout.CC
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.ListSelectionModel

/**
 * 设置选项
 *
 * > [王杰](mailto:w15555650921@gmail.com) 创建于 20201/3/29
 */
class OptionsConfigurable : SearchableConfigurable, Disposable {

    private var panel: OptionsConfigurablePanel? = null

    override fun createComponent(): JComponent {
        return OptionsConfigurablePanel().let {
            panel = it
            it.createCenterPanel()
        }
    }

    override fun isModified(): Boolean {
        return panel?.isModified() ?: false
    }

    override fun apply() {
        panel?.apply()
    }

    override fun getDisplayName(): String {
        return getString(R.String.setting_name)
    }

    override fun getId(): String {
        return "Wj.plugin.VcsHelper"
    }

    override fun dispose() {
        panel = null
    }
}

/**
 * 选项配置面板
 */
class OptionsConfigurablePanel {

    /** 选项配置 */
    private val options = Options.instance

    private val cbUseJsonConfig = JCheckBox(getString(R.String.setting_use_file_config)).apply {
        isSelected = options.useJsonConfig
    }

    private val tfConfigPath = JTextField(options.jsonConfigPath)

    private val cbTextAutoWrap = JCheckBox(getString(R.String.setting_wrap_lines)).apply {
        isSelected = options.textAutoWrap
    }

    private val tfAutoWrapLength = JTextField(options.autoWrapLength)

    private val tfScopeWrapperStart = JTextField(options.scopeWrapperStart)

    private val tfScopeWrapperEnd = JTextField(options.scopeWrapperEnd)

    private val tfDescriptionSeparator = JTextField(options.descriptionSeparator)

    private val tfBreakingChanges = JTextField(options.breakingChanges)

    private val tfBreakingChangesWhenEmpty = JTextField(options.breakingChangesWhenEmpty)

    private val tfClosedIssues = JTextField(options.closedIssues)

    private val tfClosedIssuesSeparator = JTextField(options.closedIssuesSeparator)

    private val tfClosedIssuesWhenEmpty = JTextField(options.closedIssuesWhenEmpty)

    private val lTypeOfChange = JBList(CollectionListModel(options.getTypeOfChangeList())).apply {
        selectionMode = ListSelectionModel.SINGLE_SELECTION
    }

    private val tdTypeOfChange = ToolbarDecorator
        .createDecorator(lTypeOfChange).apply {
            setAddActionName(getString(R.String.setting_add))
            setAddAction {
                TypeOfChangeDialog.actionShow(
                    ProjectManager.getInstance().defaultProject,
                    { entity ->
                        (lTypeOfChange.model as CollectionListModel).add(entity)
                    }
                )
            }
            setRemoveActionName(getString(R.String.setting_remove))
            setRemoveAction {
                (lTypeOfChange.model as CollectionListModel).remove(lTypeOfChange.selectedIndex)
            }
            setEditActionName(getString(R.String.setting_edit))
            setEditAction {
                TypeOfChangeDialog.actionShow(
                    ProjectManager.getInstance().defaultProject,
                    { entity ->
                        (lTypeOfChange.model as CollectionListModel).setElementAt(entity, lTypeOfChange.selectedIndex)
                    },
                    lTypeOfChange.selectedValue
                )
            }
        }

    fun createCenterPanel(): JComponent {
        return JPanel(migLayoutVertical()).apply {

            add(
                JPanel(migLayout()).apply {

                    border = IdeBorderFactory.createTitledBorder(getString(R.String.setting_general))

                    add(
                        JButton(getString(R.String.setting_import_config)).apply {
                            val project = guessCurrentProject(this)
                            addActionListener {
                                FileChooser.chooseFile(
                                    FileChooserDescriptorFactory.createSingleFileDescriptor("json"),
                                    project,
                                    project.guessProjectDir()
                                ) { vFile ->
                                    val config = ConfigHelper.loadFromJson(vFile)
                                    cbTextAutoWrap.isSelected = config.keywords.wrapWords
                                    tfAutoWrapLength.text = config.keywords.maxLineLength.toString()
                                    tfScopeWrapperStart.text = config.keywords.scopeWrapperStart
                                    tfScopeWrapperEnd.text = config.keywords.scopeWrapperEnd
                                    tfDescriptionSeparator.text = config.keywords.descriptionSeparator
                                    tfBreakingChanges.text = config.keywords.breakingChanges
                                    tfBreakingChangesWhenEmpty.text = config.keywords.breakingChangesEmpty
                                    tfClosedIssues.text = config.keywords.closedIssues
                                    tfClosedIssuesSeparator.text = config.keywords.closedIssuesSeparator
                                    tfClosedIssuesWhenEmpty.text = config.keywords.closedIssuesEmpty
                                    (lTypeOfChange.model as CollectionListModel).run {
                                        removeAll()
                                        addAll(0, config.changeTypes)
                                    }
                                }
                            }
                        },
                        CC().split(2).gapAfter("10")
                    )
                    add(
                        JButton(getString(R.String.setting_export_config)).apply {
                            addActionListener {
                                // TODO
                            }
                        },
                        CC().split(2).gapAfter("10")
                    )
                    add(
                        JButton(getString(R.String.setting_reset)).apply {
                            addActionListener {
                                cbUseJsonConfig.isSelected = DEFAULT_USE_JSON_CONFIG
                                tfConfigPath.text = DEFAULT_JSON_CONFIG_PATH
                                cbTextAutoWrap.isSelected = DEFAULT_TEXT_AUTO_WRAP
                                tfAutoWrapLength.text = DEFAULT_AUTO_WRAP_LENGTH
                                tfScopeWrapperStart.text = DEFAULT_SCOPE_WRAPPER_START
                                tfScopeWrapperEnd.text = DEFAULT_SCOPE_WRAPPER_END
                                tfDescriptionSeparator.text = DEFAULT_DESCRIPTION_SEPARATOR
                                tfBreakingChanges.text = DEFAULT_BREAKING_CHANGES
                                tfBreakingChangesWhenEmpty.text = DEFAULT_BREAKING_CHANGES_WHEN_EMPTY
                                tfClosedIssues.text = DEFAULT_CLOSED_ISSUES
                                tfClosedIssuesSeparator.text = DEFAULT_CLOSED_ISSUES_SEPARATOR
                                tfClosedIssuesWhenEmpty.text = DEFAULT_CLOSED_ISSUES_WHEN_EMPTY
                                (lTypeOfChange.model as CollectionListModel).let {
                                    it.removeAll()
                                    it.addAll(0, DEFAULT_TYPE_OF_CHANGE_LIST.toTypeEntity() ?: arrayListOf())
                                }
                            }
                        },
                        wrap()
                    )

                    add(cbUseJsonConfig, CC().gapAfter("20").split())
                    add(
                        TextFieldWithBrowseButton(tfConfigPath) {
                            val project = guessCurrentProject(cbUseJsonConfig)
                            val selectedFile = LocalFileSystem.getInstance()
                                .findFileByPath(
                                    tfConfigPath.text.replace(
                                        PROJECT_PATH_PLACEHOLDER,
                                        project.basePath.orEmpty()
                                    )
                                )
                                ?: project.guessProjectDir()
                            FileChooser.chooseFile(
                                FileChooserDescriptorFactory.createSingleFileDescriptor("json"),
                                project,
                                selectedFile
                            ) { vFile ->
                                tfConfigPath.text =
                                    vFile.path.replace(project.basePath.orEmpty(), PROJECT_PATH_PLACEHOLDER)
                            }
                        },
                        wrap()
                    )

                    add(cbTextAutoWrap, CC().gapAfter("20").split())
                    add(JLabel(getString(R.String.setting_maximum_length_of_single_lines)), CC().gapAfter("5"))
                    add(tfAutoWrapLength)
                    add(JPanel(), fillX().wrap())
                },
                fillX()
            )

            add(
                JPanel(migLayout()).apply {
                    border = IdeBorderFactory.createTitledBorder(getString(R.String.setting_keywords))

                    add(JLabel(getString(R.String.setting_scope_wrapper_start)), CC().gapAfter("5").split())
                    add(tfScopeWrapperStart, CC().gapAfter("20").minWidth("30"))
                    add(JLabel(getString(R.String.setting_scope_wrapper_end)), CC().gapAfter("5"))
                    add(tfScopeWrapperEnd, CC().minWidth("30").gapAfter("20"))
                    add(JLabel(getString(R.String.setting_description_separator)), CC().gapAfter("5"))
                    add(tfDescriptionSeparator, CC().minWidth("30"))
                    add(JPanel(), fillX().wrap())

                    add(JLabel(getString(R.String.setting_breaking_changes_key)), CC().gapAfter("5").split())
                    add(tfBreakingChanges, CC().gapAfter("20").minWidth("30"))
                    add(JLabel(getString(R.String.setting_breaking_changes_when_empty)), CC().gapAfter("5"))
                    add(tfBreakingChangesWhenEmpty, CC().gapAfter("20").minWidth("30"))
                    add(JPanel(), fillX().wrap())

                    add(JLabel(getString(R.String.setting_closed_issues_key)), CC().gapAfter("5").split())
                    add(tfClosedIssues, CC().gapAfter("20").minWidth("30"))
                    add(JLabel(getString(R.String.setting_closed_issues_separator)), CC().gapAfter("5"))
                    add(tfClosedIssuesSeparator, CC().gapAfter("20").minWidth("30"))
                    add(JLabel(getString(R.String.setting_closed_issues_when_empty)), CC().gapAfter("5"))
                    add(tfClosedIssuesWhenEmpty, CC().gapAfter("20").minWidth("30"))
                    add(JPanel(), fillX().wrap())
                },
                fillX()
            )

            add(
                JPanel(migLayout()).apply {
                    border = IdeBorderFactory.createTitledBorder(getString(R.String.setting_type_of_change))

                    add(tdTypeOfChange.createPanel(), fillX())
                },
                fillX()
            )

            add(JPanel(), fillY())
        }
    }

    fun isModified(): Boolean {
        return with(options) {
            val list = arrayListOf<ChangeTypeEntity>()
            for (i in 0 until lTypeOfChange.model.size) {
                list.add(lTypeOfChange.model.getElementAt(i))
            }
            useJsonConfig != cbUseJsonConfig.isSelected ||
                jsonConfigPath != tfConfigPath.text ||
                textAutoWrap != cbTextAutoWrap.isSelected ||
                autoWrapLength != tfAutoWrapLength.text ||
                scopeWrapperStart != tfScopeWrapperStart.text ||
                scopeWrapperEnd != tfScopeWrapperEnd.text ||
                descriptionSeparator != tfDescriptionSeparator.text ||
                breakingChanges != tfBreakingChanges.text ||
                breakingChangesWhenEmpty != tfBreakingChangesWhenEmpty.text ||
                closedIssues != tfClosedIssues.text ||
                closedIssuesSeparator != tfClosedIssuesSeparator.text ||
                closedIssuesWhenEmpty != tfClosedIssuesWhenEmpty.text ||
                typeOfChangeList != list.toJsonString()
        }
    }

    fun apply() {
        with(options) {
            val list = arrayListOf<ChangeTypeEntity>()
            for (i in 0 until lTypeOfChange.model.size) {
                list.add(lTypeOfChange.model.getElementAt(i))
            }
            useJsonConfig = cbUseJsonConfig.isSelected
            jsonConfigPath = tfConfigPath.text
            textAutoWrap = cbTextAutoWrap.isSelected
            autoWrapLength = tfAutoWrapLength.text
            scopeWrapperStart = tfScopeWrapperStart.text
            scopeWrapperEnd = tfScopeWrapperEnd.text
            descriptionSeparator = tfDescriptionSeparator.text
            breakingChanges = tfBreakingChanges.text
            breakingChangesWhenEmpty = tfBreakingChangesWhenEmpty.text
            closedIssues = tfClosedIssues.text
            closedIssuesSeparator = tfClosedIssuesSeparator.text
            closedIssuesWhenEmpty = tfClosedIssuesWhenEmpty.text
            typeOfChangeList = list.toJsonString()
        }
    }
}

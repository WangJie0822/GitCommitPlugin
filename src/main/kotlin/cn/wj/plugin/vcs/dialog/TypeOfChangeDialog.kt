package cn.wj.plugin.vcs.dialog

import cn.wj.plugin.vcs.R
import cn.wj.plugin.vcs.bundle.getString
import cn.wj.plugin.vcs.entity.ChangeTypeEntity
import cn.wj.plugin.vcs.ui.fillX
import cn.wj.plugin.vcs.ui.migLayout
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import net.miginfocom.layout.CC
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextArea
import javax.swing.JTextField

/**
 * 修改类型弹窗
 *
 * > [王杰](mailto:w15555650921@gmail.com) 创建于 20201/3/31
 */
class TypeOfChangeDialog(
    project: Project?,
    private val action: (ChangeTypeEntity) -> Unit,
    private val entity: ChangeTypeEntity? = null
) : DialogWrapper(project) {

    init {
        title = getString(R.String.setting_type_of_change)
        setOKButtonText(getString(R.String.general_confirm))
        init()
    }

    private var panel: TypeOfChangePanel? = null

    override fun createCenterPanel(): JComponent {
        return TypeOfChangePanel(entity).let {
            panel = it
            it.createCenterPanel()
        }
    }

    override fun isOK(): Boolean {
        return panel?.isOk() ?: false
    }

    override fun isOKActionEnabled(): Boolean {
        return panel?.isOk() ?: false
    }

    override fun doOKAction() {
        panel?.let {
            action.invoke(it.result())
        }
        super.doOKAction()
    }

    override fun dispose() {
        super.dispose()
        panel = null
    }

    companion object {
        fun actionShow(project: Project?, action: (ChangeTypeEntity) -> Unit, entity: ChangeTypeEntity? = null) {
            TypeOfChangeDialog(project, action, entity).show()
        }
    }
}

class TypeOfChangePanel(entity: ChangeTypeEntity?) {

    private val tfTitle = JTextField(entity?.title.orEmpty())

    private val tfAction = JTextField(entity?.action.orEmpty())

    private val taDescription = JTextArea(entity?.description.orEmpty())

    fun createCenterPanel(): JComponent {
        return JPanel(migLayout()).apply {
            add(JLabel(getString(R.String.setting_create_title)), CC().gapAfter("5").split())
            add(tfTitle, CC().gapAfter("10").growX().minWidth("100"))
            add(JLabel(getString(R.String.setting_create_action)), CC().gapAfter("5"))
            add(tfAction, fillX().minWidth("100").wrap())

            add(JLabel(getString(R.String.setting_create_description)), fillX().wrap())
            add(taDescription, fillX().minHeight("50").wrap())
        }
    }

    fun isOk(): Boolean {
        return tfTitle.text.isNotBlank() && tfAction.text.isNotBlank() && taDescription.text.isNotBlank()
    }

    fun result(): ChangeTypeEntity {
        return ChangeTypeEntity(tfTitle.text.trim(), tfAction.text.trim(), taDescription.text.trim())
    }
}

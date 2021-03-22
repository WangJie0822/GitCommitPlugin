package cn.wj.plugin.vcs

import cn.wj.plugin.vcs.commit.ConfigHelper
import cn.wj.plugin.vcs.constants.COMMIT_TEMPLATE_FILE_NAME
import cn.wj.plugin.vcs.simple.SimplePsiTreeChangeListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiTreeChangeEvent

/**
 * 项目监听
 *
 * > [王杰](mailto:w15555650921@gmail.com) 创建于 20201/3/22
 */
internal class MyProjectManagerListener : ProjectManagerListener {

    override fun projectOpened(project: Project) {
        println("Project Opened")
        addFileChangeListener(project)
        ConfigHelper.getInstance(project).init()
    }

    private fun addFileChangeListener(project: Project) {
        @Suppress("IncorrectParentDisposable")
        PsiManager.getInstance(project).addPsiTreeChangeListener(
            object : SimplePsiTreeChangeListener() {

                override fun childrenChanged(event: PsiTreeChangeEvent) {
                    if (event.file?.name == COMMIT_TEMPLATE_FILE_NAME &&
                        event.file?.virtualFile?.parent?.path == project.basePath
                    ) {
                        ConfigHelper.getInstance(project).propertiesChanged = true
                    }
                }
            },
            project
        )
    }
}

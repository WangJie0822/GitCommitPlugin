package cn.wj.plugin.vcs.extensions.listener

import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.checkout.CheckoutListener
import java.nio.file.Path

/**
 * 版本控制更新完成监听
 *
 * > [王杰](mailto:15555650921@163.com) 创建于 2021/4/6
 */
class VcsUpdateCompleteListener: CheckoutListener {

    override fun processCheckedOutDirectory(project: Project, directory: Path): Boolean {
        println(project.basePath)
        println(directory.toFile().absolutePath)
        return false
    }
}
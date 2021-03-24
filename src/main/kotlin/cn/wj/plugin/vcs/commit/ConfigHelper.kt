package cn.wj.plugin.vcs.commit

import cn.wj.plugin.vcs.constants.COMMIT_TEMPLATE_FILE_NAME
import cn.wj.plugin.vcs.tools.toTypeEntity
import com.intellij.openapi.project.Project
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader

/**
 * 配置帮助类
 *
 * > [王杰](mailto:w15555650921@gmail.com) 创建于 20201/3/19
 */
object ConfigHelper {

    fun loadConfig(project: Project?): PanelInfoEntity {
        val projectRoot = project?.basePath.orEmpty()
        val templateFile = File(projectRoot, COMMIT_TEMPLATE_FILE_NAME)
        if (!templateFile.exists()) {
            return PanelInfoEntity()
        }
        val br = BufferedReader(InputStreamReader(FileInputStream(templateFile), "UTF-8"))
        val sb = StringBuilder()
        br.useLines { lines ->
            lines.forEach { line ->
                sb.append(line)
            }
        }
        return sb.toString().toTypeEntity<PanelInfoEntity>() ?: PanelInfoEntity()
    }

}
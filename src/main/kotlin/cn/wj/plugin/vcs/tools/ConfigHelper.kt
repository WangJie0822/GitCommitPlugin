package cn.wj.plugin.vcs.tools

import cn.wj.plugin.vcs.constants.PROJECT_PATH_PLACEHOLDER
import cn.wj.plugin.vcs.entity.PanelInfoEntity
import cn.wj.plugin.vcs.storage.Options
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
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
        val options = Options.instance
        return if (options.useJsonConfig) {
            // 使用配置文件
            val configPath = options.jsonConfigPath.replace(PROJECT_PATH_PLACEHOLDER, project?.basePath.orEmpty())
            val templateFile = File(configPath)
            if (!templateFile.exists()) {
                // 配置文件不存在，使用通用配置
                options.toPanelEntity()
            } else {
                val br = BufferedReader(InputStreamReader(FileInputStream(templateFile), "UTF-8"))
                val sb = StringBuilder()
                br.useLines { lines ->
                    lines.forEach { line ->
                        sb.append(line)
                    }
                }
                sb.toString().toTypeEntity<PanelInfoEntity>() ?: options.toPanelEntity()
            }
        } else {
            // 使用通用配置
            options.toPanelEntity()
        }
    }

    fun loadFromJson(file: VirtualFile): PanelInfoEntity {
        val br = BufferedReader(InputStreamReader(file.inputStream, "UTF-8"))
        val sb = StringBuilder()
        br.useLines { lines ->
            lines.forEach { line ->
                sb.append(line)
            }
        }
        return sb.toString().toTypeEntity<PanelInfoEntity>() ?: PanelInfoEntity()
    }
}

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
class ConfigHelper private constructor(private val project: Project?) {

    private var config: PanelInfoEntity? = null

    var propertiesChanged = false

    /** 面板配置信息 */
    val panelInfo: PanelInfoEntity
        get() {
            if (null == config || propertiesChanged) {
                config = loadConfig(project)
                if (propertiesChanged) {
                    propertiesChanged = false
                }
            }
            return config!!
        }

    private fun loadConfig(project: Project?): PanelInfoEntity {
        println("Load Config")
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

    fun init() {
        config = loadConfig(project)
    }

    companion object : SingletonHolder<ConfigHelper, Project?>(::ConfigHelper)
}

open class SingletonHolder<out T, in A>(creator: (A) -> T) {
    private var creator: ((A) -> T)? = creator

    @Volatile
    private var instance: T? = null

    fun getInstance(arg: A): T {
        val i = instance
        if (i != null) {
            return i
        }

        return synchronized(this) {
            val i2 = instance
            if (i2 != null) {
                i2
            } else {
                val created = creator!!(arg)
                instance = created
                creator = null
                created
            }
        }
    }
}

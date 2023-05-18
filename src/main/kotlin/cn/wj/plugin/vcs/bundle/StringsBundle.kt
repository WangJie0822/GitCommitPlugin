package cn.wj.plugin.vcs.bundle

import com.intellij.AbstractBundle
import org.jetbrains.annotations.PropertyKey
import java.util.Locale
import java.util.ResourceBundle

/**
 * 字符数据包
 *
 * > [王杰](mailto:w15555650921@gmail.com) 创建于 20201/4/2
 */
object StringsBundle : AbstractBundle(BUNDLE_PATH) {

    override fun findBundle(
        pathToBundle: String,
        loader: ClassLoader,
        control: ResourceBundle.Control
    ): ResourceBundle {
        val dynamicLocale = Locale.forLanguageTag(Locale.getDefault().toLanguageTag())
        val dynamicBundle = ResourceBundle.getBundle(pathToBundle, dynamicLocale, loader, control)
        return dynamicBundle ?: super.findBundle(pathToBundle, loader, control)
    }
}

const val BUNDLE_PATH = "messages.StringsBundle"

fun getString(@PropertyKey(resourceBundle = BUNDLE_PATH) key: String, vararg params: Any): String {
    return StringsBundle.getMessage(key, *params)
}

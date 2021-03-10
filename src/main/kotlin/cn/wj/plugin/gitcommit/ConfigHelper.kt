package cn.wj.plugin.gitcommit

object ConfigHelper {

    var typeList = ChangeType.DEFAULT_LIST

    val DEFAULT: ChangeType
        get() = typeList[0]

    var dialog: DialogEntity = DialogEntity()

    fun findChangeType(typeStr: String): ChangeType {
        return typeList.firstOrNull {
            it.action.equals(typeStr, true)
        } ?: DEFAULT
    }
}
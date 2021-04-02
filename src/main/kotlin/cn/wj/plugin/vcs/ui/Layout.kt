package cn.wj.plugin.vcs.ui

import com.intellij.util.ui.JBUI
import net.miginfocom.layout.CC
import net.miginfocom.layout.LC
import net.miginfocom.swing.MigLayout
import java.awt.Color
import javax.swing.UIManager
import javax.swing.border.Border

fun migLayout(block: LC.() -> Unit = {}): MigLayout {
    val lc = LC().fill().gridGap("0!", "0!").insets("0")
    lc.block()
    return MigLayout(lc)
}

fun migLayoutVertical(block: LC.() -> Unit = {}): MigLayout {
    val lc = LC().flowY().fill().gridGap("0!", "0!").insets("0")
    lc.block()
    return MigLayout(lc)
}

fun fill(): CC = CC().grow().push()

fun fillX(): CC = CC().growX().pushX()
fun fillY(): CC = CC().growY().pushY()

fun wrap(): CC = CC().wrap()

fun emptyBorder(topAndBottom: Int, leftAndRight: Int) = JBUI.Borders.empty(topAndBottom, leftAndRight)

fun emptyBorder(offsets: Int) = JBUI.Borders.empty(offsets)

fun lineAbove(): Border = JBUI.Borders.customLine(getBordersColor(), 1, 0, 0, 0)

fun lineBelow(): Border = JBUI.Borders.customLine(getBordersColor(), 0, 0, 1, 0)

fun lineToRight(): Border = JBUI.Borders.customLine(getBordersColor(), 0, 0, 0, 1)

operator fun Border.plus(external: Border): Border = JBUI.Borders.merge(this, external, true)

fun getBordersColor(default: Color? = null): Color? = UIManager.getColor("Borders.color") ?: default

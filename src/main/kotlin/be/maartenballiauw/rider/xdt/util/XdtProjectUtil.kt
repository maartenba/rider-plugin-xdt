package be.maartenballiauw.rider.xdt.util

import com.jetbrains.rider.projectView.nodes.ProjectModelNode

class XdtProjectUtil {
    companion object {
        fun isConfigFile(item: ProjectModelNode?) : Boolean =
                item != null && item.name.endsWith(".config") && item.name.count { it == '.' } == 1

        fun isConfigTransform(item: ProjectModelNode?) : Boolean =
                item != null && item.name.endsWith(".config") && item.name.count { it == '.' } > 1

        fun isProbablyRelated(item: ProjectModelNode, other: ProjectModelNode) : Boolean {
            val itemStem = item.name.substringBefore('.')
            val otherStem = other.name.substringBefore('.')

            return itemStem == otherStem
        }
    }
}
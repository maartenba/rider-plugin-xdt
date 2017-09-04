package be.hikage.xdt4j.util

import org.dom4j.Element

object XmlUtils {
    fun findIndexOfElementInChildren(containerElement: Element, markedElement: Element): Int =
            (containerElement.elements() as List<*>)
                    .takeWhile { it !== markedElement }
                    .count()
}
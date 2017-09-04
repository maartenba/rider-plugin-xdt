package be.hikage.xdt4j.transform

import be.hikage.xdt4j.locator.LocatorUtils
import be.hikage.xdt4j.util.XmlUtils
import com.intellij.openapi.diagnostic.Logger
import org.dom4j.Document
import org.dom4j.Element

@Suppress("unused")
class InsertAfterTransform(
        workingDocument: Document,
        transformElement: Element,
        arguments: String?)
    : AbstractFirstChildBasedTransform(
        workingDocument,
        transformElement,
        arguments) {

    override fun processElement(targetElement: Element) {
        val localPath = relativeXPath

        val markedElement = targetElement.selectSingleNode(localPath) as Element
        val indexOfMarkerElement = XmlUtils.findIndexOfElementInChildren(targetElement, markedElement)

        if (indexOfMarkerElement == targetElement.elements().size - 1)
            targetElement.add(transformElementCopy)
        else
            targetElement.elements().add(indexOfMarkerElement + 1, transformElementCopy)
    }

    override fun getSelectionQuery(): String {
        return LocatorUtils.generateSpecificXPath(transformElement.parent)
    }

    private val relativeXPath: String
        get() {
            var localPath = arguments!!
            if (localPath.startsWith(transformElement.parent.path)) {
                localPath = localPath.substring(transformElement.parent.path.length + 1)
                LOG.debug("Simplify Path : {$arguments} -> {$localPath}")
            }

            return localPath
        }

    companion object {
        private val LOG = Logger.getInstance(InsertAfterTransform::class.java)
    }
}

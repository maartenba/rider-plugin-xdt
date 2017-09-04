package be.hikage.xdt4j.transform

import com.intellij.openapi.diagnostic.Logger
import org.dom4j.Document
import org.dom4j.Element

@Suppress("unused")
class ReplaceTransform(
        workingDocument: Document,
        transformElement: Element,
        arguments: String?)
    : AbstractFirstChildBasedTransform(
        workingDocument,
        transformElement,
        arguments) {

    override fun processElement(targetElement: Element) {
        if (LOG.isDebugEnabled)
            LOG.debug("Replace element : {${targetElement.path}}")

        replaceElement(targetElement, transformElementCopy)
    }

    override fun getSelectionQuery(): String {
        return xPath
    }

    private fun replaceElement(toReplace: Element, transformElement: Element) {
        toReplace.clearContent()
        toReplace.setAttributes(transformElement.attributes())
        toReplace.setContent(transformElement.content())
    }

    companion object {
        private val LOG = Logger.getInstance(ReplaceTransform::class.java)
    }
}

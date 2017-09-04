package be.hikage.xdt4j.transform

import be.hikage.xdt4j.locator.LocatorUtils
import com.intellij.openapi.diagnostic.Logger
import org.dom4j.Document
import org.dom4j.Element

@Suppress("unused")
class InsertIfMissingTransform(
        workingDocument: Document,
        transformElement: Element,
        arguments: String?)
    : AbstractFirstChildBasedTransform(
        workingDocument,
        transformElement,
        arguments) {

    override fun processElement(targetElement: Element) {
        if (targetElement.elements().count() == 0 || !targetElement.elements().any { it.matches(xPath) }) {
            targetElement.add(transformElementCopy)
        }
    }

    override fun getSelectionQuery(): String {
        return LocatorUtils.generateSpecificXPath(transformElement.parent)
    }

    companion object {
        private val LOG = Logger.getInstance(InsertIfMissingTransform::class.java)
    }
}
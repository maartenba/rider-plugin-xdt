package be.hikage.xdt4j.transform

import be.hikage.xdt4j.locator.LocatorUtils
import com.intellij.openapi.diagnostic.Logger
import org.dom4j.Document
import org.dom4j.Element

open class RemoveTransform : AbstractXPathSelectionBaseTransform {
    @Suppress("unused")
    constructor(workingDocument: Document, transformElement: Element, arguments: String?) : super(
            workingDocument,
            transformElement,
            arguments,
            AbstractXPathSelectionBaseTransform.ProcessChildenStrategy.FIRST)

    constructor(workingDocument: Document, transformElement: Element, arguments: String?, strategy: AbstractXPathSelectionBaseTransform.ProcessChildenStrategy) : super(
            workingDocument,
            transformElement,
            arguments,
            strategy)

    override fun processElement(targetElement: Element) {
        if (LOG.isDebugEnabled)
            LOG.debug("Removing element : {${targetElement.path}}")

        targetElement.parent.remove(targetElement)
    }

    override fun getSelectionQuery(): String {
        return LocatorUtils.generateSpecificXPath(transformElement)
    }

    companion object {
        private val LOG = Logger.getInstance(RemoveTransform::class.java)
    }
}

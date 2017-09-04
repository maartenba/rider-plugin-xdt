package be.hikage.xdt4j.transform

import com.intellij.openapi.diagnostic.Logger
import org.dom4j.Document
import org.dom4j.Element

abstract class AbstractXPathSelectionBaseTransform(
        workingDocument: Document,
        transformElement: Element,
        arguments: String?,

        private val processChildenStrategy: ProcessChildenStrategy)
    : Transform(
        workingDocument,
        transformElement,
        arguments) {

    override fun applyInternal() {
        val selectionXPathExpression = getSelectionQuery()
        val targetElements = workingDocument.selectNodes(selectionXPathExpression)

        if (!targetElements.isEmpty()) {
            processSelection(targetElements as List<Element>)
        } else
            LOG.warn("XPath {$selectionXPathExpression} don't match in working document, cannot process")
    }

    private fun processSelection(targetElements: List<Element>) {
        if (processChildenStrategy == ProcessChildenStrategy.FIRST)
            processElement(targetElements[0])
        else
            for (element in targetElements)
                processElement(element)
    }

    protected abstract fun processElement(targetElement: Element)

    protected abstract fun getSelectionQuery(): String

    enum class ProcessChildenStrategy {
        FIRST, EACH
    }

    companion object {
        private val LOG = Logger.getInstance(AbstractXPathSelectionBaseTransform::class.java)
    }
}

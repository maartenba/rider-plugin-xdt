package be.hikage.xdt4j.transform

import com.intellij.openapi.diagnostic.Logger
import org.dom4j.Document
import org.dom4j.Element

@Suppress("unused")
class InsertChildrenTransform(
        workingDocument: Document,
        transformElement: Element,
        arguments: String?)
    : Transform(
        workingDocument,
        transformElement,
        arguments) {

    override fun applyInternal() {
        val elementsToInsert = transformElement.elements()

        val targetElement = workingDocument.selectSingleNode(xPath) as Element

        elementsToInsert.forEach {
            targetElement.add((it as Element).createCopy())
        }
    }

    companion object {
        private val LOG = Logger.getInstance(InsertChildrenTransform::class.java)
    }
}

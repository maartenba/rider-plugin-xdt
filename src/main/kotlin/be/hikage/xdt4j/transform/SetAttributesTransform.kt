package be.hikage.xdt4j.transform

import com.intellij.openapi.diagnostic.Logger
import org.dom4j.Attribute
import org.dom4j.Document
import org.dom4j.Element

@Suppress("unused")
class SetAttributesTransform(
        workingDocument: Document,
        transformElement: Element,
        arguments: String?)
    : AbstractAllChildBasedTransform(
        workingDocument,
        transformElement,
        arguments) {

    override fun processElement(targetElement: Element) {
        (transformElementCopy.attributes() as List<Attribute>)
                .filter { mustBeSet(it.qName.name) }
                .forEach { targetElement.addAttribute(it.qName, it.value) }
    }

    override fun getSelectionQuery(): String {
        return xPath
    }

    private fun mustBeSet(name: String): Boolean {
        return if (arguments == null || arguments!!.isEmpty()) true else name == arguments
    }

    companion object {
        private val LOG = Logger.getInstance(SetAttributesTransform::class.java)
    }
}

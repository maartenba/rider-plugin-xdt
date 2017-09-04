package be.hikage.xdt4j.transform

import org.dom4j.Attribute
import org.dom4j.Document
import org.dom4j.Element

@Suppress("unused")
class RemoveAttributesTransform(
        workingDocument: Document,
        transformElement: Element,
        arguments: String?)
    : AbstractAllChildBasedTransform(
        workingDocument,
        transformElement,
        arguments) {

    override fun processElement(targetElement: Element) {
        (transformElementCopy.attributes() as List<Attribute>)
                .filter { mustBeRemove(it.qName.name) }
                .map { targetElement.attribute(it.qName) }
                .forEach { targetElement.remove(it) }
    }

    override fun getSelectionQuery(): String {
        return xPath
    }

    private fun mustBeRemove(name: String): Boolean {
        return if (arguments == null || arguments!!.isEmpty()) true else name == arguments
    }
}

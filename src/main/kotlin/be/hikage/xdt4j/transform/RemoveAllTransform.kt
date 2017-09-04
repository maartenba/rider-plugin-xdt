package be.hikage.xdt4j.transform

import org.dom4j.Document
import org.dom4j.Element

@Suppress("unused")
class RemoveAllTransform(
        workingDocument: Document,
        transformElement: Element,
        arguments: String?)
    : RemoveTransform(
        workingDocument,
        transformElement,
        arguments,
        AbstractXPathSelectionBaseTransform.ProcessChildenStrategy.EACH)

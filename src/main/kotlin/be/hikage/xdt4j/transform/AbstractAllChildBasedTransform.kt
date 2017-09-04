package be.hikage.xdt4j.transform

import org.dom4j.Document
import org.dom4j.Element

abstract class AbstractAllChildBasedTransform(
        workingDocument: Document,
        transformElement: Element,
        arguments: String?)
    : AbstractXPathSelectionBaseTransform(
        workingDocument,
        transformElement,
        arguments,
        AbstractXPathSelectionBaseTransform.ProcessChildenStrategy.EACH)
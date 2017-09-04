package be.hikage.xdt4j.locator

import be.hikage.xdt4j.XdtException
import org.dom4j.Element

class XPathLocator(parameter: String?) : Locator(parameter) {
    init {
        if (parameter == null || parameter.isEmpty())
            throw XdtException("Parameter is mandatory for XPathLocator")
    }

    override fun generateXPath(target: Element): String {
        return parameter!!
    }

    override fun generateCondition(target: Element): String {
        throw UnsupportedOperationException("XPathLocator do not allow condition to relative element")
    }
}

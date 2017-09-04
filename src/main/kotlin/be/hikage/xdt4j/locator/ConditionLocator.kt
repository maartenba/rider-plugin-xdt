package be.hikage.xdt4j.locator

import be.hikage.xdt4j.XdtException
import org.dom4j.Element

/**
 * Locator which is based on an relative XPath query on to create the selection
 */
class ConditionLocator(parameter: String?) : Locator(parameter) {
    init {
        if (parameter == null || parameter.isEmpty())
            throw XdtException("Parameter is mandatory for ConditionLocator")
    }

    override fun generateXPath(target: Element): String {
        return target.path + generateCondition(target)
    }

    override fun generateCondition(target: Element): String {
        return "[$parameter]"
    }


}

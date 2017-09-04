package be.hikage.xdt4j.locator

import org.dom4j.Element

abstract class Locator(parameter: String?) {
    var parameter: String?
        protected set

    init {
        this.parameter = parameter
    }

    /**
     * Generate a XPath for the privided Element with the correct selection query
     * accoding the Locator condition
     *
     * @param target The Element
     * @return The XPath as a String
     */
    abstract fun generateXPath(target: Element): String

    /**
     * Generate the selection query that match the provided element
     * according the Locator condition.
     * This method may thrown an UnsupportedOperationException if not supported
     *
     * @param target The Element
     * @return The selection query in an XPath format@
     * @throws UnsupportedOperationException if not supported
     */
    abstract fun generateCondition(target: Element): String
}

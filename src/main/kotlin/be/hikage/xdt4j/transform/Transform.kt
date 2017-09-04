package be.hikage.xdt4j.transform

import be.hikage.xdt4j.XdtConstants
import be.hikage.xdt4j.locator.LocatorFactory
import com.intellij.openapi.diagnostic.Logger
import org.dom4j.Attribute
import org.dom4j.Document
import org.dom4j.Element

import java.util.ArrayList

abstract class Transform(
        protected var workingDocument: Document,
        protected var transformElement: Element,
        protected var arguments: String?) {

    fun apply() {
        if (LOG.isTraceEnabled)
            LOG.trace("Before applying {${javaClass.name}}: {${workingDocument.asXML()}}")

        applyInternal()

        if (LOG.isTraceEnabled)
            LOG.trace("After applying {${javaClass.name}}: {${workingDocument.asXML()}}")
    }

    protected abstract fun applyInternal()

    protected val transformElementCopy: Element
        get() {
            val tempElement = transformElement.createCopy()
            val newAttributes = (tempElement.attributes() as List<Attribute>)
                    .filterTo(ArrayList()) { XdtConstants.XDT_NAMESPACE != it.namespaceURI }

            tempElement.setAttributes(newAttributes)

            return tempElement
        }

    protected val xPath: String
        get() {
            val locator = LocatorFactory.createLocator(transformElement)
            val xpath = locator?.generateXPath(transformElement)
                    ?: transformElement.path

            LOG.debug("XPath outcome of Locator processing : {$xpath}")

            return xpath
        }

    companion object {
        private val LOG = Logger.getInstance(Transform::class.java)
    }
}

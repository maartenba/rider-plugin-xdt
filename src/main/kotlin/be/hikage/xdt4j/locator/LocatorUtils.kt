package be.hikage.xdt4j.locator

import com.google.common.base.Joiner
import com.google.common.collect.Lists
import com.intellij.openapi.diagnostic.Logger
import org.dom4j.Element

import java.util.ArrayList

object LocatorUtils {
    private val LOG = Logger.getInstance(LocatorUtils::class.java)

    /**
     * Create an specific XPath to the provided Element by using the Locators information
     *
     * @param currentElement
     * @return an specific XPath that point to the element
     * @throws be.hikage.xdt4j.XdtException can be thrown if the hierarchy of the element present an Locator
     * that does not allow call to {@see Locator.generateCondition}
     */
    fun generateSpecificXPath(currentElement: Element?): String {
        var currentElement = currentElement
        val xpathParts = ArrayList<String>()

        while (currentElement != null) {
            val locator = LocatorFactory.createLocator(currentElement)
            if (locator != null) {
                val currentNode = currentElement.getPath(currentElement.parent)
                xpathParts.add(currentNode + locator.generateCondition(currentElement))
            } else {
                xpathParts.add(currentElement.getPath(currentElement.parent))
            }
            currentElement = currentElement.parent
        }

        val resultXPath = Joiner.on("/").join(Lists.reverse(xpathParts))

        LOG.debug("XPath outcome of Locators processing : {$resultXPath}")
        return resultXPath
    }
}
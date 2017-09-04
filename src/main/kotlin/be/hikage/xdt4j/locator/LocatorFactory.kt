package be.hikage.xdt4j.locator

import be.hikage.xdt4j.XdtException
import com.intellij.openapi.diagnostic.Logger
import org.dom4j.Element

import java.util.regex.Matcher
import java.util.regex.Pattern

object LocatorFactory {
    private val LOG = Logger.getInstance(LocatorFactory::class.java)

    private val LOCATOR_VALIDATOR_PATTERN = Pattern.compile("(\\w*)(\\((.*)\\))?")

    /**
     * Returns an instance corresponding to the value of the attribute "Locator" of the element parameter, or null if the attribute is empty or invalid.
     * In case of the attribute value is not in a valid format, an XdtException will be thrown
     *
     * @param element The Element for which a Locator must be created
     * @return tine [Locator] instance, or null
     * @throws XdtException If the attribute is not in a valid format
     */
    fun createLocator(element: Element): Locator? {
        val locatorAttributeValue = element.attributeValue("Locator", null) ?: return null

        val locatorMatcher = extractArgumentAndValidate(locatorAttributeValue)

        val locatorType = locatorMatcher.group(1)
        val locatorParameter = locatorMatcher.group(3)

        if (LOG.isDebugEnabled) {
            LOG.debug("Locator Type :  {}", locatorType)
            LOG.debug("Locator Parameter : {}", locatorParameter)
        }

        return instantiateImplementation(locatorType, locatorParameter)
    }

    private fun instantiateImplementation(locatorType: String, locatorParameter: String?): Locator? {
        return when (locatorType) {
            "Condition" -> ConditionLocator(locatorParameter)
            "Match" -> MatchLocator(locatorParameter)
            "XPath" -> XPathLocator(locatorParameter)
            else -> null
        }
    }

    private fun extractArgumentAndValidate(locatorAttributeValue: String): Matcher {
        val locatorMatcher = LOCATOR_VALIDATOR_PATTERN.matcher(locatorAttributeValue)

        if (!locatorMatcher.matches())
            throw XdtException("The Transform Attributes value is invalid " + locatorAttributeValue)

        return locatorMatcher
    }
}

package be.hikage.xdt4j.locator

import be.hikage.xdt4j.XdtConstants
import be.hikage.xdt4j.XdtException
import org.dom4j.DocumentHelper
import org.dom4j.Element
import org.dom4j.QName
import org.junit.Assert.*
import org.junit.Test

class LocatorFactoryTest {
    @Test
    fun testMatchLocator() {
        val element = createDocument("Match")

        val result = LocatorFactory.createLocator(element)
        assertNotNull(result)
        assertTrue("Result is not a MatchLocator", result is MatchLocator)
        assertNull("Argument must be null", result!!.parameter)
    }

    @Test
    fun testMatchLocatorParam() {
        val element = createDocument("Match(key)")

        val result = LocatorFactory.createLocator(element)
        assertNotNull(result)
        assertTrue("Result is not a MatchLocator", result is MatchLocator)
        assertEquals("key", result!!.parameter)
    }

    @Test
    fun testMatchLocatorParams() {
        val element = createDocument("Match(key,key2)")

        val result = LocatorFactory.createLocator(element)
        assertNotNull(result)
        assertTrue("Result is not a MatchLocator", result is MatchLocator)
        assertEquals("key,key2", result!!.parameter)
    }

    @Test(expected = XdtException::class)
    fun testConditionLocator() {
        val element = createDocument("Condition")

        val result = LocatorFactory.createLocator(element)
    }

    @Test
    fun testConditionLocatorParam() {
        val element = createDocument("Condition(key)")

        val result = LocatorFactory.createLocator(element)
        assertNotNull(result)
        assertTrue("Result is not a ConditionLocator", result is ConditionLocator)
        assertEquals("key", result!!.parameter)
    }

    @Test(expected = XdtException::class)
    fun testXPathLocator() {
        val element = createDocument("XPath")

        val result = LocatorFactory.createLocator(element)
    }

    @Test
    fun testXPathLocatorParam() {
        val element = createDocument("XPath(key)")

        val result = LocatorFactory.createLocator(element)
        assertNotNull(result)
        assertTrue("Result is not a XPath", result is XPathLocator)
        assertEquals("key", result!!.parameter)
    }

    @Test(expected = XdtException::class)
    fun testInvalidLocator() {
        val element = createDocument("#######")

        val result = LocatorFactory.createLocator(element)
        assertNull("Unknown validator must be return null", result)
    }

    @Test
    fun testUnknownLocator() {
        val element = createDocument("Unknown(key)")

        val result = LocatorFactory.createLocator(element)
        assertNull("Unknown validator must be return null", result)
    }

    private fun createDocument(locatorType: String): Element {
        val document = DocumentHelper.createDocument()
        val root = DocumentHelper.createElement("root")

        document.rootElement = root

        val child = DocumentHelper.createElement("child")

        child.addAttribute(QName.get("Locator", XdtConstants.XDT_NAMESPACE), locatorType)

        return child
    }
}
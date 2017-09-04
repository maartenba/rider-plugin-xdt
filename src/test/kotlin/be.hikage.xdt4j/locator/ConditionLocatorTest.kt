package be.hikage.xdt4j.locator

import be.hikage.xdt4j.XdtException
import be.hikage.xdt4j.util.TestUtils
import org.intellij.lang.annotations.Language
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class ConditionLocatorTest {
    @Test(expected = XdtException::class)
    fun testMatchLocatorNoParameter() {
        @Language("XML")
        val xmlDoc = "<configuration xmlns:xdt=\"http://schemas.microsoft.com/XML-Document-Transform\">\n    <appSettings>\n        <add key=\"key2\" value=\"value2-live\"/>\n    </appSettings>\n</configuration>"

        val addElement = TestUtils.loadElement(xmlDoc, "/configuration/appSettings/add")

        val matchLocator = ConditionLocator(null)
    }

    @Test
    fun testMatchLocatorSingleParameter() {
        @Language("XML")
        val xmlDoc = "<configuration xmlns:xdt=\"http://schemas.microsoft.com/XML-Document-Transform\">\n    <appSettings>\n        <add key=\"key2\" value=\"value2-live\"/>\n    </appSettings>\n</configuration>"

        val addElement = TestUtils.loadElement(xmlDoc, "/configuration/appSettings/add")

        val matchLocator = ConditionLocator("@value=\"value2-live\"")

        val resultXPath = matchLocator.generateXPath(addElement)
        assertNotNull(resultXPath)
        assertEquals("/configuration/appSettings/add[@value=\"value2-live\"]", resultXPath)
    }
}
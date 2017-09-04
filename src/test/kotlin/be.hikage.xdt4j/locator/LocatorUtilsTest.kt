package be.hikage.xdt4j.locator

import be.hikage.xdt4j.util.TestUtils
import org.intellij.lang.annotations.Language
import org.junit.Test

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull

class LocatorUtilsTest {
    @Test
    fun testUniqueXPathFromLocator_nolocatorinfo() {
        @Language("XML")
        val xmlDoc = "<configuration xmlns:xdt=\"http://schemas.microsoft.com/XML-Document-Transform\">\n    <appSettings id=\"1\">\n        <add key=\"key1\" value=\"value1\"/>\n    </appSettings>\n    <appSettings id=\"2\">\n        <add key=\"key2\" value=\"value2\"/>\n    </appSettings>\n</configuration>"

        val addElement = TestUtils.loadElement(xmlDoc, "/configuration/appSettings[@id='2']/add")

        val expectedXPath = LocatorUtils.generateSpecificXPath(addElement)

        assertNotNull(expectedXPath)
        assertEquals("/configuration/appSettings/add", expectedXPath)
    }

    @Test
    fun testUniqueXPathFromLocator_matchlocator() {
        @Language("XML")
        val xmlDoc = "<configuration xmlns:xdt=\"http://schemas.microsoft.com/XML-Document-Transform\">\n    <appSettings id=\"1\">\n        <add key=\"key1\" value=\"value1\"/>\n    </appSettings>\n    <appSettings id=\"2\" xdt:Locator=\"Match(id)\">\n        <add key=\"key2\" value=\"value2\"/>\n    </appSettings>\n</configuration>"

        val addElement = TestUtils.loadElement(xmlDoc, "/configuration/appSettings[@id=\"2\"]/add")

        val expectedXPath = LocatorUtils.generateSpecificXPath(addElement)

        assertNotNull(expectedXPath)
        assertEquals("/configuration/appSettings[@id=\"2\"]/add", expectedXPath)
    }

    @Test
    fun testUniqueXPathFromLocator_conditionlocator() {
        @Language("XML")
        val xmlDoc = "<configuration xmlns:xdt=\"http://schemas.microsoft.com/XML-Document-Transform\">\n    <appSettings id=\"1\">\n        <add key=\"key1\" value=\"value1\"/>\n    </appSettings>\n    <appSettings id=\"2\" xdt:Locator=\"Condition(@id='2')\">\n        <add key=\"key2\" value=\"value2\"/>\n    </appSettings>\n</configuration>"

        val addElement = TestUtils.loadElement(xmlDoc, "/configuration/appSettings[@id=\"2\"]/add")

        val expectedXPath = LocatorUtils.generateSpecificXPath(addElement)

        assertNotNull(expectedXPath)
        assertEquals("/configuration/appSettings[@id='2']/add", expectedXPath)
    }


    @Test(expected = UnsupportedOperationException::class)
    fun testUniqueXPathFromLocator_conditionxpath() {
        @Language("XML")
        val xmlDoc = "<configuration xmlns:xdt=\"http://schemas.microsoft.com/XML-Document-Transform\">\n    <appSettings id=\"1\">\n        <add key=\"key1\" value=\"value1\"/>\n    </appSettings>\n    <appSettings id=\"2\" xdt:Locator=\"XPath(/)\">\n        <add key=\"key2\" value=\"value2\"/>\n    </appSettings>\n</configuration>"

        val addElement = TestUtils.loadElement(xmlDoc, "/configuration/appSettings[@id=\"2\"]/add")

        val expectedXPath = LocatorUtils.generateSpecificXPath(addElement)

        assertNotNull(expectedXPath)
        assertEquals("/configuration/appSettings[@id='2']/add", expectedXPath)
    }
}
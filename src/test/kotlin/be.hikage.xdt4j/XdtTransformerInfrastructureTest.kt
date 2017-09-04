package be.hikage.xdt4j

import be.hikage.xdt4j.util.TestUtils
import be.hikage.xdt4j.util.XMLAssert
import org.intellij.lang.annotations.Language
import org.junit.Test

class XdtTransformerInfrastructureTest : XdtTransformerTestBase() {
    @Test(expected = XdtException::class)
    @Throws(Exception::class)
    fun TestInvalidTransform() {
        @Language("XML")
        val transformInstruction = "<configuration xmlns:xdt=\"http://schemas.microsoft.com/XML-Document-Transform\">\n    <test xdt:Transform=\"..test))(()()(\"/>\n    \n</configuration>"
        val transformDocument = TestUtils.loadXmlFromString(transformInstruction)

        val transformer = XdtTransformer()
        val result = transformer.transform(baseDocument!!, transformDocument)

        XMLAssert.assertXMLEqual(baseDocument!!.asXML(), result.asXML())
    }

    @Test(expected = XdtException::class)
    @Throws(Exception::class)
    fun TestUnknownValidator() {
        @Language("XML")
        val transformInstruction = "<configuration xmlns:xdt=\"http://schemas.microsoft.com/XML-Document-Transform\">\n    <test xdt:Transform=\"Unknown\"/>\n    \n</configuration>"
        val transformDocument = TestUtils.loadXmlFromString(transformInstruction)

        val transformer = XdtTransformer()
        val result = transformer.transform(baseDocument!!, transformDocument)

        XMLAssert.assertXMLEqual(baseDocument!!.asXML(), result.asXML())
    }

    @Test
    @Throws(Exception::class)
    fun TestIdentityTransform() {
        @Language("XML")
        val transformInstruction = "<configuration xmlns:xdt=\"http://schemas.microsoft.com/XML-Document-Transform\" />"
        val transformDocument = TestUtils.loadXmlFromString(transformInstruction)

        val transformer = XdtTransformer()
        val result = transformer.transform(baseDocument!!, transformDocument)

        XMLAssert.assertXMLEqual(baseDocument!!.asXML(), result.asXML())
    }
}
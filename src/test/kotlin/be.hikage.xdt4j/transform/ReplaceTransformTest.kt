package be.hikage.xdt4j.transform

import be.hikage.xdt4j.XdtTransformer
import be.hikage.xdt4j.XdtTransformerTestBase
import be.hikage.xdt4j.util.TestUtils
import be.hikage.xdt4j.util.XMLAssert
import org.intellij.lang.annotations.Language
import org.junit.Test

class ReplaceTransformTest : XdtTransformerTestBase() {
    @Test
    @Throws(Exception::class)
    fun TestReplaceTransform() {
        @Language("XML")
        val transformInstruction = "<configuration xmlns:xdt=\"http://schemas.microsoft.com/XML-Document-Transform\">\n    <system.web xdt:Transform=\"Replace\">\n        <extra content=\"here\"/>\n    </system.web>\n</configuration>"
        val transformDocument = TestUtils.loadXmlFromString(transformInstruction)

        val transformer = XdtTransformer()
        val result = transformer.transform(baseDocument!!, transformDocument)

        XMLAssert.assertXpathEvaluatesTo("5", "count(/configuration/*)", result.asXML())
        XMLAssert.assertXpathEvaluatesTo("here", "/configuration/system.web/extra/@content", result.asXML())
    }
}
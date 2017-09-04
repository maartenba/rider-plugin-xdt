package be.hikage.xdt4j.transform

import be.hikage.xdt4j.XdtTransformer
import be.hikage.xdt4j.XdtTransformerTestBase
import be.hikage.xdt4j.util.TestUtils
import be.hikage.xdt4j.util.XMLAssert
import org.intellij.lang.annotations.Language
import org.junit.Test

class InsertBeforeTransformTest : XdtTransformerTestBase() {
    @Test
    @Throws(Exception::class)
    fun TestInsertBeforeTransform() {
        @Language("XML")
        val transformInstruction = "<configuration xmlns:xdt=\"http://schemas.microsoft.com/XML-Document-Transform\">\n    <appSettings>\n        <add key=\"key2.5\" value=\"value2.5\" xdt:Transform=\"InsertBefore(/configuration/appSettings/add[@key='key3'])\"/>\n    </appSettings>\n</configuration>"
        val transformDocument = TestUtils.loadXmlFromString(transformInstruction)

        val transformer = XdtTransformer()
        val result = transformer.transform(baseDocument!!, transformDocument)

        XMLAssert.assertXpathEvaluatesTo("4", "count(/configuration/appSettings/add)", result.asXML())
        XMLAssert.assertXpathEvaluatesTo("value2.5", "/configuration/appSettings/add[@key=\"key2.5\"]/@value", result.asXML())
    }
}
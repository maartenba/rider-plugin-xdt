package be.hikage.xdt4j.transform

import be.hikage.xdt4j.XdtTransformer
import be.hikage.xdt4j.XdtTransformerTestBase
import be.hikage.xdt4j.util.TestUtils
import be.hikage.xdt4j.util.XMLAssert
import org.intellij.lang.annotations.Language
import org.junit.Test

class InsertChildrenTransformTest : XdtTransformerTestBase() {
    @Test
    @Throws(Exception::class)
    fun TestInsertChildrenTransform() {
        @Language("XML")
        val transformInstruction = "<configuration xmlns:xdt=\"http://schemas.microsoft.com/XML-Document-Transform\">\n    <appSettings xdt:Transform=\"InsertChildren\">\n        <add key=\"key4\" value=\"value4\"/>\n        <add key=\"key5\" value=\"value5\"/>\n    </appSettings>\n</configuration>"
        val transformDocument = TestUtils.loadXmlFromString(transformInstruction)

        val transformer = XdtTransformer()
        val result = transformer.transform(baseDocument!!, transformDocument)

        XMLAssert.assertXpathEvaluatesTo("5", "count(/configuration/appSettings/add)", result.asXML())
        XMLAssert.assertXpathEvaluatesTo("value4", "/configuration/appSettings/add[@key=\"key4\"]/@value", result.asXML())
        XMLAssert.assertXpathEvaluatesTo("value5", "/configuration/appSettings/add[@key=\"key5\"]/@value", result.asXML())
    }
}
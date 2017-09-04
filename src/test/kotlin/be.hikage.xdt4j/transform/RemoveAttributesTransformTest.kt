package be.hikage.xdt4j.transform

import be.hikage.xdt4j.XdtTransformer
import be.hikage.xdt4j.XdtTransformerTestBase
import be.hikage.xdt4j.util.TestUtils
import be.hikage.xdt4j.util.XMLAssert
import org.intellij.lang.annotations.Language
import org.junit.Test

class RemoveAttributesTransformTest : XdtTransformerTestBase() {
    @Test
    @Throws(Exception::class)
    fun TestRemoveAttributesTransformWithArguments() {
        @Language("XML")
        val transformInstruction = "<configuration xmlns:xdt=\"http://schemas.microsoft.com/XML-Document-Transform\">\n    <system.web>\n        <compilation debug=\"false\" xdt:Transform=\"RemoveAttributes(debug)\"/>\n    </system.web>\n</configuration>"
        val transformDocument = TestUtils.loadXmlFromString(transformInstruction)

        val transformer = XdtTransformer()
        val result = transformer.transform(baseDocument!!, transformDocument)

        XMLAssert.assertXpathEvaluatesTo("", "/configuration/system.web/compilation/@debug", result.asXML())
    }

    @Test
    @Throws(Exception::class)
    fun TestRemoveAttributesTransform() {
        @Language("XML")
        val transformInstruction = "<configuration xmlns:xdt=\"http://schemas.microsoft.com/XML-Document-Transform\">\n    <system.web>\n        <compilation debug=\"false\" xdt:Transform=\"RemoveAttributes\"/>\n    </system.web>\n</configuration>"
        val transformDocument = TestUtils.loadXmlFromString(transformInstruction)

        val transformer = XdtTransformer()
        val result = transformer.transform(baseDocument!!, transformDocument)

        XMLAssert.assertXpathEvaluatesTo("", "/configuration/system.web/compilation/@debug", result.asXML())
    }

    @Test
    @Throws(Exception::class)
    fun TestRemoveAttributesTransformWithMultipleElementsThatMatch() {
        @Language("XML")
        val transformInstruction = "<configuration xmlns:xdt=\"http://schemas.microsoft.com/XML-Document-Transform\">\n    <appSettings>\n        <add value=\"newValue\" xdt:Transform=\"RemoveAttributes\"/>\n    </appSettings>\n</configuration>"
        val transformDocument = TestUtils.loadXmlFromString(transformInstruction)

        val transformer = XdtTransformer()
        val result = transformer.transform(baseDocument!!, transformDocument)

        XMLAssert.assertXpathEvaluatesTo("", "/configuration/appSettings/add[@key=\"key1\"]/@value", result.asXML())
        XMLAssert.assertXpathEvaluatesTo("", "/configuration/appSettings/add[@key=\"key2\"]/@value", result.asXML())
    }
}
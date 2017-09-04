package be.hikage.xdt4j.transform

import be.hikage.xdt4j.XdtTransformer
import be.hikage.xdt4j.XdtTransformerTestBase
import be.hikage.xdt4j.util.TestUtils
import be.hikage.xdt4j.util.XMLAssert
import org.intellij.lang.annotations.Language
import org.junit.Test

class SetAttributesTransformTest : XdtTransformerTestBase() {
    @Test
    @Throws(Exception::class)
    fun TestSetAttributesTransform() {
        @Language("XML")
        val transformInstruction = "<configuration xmlns:xdt=\"http://schemas.microsoft.com/XML-Document-Transform\">\n    <system.web>\n        <compilation debug=\"false\" xdt:Transform=\"SetAttributes\"/>\n    </system.web>\n</configuration>"
        val transformDocument = TestUtils.loadXmlFromString(transformInstruction)

        val transformer = XdtTransformer()
        val result = transformer.transform(baseDocument!!, transformDocument)

        XMLAssert.assertXpathEvaluatesTo("false", "/configuration/system.web/compilation/@debug", result.asXML())
    }

    @Test
    @Throws(Exception::class)
    fun TestSetAttributesTransformWithMultipleElementsThatMatch() {
        @Language("XML")
        val transformInstruction = "<configuration xmlns:xdt=\"http://schemas.microsoft.com/XML-Document-Transform\">\n    <appSettings>\n        <add value=\"newValue\" xdt:Transform=\"SetAttributes\"/>\n    </appSettings>\n</configuration>"
        val transformDocument = TestUtils.loadXmlFromString(transformInstruction)

        val transformer = XdtTransformer()
        val result = transformer.transform(baseDocument!!, transformDocument)

        XMLAssert.assertXpathEvaluatesTo("newValue", "/configuration/appSettings/add[@key=\"key1\"]/@value", result.asXML())
        XMLAssert.assertXpathEvaluatesTo("newValue", "/configuration/appSettings/add[@key=\"key2\"]/@value", result.asXML())
    }

    @Test
    @Throws(Exception::class)
    fun TestSetAttributesTransformWithArguments() {
        @Language("XML")
        val transformInstruction = "<configuration xmlns:xdt=\"http://schemas.microsoft.com/XML-Document-Transform\">\n    <system.web>\n        <compilation debug=\"false\" xdt:Transform=\"SetAttributes(debug)\"/>\n    </system.web>\n</configuration>"
        val transformDocument = TestUtils.loadXmlFromString(transformInstruction)

        val transformer = XdtTransformer()
        val result = transformer.transform(baseDocument!!, transformDocument)

        XMLAssert.assertXpathEvaluatesTo("false", "/configuration/system.web/compilation/@debug", result.asXML())
    }
}
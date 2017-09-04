package be.hikage.xdt4j.transform

import be.hikage.xdt4j.XdtTransformer
import be.hikage.xdt4j.XdtTransformerTestBase
import be.hikage.xdt4j.util.TestUtils
import be.hikage.xdt4j.util.XMLAssert
import org.dom4j.DocumentHelper
import org.dom4j.Element
import org.intellij.lang.annotations.Language
import org.junit.Assert
import org.junit.Test
import java.util.*

class ConditionTransformTest : XdtTransformerTestBase() {
    @Test
    @Throws(Exception::class)
    fun TestConditionLocator() {
        @Language("XML")
        val transformInstruction = "<configuration xmlns:xdt=\"http://schemas.microsoft.com/XML-Document-Transform\">\n    <appSettings>\n        <add key=\"key2\" value=\"value2-live\" xdt:Locator=\"Condition(@key='key2')\" xdt:Transform=\"SetAttributes\"/>\n    </appSettings>\n</configuration>"
        val transformDocument = TestUtils.loadXmlFromString(transformInstruction)

        val transformer = XdtTransformer()
        val result = transformer.transform(baseDocument!!, transformDocument)

        XMLAssert.assertXpathEvaluatesTo("value1", "/configuration/appSettings/add[@key=\"key1\"]/@value", result.asXML())
        XMLAssert.assertXpathEvaluatesTo("value2-live", "/configuration/appSettings/add[@key=\"key2\"]/@value", result.asXML())
    }

    @Test
    @Throws(Exception::class)
    fun TestMultipleElementsAreTransformed() {
        @Language("XML")
        val transformInstruction = "<configuration xmlns:xdt=\"http://schemas.microsoft.com/XML-Document-Transform\">\n    <appSettings>\n        <add key=\"key2\" value=\"value2-live\" xdt:Locator=\"Match(key)\" xdt:Transform=\"SetAttributes\"/>\n    </appSettings>\n</configuration>"
        val transformDocument = TestUtils.loadXmlFromString(transformInstruction)

        val transformer = XdtTransformer()
        val result = transformer.transform(baseDocument!!, transformDocument)

        XMLAssert.assertXpathEvaluatesTo("value1", "/configuration/appSettings/add[@key=\"key1\"]/@value", result.asXML())
        XMLAssert.assertXpathEvaluatesTo("value2-live", "/configuration/appSettings/add[@key=\"key2\"]/@value", result.asXML())
    }

    @Test
    @Throws(Exception::class)
    fun TestInputDocumentsWithXmlNamespacesWorkAsExpected() {
        @Language("XML")
        val baseDocumentString = "<configuration>\n    <appSettings>\n        <add key=\"key1\" value=\"value1\"/>\n    </appSettings>\n    <blah xmlns=\"http://test.com\">\n        <add key=\"key2\" value=\"value2\"/>\n    </blah>\n    <flop xmlns=\"http://test.com\">\n        <add key=\"key3\" value=\"value3\" xmlns=\"\"/>\n    </flop>\n</configuration>"
        val baseDocument = TestUtils.loadXmlFromString(baseDocumentString)

        @Language("XML")
        val transformInstruction = "<configuration xmlns:xdt=\"http://schemas.microsoft.com/XML-Document-Transform\">\n    <appSettings>\n        <add value=\"value1-new\" xdt:Transform=\"SetAttributes\"/>\n    </appSettings>\n    <blah xmlns=\"http://test.com\">\n        <add key=\"key2\" value=\"value2-new\" xdt:Locator=\"Match(key)\" xdt:Transform=\"SetAttributes\"/>\n    </blah>\n    <flop xmlns=\"http://test.com\">\n        <add key=\"key3\" value=\"value3-new\" xmlns=\"\" xdt:Locator=\"Match(key)\" xdt:Transform=\"SetAttributes\"/>\n    </flop>\n</configuration>"
        val transformDocument = TestUtils.loadXmlFromString(transformInstruction)

        val transformer = XdtTransformer()
        val result = transformer.transform(baseDocument, transformDocument)

        val nsString = "http://test.com"

        val xPath1 = DocumentHelper.createXPath("/configuration/ns:blah/*[local-name()='add' and @key=\"key2\"]")
        xPath1.setNamespaceURIs(Collections.singletonMap("ns", nsString))

        var foundElement = xPath1.selectSingleNode(result) as Element
        Assert.assertNotNull("No element found", foundElement)
        Assert.assertEquals("Namespace are different", nsString, foundElement.namespaceURI)

        val xPath2 = DocumentHelper.createXPath("/configuration/ns:flop/*[local-name()='add' and @key=\"key3\"]")
        xPath2.setNamespaceURIs(Collections.singletonMap("ns", nsString))

        foundElement = xPath2.selectSingleNode(result) as Element
        Assert.assertNotNull("No Element found 2", foundElement)
        Assert.assertEquals("Namespace are different", "", foundElement.namespaceURI)
    }

    @Test
    @Throws(Exception::class)
    fun TestConditionLocatorRemove() {
        @Language("XML")
        val transformInstruction = "<configuration xmlns:xdt=\"http://schemas.microsoft.com/XML-Document-Transform\">\n    <fileSets>\n        <fileSet xdt:Transform=\"Remove\" xdt:Locator=\"Condition(@id='fileset2')\">\n        </fileSet>\n    </fileSets>\n</configuration>"
        @Language("XML")
        val transformInstruction2 = "<configuration xmlns:xdt=\"http://schemas.microsoft.com/XML-Document-Transform\">\n    <fileSets>\n        <fileSet xdt:Transform=\"Remove\" xdt:Locator=\"Condition(file/text()='myfile3')\">\n        </fileSet>\n    </fileSets>\n</configuration>"
        val transformDocument = TestUtils.loadXmlFromString(transformInstruction)
        val transformDocument2 = TestUtils.loadXmlFromString(transformInstruction)

        val transformer = XdtTransformer()
        val result = transformer.transform(baseDocument!!, transformDocument)
        val result2 = transformer.transform(baseDocument!!, transformDocument2)

        XMLAssert.assertXpathEvaluatesTo("1", "count(/configuration/fileSets/fileSet)", result.asXML())
        XMLAssert.assertXpathEvaluatesTo("fileset1", "/configuration/fileSets/fileSet/@id", result.asXML())

        XMLAssert.assertXpathEvaluatesTo("1", "count(/configuration/fileSets/fileSet)", result2.asXML())
        XMLAssert.assertXpathEvaluatesTo("fileset1", "/configuration/fileSets/fileSet/@id", result2.asXML())
    }
}
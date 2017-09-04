package be.hikage.xdt4j.transform

import be.hikage.xdt4j.XdtTransformer
import be.hikage.xdt4j.XdtTransformerTestBase
import be.hikage.xdt4j.util.TestUtils
import be.hikage.xdt4j.util.XMLAssert
import org.dom4j.DocumentException
import org.dom4j.XPathException
import org.intellij.lang.annotations.Language
import org.junit.Test
import org.xml.sax.SAXException
import java.io.IOException

class RemoveTransformTest : XdtTransformerTestBase() {
    @Test
    @Throws(DocumentException::class, IOException::class, SAXException::class, XPathException::class)
    fun TestRemoveTransform() {
        @Language("XML")
        val transformInstruction = "<configuration xmlns:xdt=\"http://schemas.microsoft.com/XML-Document-Transform\">\n    <system.web xdt:Transform=\"Remove\"/>\n</configuration>"
        val transformDocument = TestUtils.loadXmlFromString(transformInstruction)

        var childrenCountBefore = Integer.valueOf(baseDocument!!.valueOf("count(/configuration/*)"))

        val transformer = XdtTransformer()
        val result = transformer.transform(baseDocument!!, transformDocument)

        XMLAssert.assertXpathEvaluatesTo((--childrenCountBefore).toString(), "count(/configuration/*)", result.asXML())
    }

    @Test
    @Throws(DocumentException::class, IOException::class, SAXException::class, XPathException::class)
    fun TestRemoveTransformWithMultipleElementThatMatch() {
        @Language("XML")
        val transformInstruction = "<configuration xmlns:xdt=\"http://schemas.microsoft.com/XML-Document-Transform\">\n    <appSettings>\n        <add xdt:Transform=\"Remove\"/> \n    </appSettings>\n</configuration>"
        val transformDocument = TestUtils.loadXmlFromString(transformInstruction)

        val transformer = XdtTransformer()
        val result = transformer.transform(baseDocument!!, transformDocument)

        XMLAssert.assertXpathEvaluatesTo("2", "count(/configuration/appSettings/add)", result.asXML())
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
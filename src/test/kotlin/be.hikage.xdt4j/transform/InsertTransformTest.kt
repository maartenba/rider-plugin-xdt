package be.hikage.xdt4j.transform

import be.hikage.xdt4j.XdtTransformer
import be.hikage.xdt4j.XdtTransformerTestBase
import be.hikage.xdt4j.util.TestUtils
import be.hikage.xdt4j.util.XMLAssert
import org.intellij.lang.annotations.Language
import org.junit.Test

class InsertTransformTest : XdtTransformerTestBase() {
    @Test
    @Throws(Exception::class)
    fun TestInsertTransform() {
        @Language("XML")
        val transformInstruction = "<configuration xmlns:xdt=\"http://schemas.microsoft.com/XML-Document-Transform\">\n    <appSettings>\n        <add key=\"key4\" value=\"value4\" xdt:Transform=\"Insert\"/>\n    </appSettings>\n</configuration>"
        val transformDocument = TestUtils.loadXmlFromString(transformInstruction)

        val transformer = XdtTransformer()
        val result = transformer.transform(baseDocument!!, transformDocument)

        XMLAssert.assertXpathEvaluatesTo("4", "count(/configuration/appSettings/add)", result.asXML())
        XMLAssert.assertXpathEvaluatesTo("value4", "/configuration/appSettings/add[@key=\"key4\"]/@value", result.asXML())
    }

    @Test
    @Throws(Exception::class)
    fun TestInsertTransformNoExistingParent() {
        @Language("XML")
        val transformInstruction = "<configuration xmlns:xdt=\"http://schemas.microsoft.com/XML-Document-Transform\">\n    <mySetting>\n        <add key=\"key4\" value=\"value4\" xdt:Transform=\"Insert\"/>\n    </mySetting>\n</configuration>"
        val transformDocument = TestUtils.loadXmlFromString(transformInstruction)

        val transformer = XdtTransformer()
        val result = transformer.transform(baseDocument!!, transformDocument)

        // Must not create missing parent, may create a new Transformer to InsertAll for that
        XMLAssert.assertXpathEvaluatesTo("0", "count(/configuration/mySetting/add)", result.asXML())
    }

    @Test
    @Throws(Exception::class)
    fun TestInsertTransformSpecificChildren() {
        @Language("XML")
        val transformInstruction = "<configuration xmlns:xdt=\"http://schemas.microsoft.com/XML-Document-Transform\">\n    \n    <fileSets>\n        <fileSet id=\"fileset2\" xdt:Locator=\"Match(id)\">\n            <file xdt:Transform=\"Insert\">myfile4.4</file>\n        </fileSet>\n        \n    </fileSets>\n</configuration>"
        val transformDocument = TestUtils.loadXmlFromString(transformInstruction)

        val transformer = XdtTransformer()
        val result = transformer.transform(baseDocument!!, transformDocument)

        XMLAssert.assertXpathEvaluatesTo("3", "count(/configuration/fileSets/fileSet[@id='fileset2']/file)", result.asXML())
        XMLAssert.assertXpathEvaluatesTo("myfile4.4", "/configuration/fileSets/fileSet[@id='fileset2']/file[last()]/text()", result.asXML())
    }
}
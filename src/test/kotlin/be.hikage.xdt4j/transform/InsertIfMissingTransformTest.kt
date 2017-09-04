package be.hikage.xdt4j.transform

import be.hikage.xdt4j.XdtTransformer
import be.hikage.xdt4j.XdtTransformerTestBase
import be.hikage.xdt4j.util.TestUtils
import be.hikage.xdt4j.util.XMLAssert
import org.intellij.lang.annotations.Language
import org.junit.Test

class InsertIfMissingTransformTest : XdtTransformerTestBase() {
    @Test
    @Throws(Exception::class)
    fun TestInsertIfMissingWithMissingElementTransform() {
        @Language("XML")
        val transformInstruction = "<configuration xmlns:xdt=\"http://schemas.microsoft.com/XML-Document-Transform\">\n    <appSettings>\n        <add key=\"newKey1\" value=\"newValue1\" xdt:Transform=\"InsertIfMissing\" xdt:Locator=\"Match(key)\" />\n    </appSettings>\n</configuration>"
        val transformDocument = TestUtils.loadXmlFromString(transformInstruction)

        val transformer = XdtTransformer()
        val result = transformer.transform(baseDocument!!, transformDocument)

        XMLAssert.assertXpathEvaluatesTo("4", "count(/configuration/appSettings/add)", result.asXML())
        XMLAssert.assertXpathEvaluatesTo("newValue1", "/configuration/appSettings/add[@key=\"newKey1\"]/@value", result.asXML())
    }

    @Test
    @Throws(Exception::class)
    fun TestInsertIfMissingWithExistingElementTransform() {
        @Language("XML")
        val transformInstruction = "<configuration xmlns:xdt=\"http://schemas.microsoft.com/XML-Document-Transform\">\n    <appSettings>\n        <add key=\"key1\" value=\"value2\" xdt:Transform=\"InsertIfMissing\" xdt:Locator=\"Match(key)\" />\n    </appSettings>\n</configuration>"
        val transformDocument = TestUtils.loadXmlFromString(transformInstruction)

        val transformer = XdtTransformer()
        val result = transformer.transform(baseDocument!!, transformDocument)

        XMLAssert.assertXpathEvaluatesTo("3", "count(/configuration/appSettings/add)", result.asXML())
        XMLAssert.assertXpathEvaluatesTo("value1", "/configuration/appSettings/add[@key=\"key1\"]/@value", result.asXML())
    }

    @Test
    @Throws(Exception::class)
    fun TestInsertIfMissingTransformNoExistingParent() {
        @Language("XML")
        val transformInstruction = "<configuration xmlns:xdt=\"http://schemas.microsoft.com/XML-Document-Transform\">\n    <mySetting>\n        <add key=\"key4\" value=\"value4\" xdt:Transform=\"InsertIfMissing\" />\n    </mySetting>\n</configuration>"
        val transformDocument = TestUtils.loadXmlFromString(transformInstruction)

        val transformer = XdtTransformer()
        val result = transformer.transform(baseDocument!!, transformDocument)

        // Must not create missing parent, may create a new Transformer to InsertAll for that
        XMLAssert.assertXpathEvaluatesTo("0", "count(/configuration/mySetting/add)", result.asXML())
    }

    @Test
    @Throws(Exception::class)
    fun TestInsertIfMissingWithMissingElementTransformWithChildren() {
        @Language("XML")
        val transformInstruction = "<configuration xmlns:xdt=\"http://schemas.microsoft.com/XML-Document-Transform\">\n    \n    <fileSets>\n        <fileSet id=\"fileset3\" xdt:Transform=\"InsertIfMissing\" xdt:Locator=\"Match(id)\">\n            <file>myfile4.4</file>\n        </fileSet>\n        \n    </fileSets>\n</configuration>"
        val transformDocument = TestUtils.loadXmlFromString(transformInstruction)

        val transformer = XdtTransformer()
        val result = transformer.transform(baseDocument!!, transformDocument)

        XMLAssert.assertXpathEvaluatesTo("1", "count(/configuration/fileSets/fileSet[@id='fileset3']/file)", result.asXML())
        XMLAssert.assertXpathEvaluatesTo("myfile4.4", "/configuration/fileSets/fileSet[@id='fileset3']/file[last()]/text()", result.asXML())
    }

    @Test
    @Throws(Exception::class)
    fun TestInsertIfMissingWithExistingElementTransformWithChildren() {
        @Language("XML")
        val transformInstruction = "<configuration xmlns:xdt=\"http://schemas.microsoft.com/XML-Document-Transform\">\n    \n    <fileSets>\n        <fileSet id=\"fileset2\" xdt:Transform=\"InsertIfMissing\" xdt:Locator=\"Match(id)\">\n            <file>myfile4.4</file>\n        </fileSet>\n        \n    </fileSets>\n</configuration>"
        val transformDocument = TestUtils.loadXmlFromString(transformInstruction)

        val transformer = XdtTransformer()
        val result = transformer.transform(baseDocument!!, transformDocument)

        XMLAssert.assertXpathEvaluatesTo("0", "count(/configuration/fileSets/fileSet[@id='fileset3']/file)", result.asXML())
        XMLAssert.assertXpathEvaluatesTo("2", "count(/configuration/fileSets/fileSet[@id='fileset2']/file)", result.asXML())
        XMLAssert.assertXpathEvaluatesTo("myfile4", "/configuration/fileSets/fileSet[@id='fileset2']/file[last()]/text()", result.asXML())
    }
}
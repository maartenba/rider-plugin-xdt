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

class RemoveAllTransformTest : XdtTransformerTestBase() {
    @Test
    @Throws(DocumentException::class, IOException::class, SAXException::class, XPathException::class)
    fun TestRemoveAllTransform() {
        @Language("XML")
        val transformInstruction = "<configuration xmlns:xdt=\"http://schemas.microsoft.com/XML-Document-Transform\">\n    <appSettings>\n        <add xdt:Transform=\"RemoveAll\"/> \n    </appSettings>\n</configuration>"
        val transformDocument = TestUtils.loadXmlFromString(transformInstruction)

        val transformer = XdtTransformer()
        val result = transformer.transform(baseDocument!!, transformDocument)

        XMLAssert.assertXpathEvaluatesTo("0", "count(/configuration/appSettings/add)", result.asXML())
    }
}
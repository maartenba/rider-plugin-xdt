package be.hikage.xdt4j.util

import org.dom4j.Document
import org.dom4j.DocumentException
import org.dom4j.DocumentHelper
import org.dom4j.Element
import org.dom4j.io.SAXReader

class TestUtils {
    companion object {
        @Throws(DocumentException::class)
        fun loadSampleBase(): Document = DocumentHelper.parseText("<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n" +
                "<configuration>\n" +
                "    <connectionStrings>\n" +
                "        <add name=\"myconnection\" connectionString=\"value-dev\"/>\n" +
                "    </connectionStrings>\n" +
                "    <appSettings>\n" +
                "        <add key=\"key1\" value=\"value1\"/>\n" +
                "        <add key=\"key2\" value=\"value2-dev\"/>\n" +
                "        <add key=\"key3\" value=\"value3-dev\"/>\n" +
                "    </appSettings>\n" +
                "    <fileSets>\n" +
                "        <fileSet id=\"fileset1\">\n" +
                "            <file>myfile1</file>\n" +
                "            <file>myfile2</file>\n" +
                "        </fileSet>\n" +
                "        <fileSet id=\"fileset2\">\n" +
                "            <file>myfile3</file>\n" +
                "            <file>myfile4</file>\n" +
                "        </fileSet>\n" +
                "    </fileSets>\n" +
                "    <system.net>\n" +
                "        <mailSettings>\n" +
                "            <smtp>\n" +
                "                <network host=\"127.0.0.1\"/>\n" +
                "            </smtp>\n" +
                "        </mailSettings>\n" +
                "    </system.net>\n" +
                "    <system.web>\n" +
                "        <compilation debug=\"true\" defaultLanguage=\"F#\">\n" +
                "            <!-- this is a comment -->\n" +
                "        </compilation>\n" +
                "    </system.web>\n" +
                "</configuration>")

        @Suppress("unused")
        @Throws(DocumentException::class)
        fun loadXml(filename: String): Document {
            val stream = TestUtils::class.java.classLoader.getResourceAsStream(filename)

            val reader = SAXReader()
            return reader.read(stream)
        }

        @Throws(DocumentException::class)
        fun loadXmlFromString(xml: String): Document = DocumentHelper.parseText(xml)

        fun loadElement(xmlDoc: String, xPath: String): Element {
            return try {
                DocumentHelper.parseText(xmlDoc).selectSingleNode(xPath) as Element
            } catch (e: Exception) {
                throw RuntimeException("Bad XPath expression", e)
            }
        }
    }
}
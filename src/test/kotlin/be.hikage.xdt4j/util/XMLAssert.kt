package be.hikage.xdt4j.util

import org.junit.Assert
import org.xmlunit.matchers.CompareMatcher
import org.xmlunit.xpath.JAXPXPathEngine

class XMLAssert {
    companion object {
        fun assertXMLEqual(expected: String, actual: String) {
            Assert.assertThat(expected, CompareMatcher.isIdenticalTo(actual));        }

        fun assertXpathEvaluatesTo(expected: String, query: String, source: String) {
            Assert.assertEquals(expected, JAXPXPathEngine()
                    .evaluate(query, org.xmlunit.builder.Input.fromString(source).build()))
        }
    }
}
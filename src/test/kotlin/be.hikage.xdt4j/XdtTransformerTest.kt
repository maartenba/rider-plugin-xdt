package be.hikage.xdt4j

import be.hikage.xdt4j.util.TestUtils
import org.dom4j.Document
import org.junit.Before

abstract class XdtTransformerTestBase {
    protected var baseDocument: Document? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        baseDocument = TestUtils.loadSampleBase()
    }
}

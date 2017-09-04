package be.maartenballiauw.rider.xdt.util

import org.dom4j.Document
import org.dom4j.DocumentHelper
import org.dom4j.DocumentException

class XmlUtil {
    companion object {
        @Throws(DocumentException::class)
        fun loadDocumentFromString(xml: String): Document {
            return DocumentHelper.parseText(xml)
        }
    }
}